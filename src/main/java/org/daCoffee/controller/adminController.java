package org.daCoffee.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.daCoffee.dao.*;
import org.daCoffee.dto.HistoryDTO;
import org.daCoffee.dto.ImageDTO;
import org.daCoffee.dto.MemberDTO;
import org.daCoffee.dto.ProductDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/")
@Slf4j
public class adminController {
  private final ProductDAO productDao;
  private final MemberDAO memberDao;
  private final HistoryDAO historyDao;
  private final ImageDAO imageDao;
  private final CartDAO cartDao;

  @Autowired
  public adminController(ProductDAO productDao, MemberDAO memberDao, HistoryDAO historyDao, ImageDAO imageDao, CartDAO cartDao) {
    this.productDao = productDao;
    this.memberDao = memberDao;
    this.historyDao = historyDao;
    this.imageDao = imageDao;
    this.cartDao = cartDao;
  }

  int limit = 30; // 한 page당 게시물 개수
  int bottomLine = 100; // pagination 개수

  // 어드민 페이지 페이지네이션 계산 함수
  public Map<String, Integer> calculatePagination(int pageInt, int count) {
    Map<String, Integer> paginationInfo = new HashMap<>();

    int start = (pageInt - 1) / bottomLine * bottomLine + 1;
    int end = start + bottomLine - 1;
    int maxPage = (count / limit) + (count % limit == 0 ? 0 : 1);

    if (end > maxPage) {
      end = maxPage;
    }
    if (end > count) {
      end = count;
    }

    paginationInfo.put("start", start);
    paginationInfo.put("end", end);

    return paginationInfo;
  }

  @RequestMapping("dashboard")
  public String index() throws Exception {

    return "admin/dashboard";
  }

  @RequestMapping("productList")
  public String productList(HttpServletRequest request, HttpSession session, Model model,
                            @RequestParam(defaultValue = "1") int pageInt,
                            @SessionAttribute int memberTier) throws Exception {


    int productCount = 0;

    if(memberTier == 9) {
      productDao.rownumSet();
      List<ProductDTO> list = productDao.productList(pageInt, limit);
      productCount = productDao.productCount();
      model.addAttribute("list", list);
    }

    Map<String, Integer> paginationInfo = calculatePagination(pageInt, productCount);
    int start = paginationInfo.get("start");
    int end = paginationInfo.get("end");

    model.addAttribute("memberTier", memberTier);
    model.addAttribute("productCount", productCount);
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    model.addAttribute("pageInt", pageInt);

    return "admin/productList";
  }

  @RequestMapping("productListPro")
  public String productListPro(HttpServletRequest request, HttpSession session, Model model,
                               @RequestParam String columnName,
                               @RequestParam String orderBy,
                               @RequestParam(defaultValue = "1") int pageInt,
                               @SessionAttribute int memberTier) throws Exception {

    int productCount = 0;

    if(memberTier == 9) {
      productDao.rownumSet();
      List<ProductDTO> list;

      switch (columnName) {
        case "product_type":
          list = ("desc".equals(orderBy)) ? productDao.productListDescByProductType(pageInt, limit) :
            productDao.productListByProductType(pageInt, limit);
          break;
        case "product_code":
          list = ("desc".equals(orderBy)) ? productDao.productListDescByProductCode(pageInt, limit) :
            productDao.productListByProductCode(pageInt, limit);
          break;
        case "product_name":
          list = ("desc".equals(orderBy)) ? productDao.productListDescByProductName(pageInt, limit) :
            productDao.productListByProductName(pageInt, limit);
          break;
        case "product_unit":
          list = ("desc".equals(orderBy)) ? productDao.productListDescByProductUnit(pageInt, limit) :
            productDao.productListByProductUnit(pageInt, limit);
          break;
        case "product_price":
          list = ("desc".equals(orderBy)) ? productDao.productListDescByProductPrice(pageInt, limit) :
            productDao.productListByProductPrice(pageInt, limit);
          break;
        case "product_tier":
          list = ("desc".equals(orderBy)) ? productDao.productListDescByProductTier(pageInt, limit) :
            productDao.productListByProductTier(pageInt, limit);
          break;
        case "product_sold_out":
          list = ("desc".equals(orderBy)) ? productDao.productListDescByProductSoldOut(pageInt, limit) :
            productDao.productListByProductSoldOut(pageInt, limit);
          break;
        default:
          list = productDao.productListByProductType(pageInt, limit);
          break;
      }

      if(orderBy == "asc") {
        orderBy = "desc";
      } else if(orderBy == "desc") {
        orderBy = "asc";
      }

      productCount = productDao.productCount();
      model.addAttribute("list", list);
    }

    Map<String, Integer> paginationInfo = calculatePagination(pageInt, productCount);
    int start = paginationInfo.get("start");
    int end = paginationInfo.get("end");

    model.addAttribute("memberTier", memberTier);
    model.addAttribute("productCount", productCount);
    model.addAttribute("columnName", columnName);
    model.addAttribute("orderBy", orderBy);
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", start);
    model.addAttribute("end", end);

    return "admin/productList";
  }

