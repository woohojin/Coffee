package org.daCoffee.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.daCoffee.dto.request.admin.MemberRequestDTO;
import org.daCoffee.dto.request.admin.OrderHistoryRequestDTO;
import org.daCoffee.dto.request.admin.ProductRequestDTO;
import org.daCoffee.entity.*;
import org.daCoffee.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/admin/")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class AdminController {
  private final ProductService productService;
  private final MemberService memberService;
  private final OrderHistoryService orderHistoryService;
  private final ProductImageService productImageService;

  private static final int LIMIT = 30;
  private static final int BOTTOM_LINE = 100;

  // 어드민 페이지 페이지네이션 계산 함수
  private Map<String, Integer> calculatePagination(int pageInt, int count) {
    Map<String, Integer> paginationInfo = new HashMap<>();

    int start = (pageInt - 1) / BOTTOM_LINE * BOTTOM_LINE + 1;
    int end = start + BOTTOM_LINE - 1;
    int maxPage = (count / LIMIT) + (count % LIMIT == 0 ? 0 : 1);

    if (end > maxPage) end = maxPage;
    if (end > count) end = count;

    paginationInfo.put("start", start);
    paginationInfo.put("end", end);

    return paginationInfo;
  }

  @RequestMapping("dashboard")
  public String index() {

    return "admin/dashboard";
  }

  @RequestMapping("productList")
  public String productList(Model model,
                            @RequestParam(defaultValue = "1") int pageInt) {

    Page<Product> result = productService.findAllPaged(pageInt, LIMIT, "product_code", "asc");

    Map<String, Integer> pagination = calculatePagination(pageInt, (int) result.getTotalElements());

    model.addAttribute("list", result.getContent());
    model.addAttribute("productCount", result.getTotalElements());
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", pagination.get("start"));
    model.addAttribute("end", pagination.get("end"));

    return "admin/productList";
  }

  @RequestMapping("productListPro")
  public String productListPro(Model model,
                               @RequestParam String columnName,
                               @RequestParam String orderBy,
                               @RequestParam(defaultValue = "1") int pageInt) {

    Page<Product> result = productService.findAllPaged(pageInt, LIMIT, columnName, orderBy);
    Map<String, Integer> pagination = calculatePagination(pageInt, (int) result.getTotalElements());

    model.addAttribute("list", result.getContent());
    model.addAttribute("productCount", result.getTotalElements());
    model.addAttribute("columnName", columnName);
    model.addAttribute("orderBy", orderBy);
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", pagination.get("start"));
    model.addAttribute("end", pagination.get("end"));

    return "admin/productList";
  }

  @RequestMapping("productSearch")
  public String productSearch(Model model,
                              @RequestParam(defaultValue = "1") int pageInt,
                              @RequestParam(required = false) String columnName,
                              @RequestParam(required = false) String searchText) {

    Page<Product> result = productService.searchProducts(columnName, searchText, pageInt, LIMIT);
    Map<String, Integer> pagination = calculatePagination(pageInt, (int) result.getTotalElements());

    model.addAttribute("list", result.getContent());
    model.addAttribute("productCount", result.getTotalElements());
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", pagination.get("start"));
    model.addAttribute("end", pagination.get("end"));

    return "admin/productList";
  }

  @RequestMapping("productSoldOutUpdate")
  public String productSoldOutUpdate(Model model, @RequestParam(defaultValue = "1") int pageInt) {

    Page<Product> result = productService.findAllPaged(pageInt, LIMIT, "product_code", "asc");
    Map<String, Integer> pagination = calculatePagination(pageInt, (int) result.getTotalElements());

    model.addAttribute("list", result.getContent());
    model.addAttribute("productCount", result.getTotalElements());
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", pagination.get("start"));
    model.addAttribute("end", pagination.get("end"));

    return "admin/productSoldOutUpdate";
  }


  @RequestMapping("productSoldOutUpdatePro")
  public String productSoldOutUpdatePro(Model model, String productCode, boolean productSoldOut) {

    productService.updateSoldOut(productCode, productSoldOut);

    model.addAttribute("url", "/admin/productSoldOutUpdate");
    model.addAttribute("msg", "제품 품절 수정 성공");

    return "alert";
  }

  @RequestMapping("productUpload")
  public String productUpload() {
    return "admin/productUpload";
  }

  @RequestMapping("productUploadPro")
  public String productUploadPro(HttpServletRequest request, Model model, MultipartHttpServletRequest files, ProductRequestDTO dto,
                                 @SessionAttribute(name="memberId") String adminId) {

    String adminName = memberService.findById(adminId)
      .map(Member::getMemberName)
      .orElse("admin");

    String msg = "제품 등록에 실패하였습니다.";
    String url = "/admin/productUpload";

    if (productService.findById(dto.getProductCode()).isPresent()) {
      model.addAttribute("msg", "이미 존재하는 제품입니다.");
      model.addAttribute("url", url);
      return "alert";
    }

    String filePath = request.getServletContext().getRealPath("/") + "view/files/"
      + (dto.getProductType() == 1 ? "mix" : dto.getProductType() == 2 ? "cafe" : "bean")
      + "/" + dto.getProductCode();

    File uploadPath = new File(filePath);
    if (!uploadPath.exists()) {
      boolean created = uploadPath.mkdirs();
      if (!created) log.error("디렉토리 생성 실패: {}", filePath);
    }

    List<MultipartFile> fileList = files.getFiles("files");

    if (!fileList.isEmpty()) {
      String thumbnailFileName = null;

      for (MultipartFile file : fileList) {
        String fileName = file.getOriginalFilename();
        try {
          file.transferTo(new File(filePath, fileName));
        } catch (IOException e) {
          log.error("파일 업로드 실패: {}", e.getMessage());
        }
        if (fileName != null && fileName.contains("thumbnail")) {
          thumbnailFileName = fileName;
        }
      }

      Product product = Product.builder()
        .productCode(dto.getProductCode())
        .productType(dto.getProductType())
        .productName(dto.getProductName())
        .productPrice(dto.getProductPrice())
        .productUnit(dto.getProductUnit())
        .productTier(dto.getProductTier())
        .productFile(thumbnailFileName)
        .productSoldOut(false)
        .productRegisterName(adminName)
        .productRegisterDate(LocalDate.now())
        .build();
      productService.saveProduct(product);

      for (MultipartFile file : fileList) {
        ProductImage image = ProductImage.builder()
          .product(product)
          .fileName(file.getOriginalFilename())
          .fileRegisterName(adminName)
          .fileRegisterDate(LocalDate.now())
          .build();
        productImageService.save(image);
      }

      if (dto.getProductType() == 0) {
        Bean bean = Bean.builder()
          .productCode(dto.getProductCode())
          .product(product)
          .beanSpecies(dto.getBeanSpecies())
          .beanCompany(dto.getBeanCompany())
          .beanUseByDate(dto.getBeanUseByDate())
          .beanCountry(dto.getBeanCountry())
          .beanRegisterName(adminName)
          .beanRegisterDate(LocalDate.now())
          .build();
        productService.saveBean(bean);
      } else if (dto.getProductType() == 1) {
        Mix mix = Mix.builder()
          .productCode(dto.getProductCode())
          .product(product)
          .mixCompany(dto.getMixCompany())
          .mixUseByDate(dto.getMixUseByDate())
          .mixRegisterName(adminName)
          .mixRegisterDate(LocalDate.now())
          .build();
        productService.saveMix(mix);
      }

      msg = "제품 등록에 성공하였습니다.";
    } else {
      msg = "업로드 된 파일이 없습니다.";
    }

    model.addAttribute("msg", msg);
    model.addAttribute("url", url);

    return "alert";
  }


  @RequestMapping("productUpdate")
  public String productUpdate(Model model, @RequestParam String productCode) {

    Product product = productService.findById(productCode)
      .orElseThrow(() -> new IllegalArgumentException("제품 없음: " + productCode));

    model.addAttribute("product", product);

    // cafe는 product 테이블 컬럼으로 충분해 테이블이 없음
    if (product.getProductType() == 0) {
      productService.findBeanById(productCode)
        .ifPresent(bean -> model.addAttribute("bean", bean));
    } else if (product.getProductType() == 1) {
      productService.findMixById(productCode)
        .ifPresent(mix -> model.addAttribute("mix", mix));
    }

    return "admin/productUpdate";
  }

  @RequestMapping("productUpdatePro")
  public String productUpdatePro(HttpServletRequest request, Model model, MultipartHttpServletRequest files, ProductRequestDTO dto,
                                 @SessionAttribute(name="memberId") String adminId) {

    String adminName = memberService.findById(adminId)
      .map(Member::getMemberName)
      .orElse("admin");

    String msg = "제품 수정에 실패하였습니다.";
    String url = "/admin/productList";

    Product product = productService.findById(dto.getExistProductCode()).orElse(null);

    if (product == null) {
      model.addAttribute("msg", "제품이 존재하지 않습니다.");
      model.addAttribute("url", url);
      return "alert";
    }

    String filePath = request.getServletContext().getRealPath("/") + "view/files/"
      + (dto.getProductType() == 1 ? "mix" : dto.getProductType() == 2 ? "cafe" : "bean")
      + "/" + dto.getProductCode();

    File uploadPath = new File(filePath);
    if (!uploadPath.exists()) {
      boolean created = uploadPath.mkdirs();
      if (!created) log.error("디렉토리 생성 실패: {}", filePath);
    }

    List<MultipartFile> fileList = files.getFiles("files");

    if (!fileList.isEmpty()) {
      String thumbnailFileName = product.getProductFile();

      for (MultipartFile file : fileList) {
        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isEmpty()) continue;

        try {
          File dest = new File(filePath, fileName);
          if (dest.exists()) {
            boolean deleted = dest.delete();
            if (!deleted) log.warn("기존 파일 삭제 실패: {}", dest.getAbsolutePath());
          }
          file.transferTo(dest);
        } catch (IOException e) {
          log.error("파일 업로드 실패: {}", e.getMessage());
          model.addAttribute("msg", "파일 업로드에 실패했습니다: " + fileName);
          model.addAttribute("url", url);
          return "alert";
        }

        if (fileName.contains("thumbnail")) thumbnailFileName = fileName;

        final String finalFileName = fileName;
        boolean exists = productImageService.findByProductCode(dto.getProductCode())
          .stream()
          .anyMatch(img -> img.getFileName().equals(finalFileName));

        if (exists) {
          productImageService.findByProductCode(dto.getProductCode())
            .stream()
            .filter(img -> img.getFileName().equals(finalFileName))
            .findFirst()
            .ifPresent(img -> productImageService.updateModifier(img.getFileId(), finalFileName, adminName));
        } else {
          ProductImage image = ProductImage.builder()
            .product(product)
            .fileName(finalFileName)
            .fileRegisterName(adminName)
            .fileRegisterDate(LocalDate.now())
            .build();
          productImageService.save(image);
        }
      }

      product.adminUpdateProduct(dto, thumbnailFileName, adminName);

      if (dto.getProductType() == 0) {
        productService.findBeanById(dto.getExistProductCode())
          .ifPresent(bean -> bean.adminUpdate(dto, adminName));
      } else if (dto.getProductType() == 1) {
        productService.findMixById(dto.getExistProductCode())
          .ifPresent(mix -> mix.adminUpdate(dto, adminName));
      }
        msg = "제품 수정에 성공하였습니다.";
    } else {
      msg = "업로드 된 파일이 없습니다.";
    }

    model.addAttribute("msg", msg);
    model.addAttribute("url", url);

    return "alert";
  }

  @RequestMapping("productDelete")
  public String productDelete(Model model, @RequestParam(defaultValue = "1") int pageInt) {

    Page<Product> result = productService.findAllPaged(pageInt, LIMIT, "product_code", "asc");
    Map<String, Integer> pagination = calculatePagination(pageInt, (int) result.getTotalElements());

    model.addAttribute("list", result.getContent());
    model.addAttribute("productCount", result.getTotalElements());
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", pagination.get("start"));
    model.addAttribute("end", pagination.get("end"));

    return "admin/productDelete";
  }

  @RequestMapping("productDeletePro")
  public String productDeletePro(Model model, String productCode) {

    String msg = "제품 삭제에 실패했습니다.";
    String url = "/admin/productDelete";

    productService.deleteProduct(productCode);

    model.addAttribute("msg", msg);
    model.addAttribute("url", url);

    return "alert";
  }

  @RequestMapping("memberList")
  public String memberList(Model model,
                           @RequestParam(defaultValue = "1") int pageInt) {


    Page<Member> result = memberService.findAllPaged(pageInt, LIMIT, "member_tier", "asc");
    Map<String, Integer> pagination = calculatePagination(pageInt, (int) result.getTotalElements());

    model.addAttribute("list", result.getContent());
    model.addAttribute("memberCount", result.getTotalElements());
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", pagination.get("start"));
    model.addAttribute("end", pagination.get("end"));

    return "admin/memberList";
  }

  @RequestMapping("memberListPro")
  public String memberListPro(Model model,
                              @RequestParam(defaultValue = "1") int pageInt,
                              @RequestParam String columnName,
                              @RequestParam String orderBy) {

    Page<Member> result = memberService.findAllPaged(pageInt, LIMIT, columnName, orderBy);
    Map<String, Integer> pagination = calculatePagination(pageInt, (int) result.getTotalElements());

    model.addAttribute("list", result.getContent());
    model.addAttribute("memberCount", result.getTotalElements());
    model.addAttribute("columnName", columnName);
    model.addAttribute("orderBy", orderBy);
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", pagination.get("start"));
    model.addAttribute("end", pagination.get("end"));

    return "admin/memberList";
  }

  @RequestMapping("memberUpdate")
  public String memberUpdate(Model model, String memberId) {

    Member member = memberService.findById(memberId)
      .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + memberId));

    model.addAttribute("member", member);

    return "admin/memberUpdate";
  }

  @RequestMapping("memberUpdatePro")
  public String memberUpdatePro(Model model, MemberRequestDTO dto,
                                @SessionAttribute(name="memberId") String adminId) {

    String adminName = memberService.findById(adminId)
      .map(Member::getMemberName)
      .orElse("admin");

    memberService.adminUpdate(adminName, dto);

    model.addAttribute("url", "/admin/memberList");
    model.addAttribute("msg", "회원 정보가 수정되었습니다.");

    return "alert";
  }

  @RequestMapping("memberSearch")
  public String memberSearch(Model model,
                             @RequestParam(defaultValue = "1") int pageInt,
                             @RequestParam(required = false) String columnName,
                             @RequestParam(required = false) String searchText) {

    Page<Member> result = memberService.searchMembers(columnName, searchText, pageInt, LIMIT);
    Map<String, Integer> pagination = calculatePagination(pageInt, (int) result.getTotalElements());

    model.addAttribute("list", result.getContent());
    model.addAttribute("memberCount", result.getTotalElements());
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", pagination.get("start"));
    model.addAttribute("end", pagination.get("end"));

    return "admin/memberList";
  }

  @RequestMapping("memberWithdrawalList")
  public String memberWithdrawalList(Model model, @RequestParam(defaultValue = "1") int pageInt) {

    Page<MemberWithdrawal> result = memberService.findWithdrawalMembers(pageInt, LIMIT);
    Map<String, Integer> pagination = calculatePagination(pageInt, (int) result.getTotalElements());

    model.addAttribute("list", result.getContent());
    model.addAttribute("memberCount", result.getTotalElements());
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", pagination.get("start"));
    model.addAttribute("end", pagination.get("end"));

    return "admin/memberWithdrawalList";
  }

  @RequestMapping("memberTierUpdate")
  public String memberTierUpdate(Model model) {

    Page<Member> result = memberService.searchMembers("memberTier", "0", 1, 32);

    model.addAttribute("list", result.getContent());
    model.addAttribute("memberCount", result.getTotalElements());

    return "admin/memberTierUpdate";
  }

  @RequestMapping("memberTierUpdatePro")
  public String memberTierUpdatePro(Model model, String memberId, int memberTier) {

    memberService.updateMemberTier(memberId, memberTier);

    model.addAttribute("url", "/admin/memberTierUpdate");
    model.addAttribute("msg", "멤버 등급 수정 성공");

    return "alert";
  }

  @RequestMapping("memberDisableUpdate")
  public String memberDisableUpdate(Model model) {

    Page<Member> result = memberService.findDisabledMembers(1, 32);

    model.addAttribute("list", result.getContent());

    return "admin/memberDisable";
  }

  @RequestMapping("memberDisableUpdatePro")
  public String memberDisableUpdatePro(Model model, String memberId) {

    memberService.toggleDisable(memberId);

    model.addAttribute("url", "/admin/memberDisableUpdate");
    model.addAttribute("msg", "멤버 비활성화 상태 수정 성공");

    return "alert";
  }

  @RequestMapping("orderHistory")
  public String orderHistory(Model model,
                             @RequestParam(defaultValue = "1") int pageInt) {

    Page<OrderHistory> result = orderHistoryService.findAllPaged(pageInt, LIMIT, "order_num", "desc");
    Map<String, Integer> pagination = calculatePagination(pageInt, (int) result.getTotalElements());

    model.addAttribute("list", result.getContent());
    model.addAttribute("historyCount", result.getTotalElements());
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", pagination.get("start"));
    model.addAttribute("end", pagination.get("end"));

    return "admin/orderHistory";
  }

  @RequestMapping("orderHistoryPro")
  public String orderHistoryPro(Model model,
                                @RequestParam(defaultValue = "1") int pageInt,
                                @RequestParam String columnName,
                                @RequestParam String orderBy) {

    Page<OrderHistory> result = orderHistoryService.findAllPaged(pageInt, LIMIT, columnName, orderBy);
    Map<String, Integer> pagination = calculatePagination(pageInt, (int) result.getTotalElements());

    model.addAttribute("list", result.getContent());
    model.addAttribute("historyCount", result.getTotalElements());
    model.addAttribute("columnName", columnName);
    model.addAttribute("orderBy", orderBy);
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", pagination.get("start"));
    model.addAttribute("end", pagination.get("end"));

    return "admin/orderHistory";
  }

  @RequestMapping("orderHistoryUpdate")
  public String orderHistoryUpdate(Model model, String orderId, String productCode) {

    OrderHistory history = orderHistoryService.findByOrderIdAndProductCode(orderId, productCode)
      .orElseThrow(() -> new IllegalArgumentException("주문 없음"));

    model.addAttribute("history", history);

    return "admin/orderHistoryUpdate";
  }

  @RequestMapping("orderHistoryUpdatePro")
  public String orderHistoryUpdatePro(Model model,
                                      OrderHistoryRequestDTO dto,
                                      @SessionAttribute(name = "memberId") String adminId) {

    String adminName = memberService.findById(adminId)
      .map(Member::getMemberName)
      .orElse("admin");

    boolean success = orderHistoryService.adminUpdate(adminName, dto);

    model.addAttribute("msg", success ? "주문 기록이 수정되었습니다." : "주문 기록 수정에 실패했습니다.");
    model.addAttribute("url", "/admin/orderHistory");

    return "alert";
  }

  @RequestMapping("orderHistoryDelete")
  public String orderHistoryDelete(Model model, String orderId, String productCode) {

    model.addAttribute("orderId", orderId);
    model.addAttribute("productCode", productCode);

    return "/admin/orderHistoryDelete";
  }

  @RequestMapping("orderHistoryDeletePro")
  public String orderHistoryDeletePro(HttpServletRequest request, Model model, String orderId, String productCode, String confirmDelete) {

    String msg;

    if(orderId.equals(confirmDelete)) {
      boolean success = orderHistoryService.adminDelete(orderId, productCode);
      msg = success ? "주문 기록 삭제에 성공했습니다." : "주문 기록 삭제에 실패했습니다.";
    } else {
      msg = "주문번호와 일치하지 않습니다.";
    }

    model.addAttribute("msg", msg);
    model.addAttribute("url", "/admin/orderHistory");

    return "alert";
  }

  @RequestMapping("historySearch")
  public String historySearch(HttpServletRequest request, Model model,
                              @RequestParam(defaultValue = "1") int pageInt) {

    String orderId = request.getParameter("historyCode");
    String memberId = request.getParameter("memberId");
    String startDateStr = request.getParameter("startDate");
    String endDateStr = request.getParameter("endDate");

    LocalDateTime startDate = (startDateStr != null && !startDateStr.isEmpty())
      ? LocalDateTime.parse(startDateStr + "T00:00:00") : null;
    LocalDateTime endDate = (endDateStr != null && !endDateStr.isEmpty())
      ? LocalDateTime.parse(endDateStr + "T23:59:59") : null;

    Page<OrderHistory> result = orderHistoryService.searchOrders(
      orderId, memberId, startDate, endDate, pageInt, LIMIT);
    Map<String, Integer> pagination = calculatePagination(pageInt, (int) result.getTotalElements());

    model.addAttribute("list", result.getContent());
    model.addAttribute("historyCount", result.getTotalElements());
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", pagination.get("start"));
    model.addAttribute("end", pagination.get("end"));

    return "admin/orderHistory";
  }