  @RequestMapping("productSearch")
  public String productSearch(HttpServletRequest request, HttpSession session, Model model,
                              @RequestParam(defaultValue = "1") int pageInt,
                              @SessionAttribute int memberTier) throws Exception {

    int productCount = 0;
    String searchText = "";
    List<ProductDTO> list = null;
    String[] array = {"productCode", "productName", "productType", "productPrice",
                      "productUnit", "productCountry", "productSpecies", "productCompany", "productTier", "productSoldOut"};

    if(memberTier == 9) {
      for(String param : array) {
        String parameter = request.getParameter(param);
        if(parameter != null && !parameter.isEmpty()) {
          searchText = request.getParameter(param);
          model.addAttribute(param, searchText);
          productDao.rownumSet();
          switch(param) {
            case "productCode":
              list = productDao.productSearchListByProductCode(pageInt, limit, searchText);
              break;
            case "productName":
              list = productDao.productSearchListByProductName(pageInt, limit, searchText);
              break;
            case "productType":
              list = productDao.productSearchListByProductType(pageInt, limit, searchText);
              break;
            case "productPrice":
              list = productDao.productSearchListByProductPrice(pageInt, limit, searchText);
              break;
            case "productUnit":
              list = productDao.productSearchListByProductUnit(pageInt, limit, searchText);
              break;
            case "productCountry":
              list = productDao.productSearchListByProductCountry(pageInt, limit, searchText);
              break;
            case "productSpecies":
              list = productDao.productSearchListByProductSpecies(pageInt, limit, searchText);
              break;
            case "productCompany":
              list = productDao.productSearchListByProductCompany(pageInt, limit, searchText);
              break;
            case "productTier":
              list = productDao.productSearchListByProductTier(pageInt, limit, searchText);
              break;
            case "productSoldOut":
              list = productDao.productSearchListByProductSoldOut(pageInt, limit, searchText);
              break;
          }
        }
      }
      productCount = productDao.productCount();
    }

    Map<String, Integer> paginationInfo = calculatePagination(pageInt, productCount);
    int start = paginationInfo.get("start");
    int end = paginationInfo.get("end");

    model.addAttribute("memberTier", memberTier);
    model.addAttribute("productCount", productCount);
    model.addAttribute("list", list);
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", start);
    model.addAttribute("end", end);

    return "admin/productList";
  }

  @RequestMapping("productSoldOutUpdate")
  public String productSoldOutUpdate(HttpServletRequest request, HttpSession session, Model model,
                                     @RequestParam(defaultValue = "1") int pageInt,
                                     @SessionAttribute int memberTier) throws Exception {

    int productCount = 0;

    if(memberTier == 9) {
      productDao.rownumSet();
      List<ProductDTO> list = productDao.productList(pageInt, limit);
      productCount = productDao.productCount();
      model.addAttribute("list", list);
    }

    Map<String, Integer> paginationInfo = calculatePagination(pageInt, productCount);
    int start = paginationInfo.get("start");
    int end = paginationInfo.get("end");

    model.addAttribute("memberTier", memberTier);
    model.addAttribute("productCount", productCount);
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", start);
    model.addAttribute("end", end);

    return "admin/productSoldOutUpdate";
  }


  @RequestMapping("productSoldOutUpdatePro")
  public String productSoldOutUpdatePro(HttpServletRequest request, HttpSession session, Model model, String productCode, int productSoldOut) throws Exception {

    productDao.productSoldOutUpdate(productCode, productSoldOut);

    String url = "/admin/productSoldOutUpdate";
    String msg ="제품 품절 수정 성공";

    model.addAttribute("url", url);
    model.addAttribute("msg", msg);

    return "alert";
  }

  @RequestMapping("memberList")
  public String memberList(HttpServletRequest request, HttpSession session, Model model,
                           @RequestParam(defaultValue = "1") int pageInt,
                           @SessionAttribute int memberTier) throws Exception {

    int memberCount = 0;

    if(memberTier == 9) {
      memberDao.rownumSet();
      List<MemberDTO> list = memberDao.memberList(pageInt, limit);
      log.info(list.toString());
      memberCount = memberDao.memberCount();
      model.addAttribute("list", list);
      log.info(list.toString());
    }

    Map<String, Integer> paginationInfo = calculatePagination(pageInt, memberCount);
    int start = paginationInfo.get("start");
    int end = paginationInfo.get("end");

    model.addAttribute("memberTier", memberTier);
    model.addAttribute("memberCount", memberCount);
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    return "admin/memberList";
  }

  @RequestMapping("memberListPro")
  public String memberListPro(HttpServletRequest request, HttpSession session, Model model,
                              @RequestParam(defaultValue = "1") int pageInt,
                              @RequestParam String columnName,
                              @RequestParam String orderBy,
                              @SessionAttribute int memberTier) throws Exception {

    int memberCount = 0;

    if(memberTier == 9) {
      memberDao.rownumSet();
      List<MemberDTO> list;

      switch (columnName) {
        case "member_company_name":
          list = ("desc".equals(orderBy)) ? memberDao.memberListDescByMemberCompanyName(pageInt, limit) :
            memberDao.memberListByMemberCompanyName(pageInt, limit);
          break;
        case "member_fran_code":
          list = ("desc".equals(orderBy)) ? memberDao.memberListDescByMemberFranCode(pageInt, limit) :
            memberDao.memberListByMemberFranCode(pageInt, limit);
          break;
        case "member_id":
          list = ("desc".equals(orderBy)) ? memberDao.memberListDescByMemberId(pageInt, limit) :
            memberDao.memberListByMemberId(pageInt, limit);
          break;
        case "member_name":
          list = ("desc".equals(orderBy)) ? memberDao.memberListDescByMemberName(pageInt, limit) :
            memberDao.memberListByMemberName(pageInt, limit);
          break;
        case "member_tel":
          list = ("desc".equals(orderBy)) ? memberDao.memberListDescByMemberTel(pageInt, limit) :
            memberDao.memberListByMemberTel(pageInt, limit);
          break;
        case "member_company_tel":
          list = ("desc".equals(orderBy)) ? memberDao.memberListDescByMemberCompanyTel(pageInt, limit) :
            memberDao.memberListByMemberCompanyTel(pageInt, limit);
          break;
        case "member_tier":
          list = ("desc".equals(orderBy)) ? memberDao.memberListDescByMemberTier(pageInt, limit) :
            memberDao.memberListByMemberTier(pageInt, limit);
          break;
        case "member_date":
          list = ("desc".equals(orderBy)) ? memberDao.memberListDescByMemberDate(pageInt, limit) :
            memberDao.memberListByMemberDate(pageInt, limit);
          break;
        case "member_disable":
          list = ("desc".equals(orderBy)) ? memberDao.memberListDescByMemberDisable(pageInt, limit) :
            memberDao.memberListByMemberDisable(pageInt, limit);
          break;
        default:
          list = memberDao.memberListByMemberTier(pageInt, limit);
          break;
      }

      if(orderBy == "asc") {
        orderBy = "desc";
      } else if(orderBy == "desc") {
        orderBy = "asc";
      }

      memberCount = memberDao.memberCount();
      model.addAttribute("list", list);
    }

    Map<String, Integer> paginationInfo = calculatePagination(pageInt, memberCount);
    int start = paginationInfo.get("start");
    int end = paginationInfo.get("end");

    model.addAttribute("memberTier", memberTier);
    model.addAttribute("memberCount", memberCount);
    model.addAttribute("columnName", columnName);
    model.addAttribute("orderBy", orderBy);
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", start);
    model.addAttribute("end", end);

    return "admin/memberList";
  }

  @RequestMapping("memberUpdate")
  public String memberUpdate(Model model, String memberId) throws Exception {
    MemberDTO memberDTO = memberDao.memberSelectOne(memberId);

    model.addAttribute("member", memberDTO);

    return "admin/memberUpdate";
  }

  @RequestMapping("memberUpdatePro")
  public String memberUpdatePro(HttpSession session, Model model, MemberDTO memberDTO,
                                @SessionAttribute(name="memberId") String adminId) throws Exception {

    MemberDTO admin = memberDao.memberSelectOne(adminId);
    String adminName = admin.getMemberName();

    String url = "/admin/memberList";
    String msg = "회원 정보가 수정되었습니다.";

    String memberId = memberDTO.getMemberId();
    MemberDTO existMemberDTO = memberDao.memberSelectOne(memberId); // 업데이트 전 회원 정보
    String existMemberFranCode = existMemberDTO.getMemberFranCode(); // 업데이트 전 가맹점코드

    memberDTO.setMemberModifierName(adminName);
    memberDao.memberAdminUpdate(memberDTO);

    MemberDTO updatedMemberDTO = memberDao.memberSelectOne(memberId); // 업데이트 후 회원 정보
    String memberFranCode = updatedMemberDTO.getMemberFranCode(); // 업데이트 후 가맹점 코드

    if(!existMemberFranCode.equals(memberFranCode)) {
      historyDao.historyFranCodeUpdate(existMemberFranCode, memberFranCode); // 주문 기록에 있는 변경된 가맹점코드 전부 업데이트
    }

    model.addAttribute("url", url);
    model.addAttribute("msg", msg);

    return "alert";
  }

  @RequestMapping("memberSearch")
  public String memberSearch(HttpServletRequest request, HttpSession session, Model model,
                             @RequestParam(defaultValue = "1") int pageInt,
                             @SessionAttribute int memberTier) throws Exception {

    int memberCount = 0;
    String searchText = "";
    List<MemberDTO> list = null;
    String[] array = {"memberCompanyName", "memberFranCode", "memberId", "memberName",
      "memberTel", "memberCompanyTel", "memberTier"};

    if(memberTier == 9) {
      for(String param : array) {
        if(!request.getParameter(param).isEmpty()) {
          searchText = request.getParameter(param);
          model.addAttribute(param, searchText);
          memberDao.rownumSet();
          switch(param) {
            case "memberCompanyName":
              list = memberDao.memberSearchListByMemberCompanyName(pageInt, limit, searchText);
              break;
            case "memberFranCode":
              list = memberDao.memberSearchListByMemberFranCode(pageInt, limit, searchText);
              break;
            case "memberId":
              list = memberDao.memberSearchListByMemberId(pageInt, limit, searchText);
              break;
            case "memberName":
              list = memberDao.memberSearchListByMemberName(pageInt, limit, searchText);
              break;
            case "memberTel":
              list = memberDao.memberSearchListByMemberTel(pageInt, limit, searchText);
              break;
            case "memberCompanyTel":
              list = memberDao.memberSearchListByMemberCompanyTel(pageInt, limit, searchText);
              break;
            case "memberTier":
              list = memberDao.memberSearchListByMemberTier(pageInt, limit, searchText);
              break;
          }
        }
      }
      memberCount = memberDao.memberCount();
    }

    Map<String, Integer> paginationInfo = calculatePagination(pageInt, memberCount);
    int start = paginationInfo.get("start");
    int end = paginationInfo.get("end");

    model.addAttribute("memberTier", memberTier);
    model.addAttribute("memberCount", memberCount);
    model.addAttribute("list", list);
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    return "admin/memberList";
  }