//  @RequestMapping("memberFranCodeUpdate")
//  public String memberFranCodeUpdate() throws Exception {
//    Integer memberTier = (Integer) session.getAttribute("memberTier");
//    if(memberTier == null) {
//      memberTier = 0;
//    }
//
//    List<Member> list;
//
//    list = memberDao.memberSearchListByMemberFranCodeByNull(1, 32);
//    model.addAttribute()("list", list);
//
//    model.addAttribute()("memberTier", memberTier);
//
//    return "admin/memberFranCodeUpdate";
//  }
//
//  @RequestMapping("memberFranCodeUpdatePro")
//  public String memberFranCodeUpdatePro(String memberId, String memberFranCode) throws Exception {
//    memberDao.memberFranCodeUpdate(memberId, memberFranCode);
//
//    String url = "/admin/memberFranCodeUpdate";
//    String msg ="멤버 가맹점코드 수정 성공";
//
//    model.addAttribute()("url", url);
//    model.addAttribute()("msg", msg);
//
//    return "alert";
//  }

  @RequestMapping("excelProductDownload")
  public void excelProductDownload(HttpServletResponse response) throws IOException {

    List<Product> list = productService.findAllPaged(1, Integer.MAX_VALUE, "product_code", "asc").getContent();

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment; filename=productList.xlsx");

    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("제품 리스트");

    Row headerRow = sheet.createRow(0);
    headerRow.createCell(0).setCellValue("제품번호");
    headerRow.createCell(1).setCellValue("제품종류");
    headerRow.createCell(2).setCellValue("제품명");
    headerRow.createCell(3).setCellValue("가격");
    headerRow.createCell(4).setCellValue("단위");
    headerRow.createCell(5).setCellValue("등급");
    headerRow.createCell(6).setCellValue("품절여부");
    headerRow.createCell(7).setCellValue("등록자");
    headerRow.createCell(8).setCellValue("등록일");
    headerRow.createCell(9).setCellValue("수정자");
    headerRow.createCell(10).setCellValue("수정일");

    int rowNum = 1;
    for (Product p : list) {
      Row row = sheet.createRow(rowNum++);
      row.createCell(0).setCellValue(p.getProductCode());
      row.createCell(1).setCellValue(p.getProductType());
      row.createCell(2).setCellValue(p.getProductName());
      row.createCell(3).setCellValue(p.getProductPrice());
      row.createCell(4).setCellValue(p.getProductUnit());
      row.createCell(5).setCellValue(p.getProductTier());
      row.createCell(6).setCellValue(p.isProductSoldOut());
      row.createCell(7).setCellValue(p.getProductRegisterName());
      row.createCell(8).setCellValue(String.valueOf(p.getProductRegisterDate()));
      row.createCell(9).setCellValue(p.getProductModifierName());
      row.createCell(10).setCellValue(String.valueOf(p.getProductModifierDate()));
    }

    ServletOutputStream outputStream = response.getOutputStream();
    workbook.write(outputStream);
    workbook.close();
    outputStream.close();
  }

  @RequestMapping("excelMemberDownload")
  public void excelMemberDownload(HttpServletResponse response) throws IOException {

    List<Member> list = memberService.findAllPaged(1, Integer.MAX_VALUE, "member_tier", "asc").getContent();

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment; filename=memberList.xlsx");

    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("회원 리스트");

    Row headerRow = sheet.createRow(0);
    headerRow.createCell(0).setCellValue("등급");
    headerRow.createCell(1).setCellValue("아이디");
    headerRow.createCell(2).setCellValue("이름");
    headerRow.createCell(3).setCellValue("회사명");
    headerRow.createCell(4).setCellValue("전화번호");
    headerRow.createCell(5).setCellValue("회사번호");
    headerRow.createCell(6).setCellValue("주소");
    headerRow.createCell(7).setCellValue("배송지");
    headerRow.createCell(8).setCellValue("이메일");
    headerRow.createCell(9).setCellValue("가맹점코드");
    headerRow.createCell(10).setCellValue("가입일");
    headerRow.createCell(11).setCellValue("비활성화일");
    headerRow.createCell(12).setCellValue("수정자");
    headerRow.createCell(13).setCellValue("수정일");

    int rowNum = 1;
    for (Member m : list) {
      Row row = sheet.createRow(rowNum++);
      row.createCell(0).setCellValue(m.getMemberTier());
      row.createCell(1).setCellValue(m.getMemberId());
      row.createCell(2).setCellValue(m.getMemberName());
      row.createCell(3).setCellValue(m.getMemberCompanyName());
      row.createCell(4).setCellValue(m.getMemberTel());
      row.createCell(5).setCellValue(m.getMemberCompanyTel());
      row.createCell(6).setCellValue(m.getMemberAddress() + " " + m.getMemberDetailAddress());
      row.createCell(7).setCellValue(m.getMemberDeliveryAddress() + " " + m.getMemberDetailDeliveryAddress());
      row.createCell(8).setCellValue(m.getMemberEmail());
      row.createCell(9).setCellValue(m.getMemberFranCode());
      row.createCell(10).setCellValue(String.valueOf(m.getMemberDate()));
      row.createCell(11).setCellValue(String.valueOf(m.getMemberDisableDate()));
      row.createCell(12).setCellValue(m.getMemberModifierName());
      row.createCell(13).setCellValue(String.valueOf(m.getMemberModifierDate()));
    }

    ServletOutputStream outputStream = response.getOutputStream();
    workbook.write(outputStream);
    workbook.close();
    outputStream.close();
  }

  @RequestMapping("excelHistoryDownload")
  public void excelHistoryDownload(HttpServletResponse response) throws IOException {

    List<OrderHistory> list = orderHistoryService.findAllPaged(1, Integer.MAX_VALUE, "order_num", "desc").getContent();

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment; filename=historyList.xlsx");

    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("주문기록");

    Row headerRow = sheet.createRow(0);

    headerRow.createCell(0).setCellValue("주문번호");
    headerRow.createCell(1).setCellValue("회원등급");
    headerRow.createCell(2).setCellValue("아이디");
    headerRow.createCell(3).setCellValue("주문자명");
    headerRow.createCell(4).setCellValue("가맹점코드");
    headerRow.createCell(5).setCellValue("제품번호");
    headerRow.createCell(6).setCellValue("제품이름");
    headerRow.createCell(7).setCellValue("수량");
    headerRow.createCell(8).setCellValue("용량");
    headerRow.createCell(9).setCellValue("가격");
    headerRow.createCell(10).setCellValue("합계");
    headerRow.createCell(11).setCellValue("주문일");
    headerRow.createCell(12).setCellValue("배송지");

    int rowNum = 1;
    for (OrderHistory h : list) {
      Row row = sheet.createRow(rowNum++);
      row.createCell(0).setCellValue(h.getOrderId());
      row.createCell(1).setCellValue(h.getMemberTier());
      row.createCell(2).setCellValue(h.getMemberId());
      row.createCell(3).setCellValue(h.getMemberName());
      row.createCell(4).setCellValue(h.getMemberFranCode());
      row.createCell(5).setCellValue(h.getProductCode());
      row.createCell(6).setCellValue(h.getProductName());
      row.createCell(7).setCellValue(h.getQuantity());
      row.createCell(8).setCellValue(h.getProductUnit());
      row.createCell(9).setCellValue(h.getProductPrice());
      row.createCell(10).setCellValue(h.getTotalPrice());
      row.createCell(11).setCellValue(String.valueOf(h.getOrderDate()));
      row.createCell(12).setCellValue(h.getDeliveryAddress() + " " + h.getDetailDeliveryAddress());
    }

    ServletOutputStream outputStream = response.getOutputStream();
    workbook.write(outputStream);
    workbook.close();
    outputStream.close();
  }

}