  @RequestMapping("memberWithdrawalList")
  public String memberWithdrawalList(HttpServletRequest request, HttpSession session, Model model,
                                     @RequestParam(defaultValue = "1") int pageInt,
                                     @SessionAttribute int memberTier) throws Exception {

    int memberCount = 0;

    if(memberTier == 9) {
      memberDao.rownumSet();
      List<MemberDTO> list = memberDao.memberWithdrawalList(pageInt, limit);
      log.info(list.toString());
      memberCount = memberDao.memberWithdrawalCount();
      model.addAttribute("list", list);
      log.info(list.toString());
    }

    Map<String, Integer> paginationInfo = calculatePagination(pageInt, memberCount);
    int start = paginationInfo.get("start");
    int end = paginationInfo.get("end");

    model.addAttribute("memberTier", memberTier);
    model.addAttribute("memberCount", memberCount);
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    return "admin/memberWithdrawalList";
  }

  @RequestMapping("orderHistory")
  public String orderHistory(HttpServletRequest request, HttpSession session, Model model,
                             @RequestParam(defaultValue = "1") int pageInt,
                             @SessionAttribute int memberTier) throws Exception {

    int historyCount = 0;

    if(memberTier == 9) {
      historyDao.rownumSet();
      List<HistoryDTO> list = historyDao.historyList(pageInt, limit);
      historyCount = historyDao.historyCount();
      model.addAttribute("list", list);
    }

    Map<String, Integer> paginationInfo = calculatePagination(pageInt, historyCount);
    int start = paginationInfo.get("start");
    int end = paginationInfo.get("end");

    model.addAttribute("memberTier", memberTier);
    model.addAttribute("historyCount", historyCount);
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    return "admin/orderHistory";
  }

  @RequestMapping("orderHistoryPro")
  public String orderHistoryPro(HttpServletRequest request, HttpSession session, Model model,
                                @RequestParam(defaultValue = "1") int pageInt,
                                @RequestParam String columnName,
                                @RequestParam String orderBy,
                                @SessionAttribute int memberTier) throws Exception {

    int historyCount = 0;

    if(memberTier == 9) {
      historyDao.rownumSet();
      List<HistoryDTO> list;

      switch (columnName) {
        case "history_code":
          list = ("desc".equals(orderBy)) ? historyDao.historyListDescByHistoryCode(pageInt, limit) :
            historyDao.historyListByHistoryCode(pageInt, limit);
          break;
        case "member_id":
          list = ("desc".equals(orderBy)) ? historyDao.historyListDescByMemberId(pageInt, limit) :
            historyDao.historyListByMemberId(pageInt, limit);
          break;
        case "product_code":
          list = ("desc".equals(orderBy)) ? historyDao.historyListDescByProductCode(pageInt, limit) :
            historyDao.historyListByProductCode(pageInt, limit);
          break;
        case "product_name":
          list = ("desc".equals(orderBy)) ? historyDao.historyListDescByProductName(pageInt, limit) :
            historyDao.historyListByProductName(pageInt, limit);
          break;
        case "product_unit":
          list = ("desc".equals(orderBy)) ? historyDao.historyListDescByProductUnit(pageInt, limit) :
            historyDao.historyListByProductUnit(pageInt, limit);
          break;
        case "product_price":
          list = ("desc".equals(orderBy)) ? historyDao.historyListDescByProductPrice(pageInt, limit) :
            historyDao.historyListByProductPrice(pageInt, limit);
          break;
        case "quantity":
          list = ("desc".equals(orderBy)) ? historyDao.historyListDescByQuantity(pageInt, limit) :
            historyDao.historyListByQuantity(pageInt, limit);
          break;
        case "deliveryAddress":
          list = ("desc".equals(orderBy)) ? historyDao.historyListDescByDeliveryAddress(pageInt, limit) :
            historyDao.historyListByDeliveryAddress(pageInt, limit);
          break;
        case "order_date":
          list = ("desc".equals(orderBy)) ? historyDao.historyListDescByOrderDate(pageInt, limit) :
            historyDao.historyListByOrderDate(pageInt, limit);
          break;
        default:
          list = historyDao.historyList(pageInt, limit);
          break;
      }

      if(orderBy == "asc") {
        orderBy = "desc";
      } else if(orderBy == "desc") {
        orderBy = "asc";
      }

      historyCount = historyDao.historyCount();
      model.addAttribute("list", list);
    }

    Map<String, Integer> paginationInfo = calculatePagination(pageInt, historyCount);
    int start = paginationInfo.get("start");
    int end = paginationInfo.get("end");

    model.addAttribute("memberTier", memberTier);
    model.addAttribute("historyCount", historyCount);
    model.addAttribute("columnName", columnName);
    model.addAttribute("orderBy", orderBy);
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    model.addAttribute("pageInt", pageInt);

    return "admin/orderHistory";
  }

  @RequestMapping("orderHistoryUpdate")
  public String orderHistoryUpdate(HttpServletRequest request, HttpSession session, Model model, String orderId, String productCode) throws Exception {
    HistoryDTO historyDTO = historyDao.historySelectOne(orderId, productCode);

    model.addAttribute("history", historyDTO);

    return "admin/orderHistoryUpdate";
  }

  @RequestMapping("orderHistoryUpdatePro")
  public String orderHistoryUpdatePro(HttpServletRequest request, HttpSession session, Model model, HistoryDTO historyDTO,
                                      @SessionAttribute(name="memberId") String adminId) throws Exception {

    MemberDTO admin = memberDao.memberSelectOne(adminId);
    String adminName = admin.getMemberName();

    String msg = "주문 기록이 수정되었습니다.";
    String url = "/admin/orderHistory";

    historyDTO.setHistoryModifierName(adminName);
    int num = historyDao.historyUpdate(historyDTO);

    if(num == 0) {
      msg = "주문 기록 수정에 실패했습니다.";
      url = "/admin/orderHistory";
    }

    model.addAttribute("msg", msg);
    model.addAttribute("url", url);

    return "alert";
  }

  @RequestMapping("orderHistoryDelete")
  public String orderHistoryDelete(HttpServletRequest request, HttpSession session, Model model, String orderId, String productCode) throws Exception {

    model.addAttribute("orderId", orderId);
    model.addAttribute("productCode", productCode);

    return "/admin/orderHistoryDelete";
  }

  @RequestMapping("orderHistoryDeletePro")
  public String orderHistoryDeletePro(HttpServletRequest request, Model model, String orderId, String productCode, String confirmDelete) throws Exception {

    String msg = "주문 기록 삭제에 성공했습니다.";
    String url = "/admin/orderHistory";

    if(orderId.equals(confirmDelete)) {
      int num = historyDao.historyDelete(orderId, productCode);

      if(num == 0) {
        msg = "주문 기록 삭제에 실패했습니다.";
        url = "/admin/orderHistory";
      }
    } else {
      msg = "주문번호와 일치하지 않습니다.";
      url = "/admin/orderHistory";
    }

    model.addAttribute("msg", msg);
    model.addAttribute("url", url);

    return "alert";
  }

  @RequestMapping("historySearch")
  public String historySearch(HttpServletRequest request, HttpSession session, Model model,
                              @RequestParam(defaultValue = "1") int pageInt,
                              @SessionAttribute int memberTier) throws Exception {

    int historyCount = 0;
    List<HistoryDTO> list = null;
    String searchText = "";
    String searchText1 = "";
    String[] array = {"historyCode", "memberId"};
    List<String> arrayList = new ArrayList<String>();
    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");

    if(memberTier == 9) {
      historyDao.rownumSet();
      historyCount = historyDao.historyCount();

      for(String param : array) {
        String parameter = request.getParameter(param);
        if(parameter != null && !parameter.isEmpty()) {
          arrayList.add(param);
        }
      }

      if(arrayList.size() == 2) {
        searchText = request.getParameter(arrayList.get(0));
        searchText1 = request.getParameter(arrayList.get(1));
        if(!startDate.isEmpty() && !endDate.isEmpty()) {
          list = historyDao.historySearchListByHistoryCodeAndMemberIdWithOrderDate(pageInt, limit, searchText, searchText1 ,startDate, endDate);
          model.addAttribute("startDate", startDate);
          model.addAttribute("endDate", endDate);
          model.addAttribute("historyCode", searchText);
          model.addAttribute("memberId", searchText1);
        } else {
          list = historyDao.historySearchListByHistoryCodeAndMemberId(pageInt, limit, searchText, searchText1);
          model.addAttribute("historyCode", searchText);
          model.addAttribute("memberId", searchText1);
        }
      } else if (arrayList.size() == 1) {
        searchText = request.getParameter(arrayList.get(0));
        if(arrayList.get(0) == "historyCode") {
          if(!startDate.isEmpty() && !endDate.isEmpty()) {
            list = historyDao.historySearchListByHistoryCodeWithOrderDate(pageInt, limit, searchText, startDate, endDate);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("historyCode", searchText);
          } else {
            list = historyDao.historySearchListByHistoryCode(pageInt, limit, searchText);
            model.addAttribute("historyCode", searchText);
          }
        } else if(arrayList.get(0) == "memberId") {
          if(!startDate.isEmpty() && !endDate.isEmpty()) {
            list = historyDao.historySearchListByMemberIdWithOrderDate(pageInt, limit, searchText, startDate, endDate);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("memberId", searchText);
          } else {
            list = historyDao.historySearchListByMemberId(pageInt, limit, searchText);
            model.addAttribute("memberId", searchText);
          }
        }
      } else if(!startDate.isEmpty() && !endDate.isEmpty()) {
        list = historyDao.historySearchListByOrderDate(pageInt, limit, startDate, endDate);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
      }
    }

    Map<String, Integer> paginationInfo = calculatePagination(pageInt, historyCount);
    int start = paginationInfo.get("start");
    int end = paginationInfo.get("end");

    model.addAttribute("memberTier", memberTier);
    model.addAttribute("historyCount", historyCount);
    model.addAttribute("list", list);
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    return "admin/orderHistory";
  }

  @RequestMapping("memberTierUpdate")
  public String memberTierUpdate(HttpServletRequest request, HttpSession session, Model model,
                                 @SessionAttribute int memberTier) throws Exception {

    List<MemberDTO> list;

    if(memberTier == 9) {
      list = memberDao.memberSearchListByMemberTier(1, 32, "0");
      model.addAttribute("list", list);
    }

    Integer memberCount = memberDao.memberCount();

    model.addAttribute("memberCount", memberCount);
    model.addAttribute("memberTier", memberTier);

    return "admin/memberTierUpdate";
  }

  @RequestMapping("memberTierUpdatePro")
  public String memberTierUpdatePro(HttpServletRequest request, Model model, String memberId, int memberTier) throws Exception {

    log.info(memberId, memberTier);

    memberDao.memberTierUpdate(memberId, memberTier);

    String url = "/admin/memberTierUpdate";
    String msg ="멤버 등급 수정 성공";

    model.addAttribute("url", url);
    model.addAttribute("msg", msg);

    return "alert";
  }
//
//  @RequestMapping("memberFranCodeUpdate")
//  public String memberFranCodeUpdate() throws Exception {
//    Integer memberTier = (Integer) session.getAttribute("memberTier");
//    if(memberTier == null) {
//      memberTier = 0;
//    }
//
//    List<Member> list;
//
//    if(memberTier == 9) {
//      list = memberDao.memberSearchListByMemberFranCodeByNull(1, 32);
//      model.addAttribute()("list", list);
//    }
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

  @RequestMapping("memberDisableUpdate")
  public String memberDisableUpdate(HttpServletRequest request, HttpSession session, Model model,
                                    @SessionAttribute int memberTier) throws Exception {

    List<MemberDTO> list;

    if(memberTier == 9) {
      list = memberDao.memberSearchListByMemberDisable(1, 32);
      model.addAttribute("list", list);
    }

    model.addAttribute("memberTier", memberTier);

    return "admin/memberDisable";
  }

  @RequestMapping("memberDisableUpdatePro")
  public String memberDisableUpdatePro(HttpServletRequest request, Model model, String memberId) throws Exception {
    MemberDTO memberDTO = memberDao.memberSelectOne(memberId);
    MemberDTO disabledMemberDTO = memberDao.memberDisableSelectOne(memberId);

    int checkMember = memberDao.checkMember(memberId);
    int checkDisabledMember = memberDao.checkDisabledMember(memberId);

    log.info("memberID : {}", memberId);
    System.out.println(checkMember + "checkMember");
    System.out.println(checkDisabledMember + "checkDisabledMember");

    if(checkMember > 0) {
      memberDao.memberDisable(memberId);
      cartDao.deleteCartByMember(memberId);
      memberDao.memberDelete(memberId);
      memberDao.updateDisabledDate(memberId);
    } else if(checkDisabledMember > 0) {
      memberDao.memberEnable(memberId);
      memberDao.disabledMemberDelete(memberId);
      memberDao.updateDisabledDateToNull(memberId);
    }

    String url = "/admin/memberDisableUpdate";
    String msg = "멤버 비활성화 상태 수정 성공";

    model.addAttribute("url", url);
    model.addAttribute("msg", msg);

    return "alert";
  }

  @RequestMapping("productUpload")
  public String productUpload() {
    return "admin/productUpload";
  }

  @RequestMapping("productUploadPro")
  public String productUploadPro(HttpServletRequest request, HttpSession session, Model model, MultipartHttpServletRequest files, ProductDTO productDTO,
                                 @SessionAttribute(name="memberId") String adminId) throws Exception {

    MemberDTO admin = memberDao.memberSelectOne(adminId);
    String adminName = admin.getMemberName();

    String msg = "제품 등록에 실패하였습니다.";
    String url = "/admin/productUpload";

    ImageDTO imageDTO = new ImageDTO();
    String productCode = productDTO.getProductCode();
    int productType = productDTO.getProductType();

    String filePath = request.getServletContext().getRealPath("/") + "view/files/bean/" + productCode;

    if(productType == 1) {
      filePath = request.getServletContext().getRealPath("/") + "view/files/mix/" + productCode;
    }

    if(productType == 2) {
      filePath = request.getServletContext().getRealPath("/") + "view/files/cafe/" + productCode;
    }

    String fileName;
    File uploadPath = new File(filePath);

    if (!uploadPath.exists()) {
      uploadPath.mkdirs(); // 경로가 없으면 생성
    }

    List<MultipartFile> fileList = files.getFiles("files");

    ProductDTO check = productDao.productSelectOne(productCode);

    if (check == null) {
      if (fileList.size() > 0) {
        for(int i = 0; i < fileList.size(); i++) {
          fileName = fileList.get(i).getOriginalFilename();
          File file = new File(filePath, fileName);

          imageDTO.setProductCode(productCode);
          imageDTO.setFileName(fileName);
          imageDTO.setFileRegisterName(adminName);
          imageDao.insertProductImage(imageDTO);

          if(fileName.contains("thumbnail")) {
            productDTO.setProductFile(fileName);
          }

          try {
            fileList.get(i).transferTo(file);
          } catch (IllegalStateException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }

        productDTO.setProductRegisterName(adminName);
        productDao.productInsert(productDTO);

        if(productType == 0) {
          productDTO.setBeanRegisterName(adminName);
          productDao.beanInsert(productDTO);
        }

        if(productType == 1) {
          productDTO.setMixRegisterName(adminName);
          productDao.mixInsert(productDTO);
        }

//        if(productType == 2) { // cafe 제품에 필요한 데이터 컬럼은 product 테이블에 전부 있어서 비활성화
//          product.setCafeRegisterName(product.getProductRegisterName());
//          productDao.cafeInsert(product);
//        }

        msg = "제품 등록에 성공하였습니다.";
      } else {
        msg = "업로드 된 파일이 없습니다.";
      }
    } else {
      msg = "이미 존재하는 제품입니다.";
    }

    model.addAttribute("msg", msg);
    model.addAttribute("url", url);

    return "alert";
  }


  @RequestMapping("productUpdate")
  public String productUpdate(HttpServletRequest request, Model model, @RequestParam String productCode) {

    ProductDTO productDTO = productDao.productSelectOne(productCode);
    int productType = productDTO.getProductType();

    if(productType == 0) { // bean (cafe는 테이블이 없음)
      ProductDTO bean = productDao.beanSelectOne(productCode);
      model.addAttribute("bean", bean);
    }
    if(productType == 1) { // mix
      ProductDTO mix = productDao.mixSelectOne(productCode);
      model.addAttribute("mix", mix);
    }

    productDTO.setExistProductCode(productCode);

    model.addAttribute("product", productDTO);

    return "admin/productUpdate";
  }

  @RequestMapping("productUpdatePro")
  public String productUpdatePro(HttpServletRequest request, HttpSession session, Model model, MultipartHttpServletRequest files, ProductDTO productDTO,
                                 @SessionAttribute(name="memberId") String adminId) throws Exception {

    MemberDTO admin = memberDao.memberSelectOne(adminId);
    String adminName = admin.getMemberName();

    String msg = "제품 수정에 실패하였습니다.";
    String url = "/admin/productList";

    ImageDTO imageDTO = new ImageDTO();
    String productCode = productDTO.getProductCode();
    int productType = productDTO.getProductType();

    String filePath = request.getServletContext().getRealPath("/") + "view/files/bean/" + productCode;

    if(productType == 1) {
      filePath = request.getServletContext().getRealPath("/") + "view/files/mix/" + productCode;
    }

    if(productType == 2) {
      filePath = request.getServletContext().getRealPath("/") + "view/files/cafe/" + productCode;
    }

    String fileName;
    File uploadPath = new File(filePath);

    if (!uploadPath.exists()) {
      uploadPath.mkdirs(); // 경로가 없으면 생성
    }

    List<MultipartFile> fileList = files.getFiles("files");

    ProductDTO check = productDao.productSelectOne(productCode);
    productDTO.setProductFile(check.getProductFile());

    if (check != null) {
      if (fileList.size() > 0) {
        for(int i = 0; i < fileList.size(); i++) {
          fileName = fileList.get(i).getOriginalFilename();
          File file = new File(filePath, fileName);

          if(fileName.contains("thumbnail")) {
            productDTO.setProductFile(fileName);
          }

          if(!fileName.isEmpty()) {
            imageDTO.setProductCode(productCode);
            imageDTO.setFileName(fileName);
            imageDTO.setFileModifierName(adminName);

            if(file.exists() && file.delete()) {
              System.out.println("File is deleted.");
            }

            imageDao.updateProductImage(imageDTO);
            fileList.get(i).transferTo(file);
          }
        }

        productDTO.setProductModifierName(adminName);
        productDao.productUpdate(productDTO);

        if(productType == 0) {
          productDTO.setBeanModifierName(adminName);
          productDao.beanUpdate(productDTO);
        }

        if(productType == 1) {
          productDTO.setMixModifierName(adminName);
          productDao.mixUpdate(productDTO);
        }

//        if(productType == 2) { // cafe 제품에 필요한 데이터 컬럼은 product 테이블에 전부 있어서 비활성화
//          product.setCafeRegisterName(product.getProductRegisterName());
//          productDao.cafeInsert(product);
//        }

        msg = "제품 수정에 성공하였습니다.";
      } else {
        msg = "업로드 된 파일이 없습니다.";
      }
    } else {
      msg = "제품이 존재하지 않습니다.";
    }

    model.addAttribute("msg", msg);
    model.addAttribute("url", url);

    return "alert";
  }

  @RequestMapping("productDelete")
  public String productDelete(HttpServletRequest request, HttpSession session, Model model,
                              @RequestParam(defaultValue = "1") int pageInt,
                              @SessionAttribute int memberTier) throws Exception {

    int productCount = 0;

    if(memberTier == 9) {
      productDao.rownumSet();
      List<ProductDTO> list = productDao.productList(pageInt, limit);
      productCount = productDao.productCount();
      model.addAttribute("list", list);
    }

    Map<String, Integer> paginationInfo = calculatePagination(pageInt, productCount);
    int start = paginationInfo.get("start");
    int end = paginationInfo.get("end");

    model.addAttribute("memberTier", memberTier);
    model.addAttribute("productCount", productCount);
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", start);
    model.addAttribute("end", end);
    return "admin/productDelete";
  }

  @RequestMapping("productDeletePro")
  public String productDeletePro(HttpServletRequest request, HttpSession session, Model model, String productCode,
                                 @SessionAttribute int memberTier) throws Exception {

    String msg = "제품 삭제에 실패했습니다.";
    String url = "/admin/productDelete";

    String productType = productDao.productTypeFindByProductCode(productCode);

    if(memberTier == 9) {
      switch (productType) {
        case "0" -> {
          productDao.beanDelete(productCode);
          productDao.productDelete(productCode);
          msg = "제품 삭제에 성공했습니다.";
        }
        case "1" -> {
          productDao.mixDelete(productCode);
          productDao.productDelete(productCode);
          msg = "제품 삭제에 성공했습니다.";
        }
        case "2" -> {
          productDao.cafeDelete(productCode);
          productDao.productDelete(productCode);
          msg = "제품 삭제에 성공했습니다.";
        }
      }
    }

    model.addAttribute("msg", msg);
    model.addAttribute("url", url);

    return "alert";
  }

  @RequestMapping("excelProductDownload")
  public void excelProductDownload(HttpServletResponse response) throws IOException {
    List<ProductDTO> productDTOList = productDao.productListAll();

    String excelFileName = "productList.xlsx";

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment; filename=" + excelFileName);

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
    for (ProductDTO productDTO : productDTOList) {
      Row row = sheet.createRow(rowNum++);
      int colNum = 0;
      row.createCell(colNum++).setCellValue(productDTO.getProductCode());
      row.createCell(colNum++).setCellValue(productDTO.getProductType());
      row.createCell(colNum++).setCellValue(productDTO.getProductName());
      row.createCell(colNum++).setCellValue(productDTO.getProductPrice());
      row.createCell(colNum++).setCellValue(productDTO.getProductUnit());
      row.createCell(colNum++).setCellValue(productDTO.getProductTier());
      row.createCell(colNum++).setCellValue(productDTO.getProductSoldOut());
      row.createCell(colNum++).setCellValue(productDTO.getProductRegisterName());
      row.createCell(colNum++).setCellValue(productDTO.getProductRegisterDate());
      row.createCell(colNum++).setCellValue(productDTO.getProductModifierName());
      row.createCell(colNum++).setCellValue(productDTO.getProductModifierDate());
    }

    ServletOutputStream outputStream = response.getOutputStream();
    workbook.write(outputStream);
    workbook.close();
    outputStream.close();
  }

  @RequestMapping("excelMemberDownload")
  public void excelMemberDownload(HttpServletResponse response) throws IOException {
    List<MemberDTO> memberDTOList = memberDao.memberListAll();

    String excelFileName = "memberList.xlsx";

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment; filename=" + excelFileName);

    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("회원 리스트");

    Row headerRow = sheet.createRow(0);
    headerRow.createCell(0).setCellValue("등급");
    headerRow.createCell(1).setCellValue("아이디");
    headerRow.createCell(2).setCellValue("이름");
    headerRow.createCell(3).setCellValue("회사명");
    headerRow.createCell(4).setCellValue("비밀번호");
    headerRow.createCell(5).setCellValue("전화번호");
    headerRow.createCell(6).setCellValue("회사번호");
    headerRow.createCell(7).setCellValue("주소");
    headerRow.createCell(8).setCellValue("배송지");
    headerRow.createCell(9).setCellValue("이메일");
    headerRow.createCell(10).setCellValue("가맹점코드");
    headerRow.createCell(11).setCellValue("가입일");
    headerRow.createCell(12).setCellValue("비활성화일");
    headerRow.createCell(13).setCellValue("수정자");
    headerRow.createCell(14).setCellValue("수정일");

    int rowNum = 1;
    for (MemberDTO memberDTO : memberDTOList) {
      Row row = sheet.createRow(rowNum++);
      int colNum = 0;
      row.createCell(colNum++).setCellValue(memberDTO.getMemberTier());
      row.createCell(colNum++).setCellValue(memberDTO.getMemberId());
      row.createCell(colNum++).setCellValue(memberDTO.getMemberName());
      row.createCell(colNum++).setCellValue(memberDTO.getMemberCompanyName());
      row.createCell(colNum++).setCellValue(memberDTO.getMemberPassword());
      row.createCell(colNum++).setCellValue(memberDTO.getMemberTel());
      row.createCell(colNum++).setCellValue(memberDTO.getMemberCompanyTel());
      row.createCell(colNum++).setCellValue(memberDTO.getMemberAddress() + " " + memberDTO.getMemberDetailAddress());
      row.createCell(colNum++).setCellValue(memberDTO.getMemberDeliveryAddress() + " " + memberDTO.getMemberDetailDeliveryAddress());
      row.createCell(colNum++).setCellValue(memberDTO.getMemberEmail());
      row.createCell(colNum++).setCellValue(memberDTO.getMemberFranCode());
      row.createCell(colNum++).setCellValue(memberDTO.getMemberDate());
      row.createCell(colNum++).setCellValue(memberDTO.getMemberDisableDate());
      row.createCell(colNum++).setCellValue(memberDTO.getMemberModifierName());
      row.createCell(colNum++).setCellValue(memberDTO.getMemberModifierDate());
    }

    ServletOutputStream outputStream = response.getOutputStream();
    workbook.write(outputStream);
    workbook.close();
    outputStream.close();
  }

  @RequestMapping("excelHistoryDownload")
  public void excelHistoryDownload(HttpServletResponse response) throws IOException {
    List<HistoryDTO> historyDTOList = historyDao.historyListAll();

    String excelFileName = "historyList.xlsx";

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment; filename=" + excelFileName);

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
    for (HistoryDTO historyDTO : historyDTOList) {
      Row row = sheet.createRow(rowNum++);
      int colNum = 0;
      row.createCell(colNum++).setCellValue(historyDTO.getOrderId());
      row.createCell(colNum++).setCellValue(historyDTO.getMemberTier());
      row.createCell(colNum++).setCellValue(historyDTO.getMemberId());
      row.createCell(colNum++).setCellValue(historyDTO.getMemberName());
      row.createCell(colNum++).setCellValue(historyDTO.getMemberFranCode());
      row.createCell(colNum++).setCellValue(historyDTO.getProductCode());
      row.createCell(colNum++).setCellValue(historyDTO.getProductName());
      row.createCell(colNum++).setCellValue(historyDTO.getQuantity());
      row.createCell(colNum++).setCellValue(historyDTO.getProductUnit());
      row.createCell(colNum++).setCellValue(historyDTO.getProductPrice());
      row.createCell(colNum++).setCellValue(historyDTO.getTotalPrice());
      row.createCell(colNum++).setCellValue(historyDTO.getOrderDate());
      row.createCell(colNum++).setCellValue(historyDTO.getDeliveryAddress() + " " + historyDTO.getDetailDeliveryAddress());
    }

    ServletOutputStream outputStream = response.getOutputStream();
    workbook.write(outputStream);
    workbook.close();
    outputStream.close();
  }

}
