package org.daCoffee.controller.api;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.daCoffee.dto.ApiResponseDTO;
import org.daCoffee.dto.request.admin.MemberRequestDTO;
import org.daCoffee.dto.request.admin.OrderHistoryRequestDTO;
import org.daCoffee.dto.request.admin.ProductRequestDTO;
import org.daCoffee.dto.response.BeanDataDTO;
import org.daCoffee.dto.response.MixDataDTO;
import org.daCoffee.entity.*;
import org.daCoffee.exception.NotFoundException;
import org.daCoffee.jwt.JwtUserDetails;
import org.daCoffee.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminApiController {

    @Value("${FILE_UPLOAD_PATH}")
    private String fileUploadPath;

    private final MemberService memberService;
    private final ProductService productService;
    private final OrderHistoryService orderHistoryService;
    private final ProductImageService productImageService;

    private static final int LIMIT = 20;

    @GetMapping("/me")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> getAdminMe(
            @AuthenticationPrincipal JwtUserDetails userDetails) {
        String adminId = userDetails.getMemberId();
        int memberTier = userDetails.getMemberTier();

        if (adminId == null || memberTier != 9) {
            return ResponseEntity.status(401)
                    .body(ApiResponseDTO.error("관리자 권한이 필요합니다.", 401));
        }

        return ResponseEntity.ok(ApiResponseDTO.success(Map.of(
                "adminId", adminId,
                "memberTier", memberTier
        )));
    }

    @GetMapping("/members")
    public ApiResponseDTO<Map<String, Object>> getMemberList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "member_tier") String columnName,
            @RequestParam(defaultValue = "asc") String orderBy) {

        Page<Member> result = memberService.findAllPaged(page, LIMIT, columnName, orderBy);

        return ApiResponseDTO.success(Map.of(
                "list", result.getContent(),
                "totalCount", result.getTotalElements(),
                "totalPages", result.getTotalPages(),
                "page", page
        ));
    }

    @GetMapping("/members/search")
    public ApiResponseDTO<Map<String, Object>> searchMembers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String memberCompanyName,
            @RequestParam(required = false) String memberFranCode,
            @RequestParam(required = false) String memberId,
            @RequestParam(required = false) String memberName,
            @RequestParam(required = false) String memberTel,
            @RequestParam(required = false) String memberCompanyTel,
            @RequestParam(required = false) String memberTier) {

        Page<Member> result = memberService.searchMembers(memberCompanyName, memberFranCode, memberId,
                memberName, memberTel, memberCompanyTel, memberTier, page, LIMIT);

        return ApiResponseDTO.success(Map.of(
                "list", result.getContent(),
                "totalCount", result.getTotalElements(),
                "totalPages", result.getTotalPages(),
                "page", page
        ));
    }

    @GetMapping("/members/{memberId}")
    public ApiResponseDTO<Member> getMember(@PathVariable String memberId) {
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new NotFoundException("회원 없음: " + memberId));
        return ApiResponseDTO.success(member);
    }

    @PutMapping("/members/{memberId}")
    public ApiResponseDTO<Void> updateMember(
            @RequestBody MemberRequestDTO dto,
            @AuthenticationPrincipal JwtUserDetails userDetails) {

        String adminId = userDetails.getMemberId();

        String adminName = memberService.findById(adminId)
                .map(Member::getMemberName)
                .orElse("admin");

        memberService.adminUpdate(adminName, dto);
        return ApiResponseDTO.success(null);
    }

    @PatchMapping("/members/{memberId}/tier")
    public ApiResponseDTO<Void> updateMemberTier(
            @PathVariable String memberId,
            @RequestParam int memberTier) {

        memberService.updateMemberTier(memberId, memberTier);
        return ApiResponseDTO.success(null);
    }

    @PatchMapping("/members/{memberId}/disable")
    public ApiResponseDTO<Void> toggleDisable(@PathVariable String memberId) {
        memberService.toggleDisable(memberId);
        return ApiResponseDTO.success(null);
    }

    // 회원 승인 대기 목록 (tier == 0)
    @GetMapping("/members/pending")
    public ApiResponseDTO<Map<String, Object>> getPendingMembers() {
        Page<Member> result = memberService.searchMembers(null, null, null, null, null, null, "0", 1, 100);
        return ApiResponseDTO.success(Map.of(
                "list", result.getContent(),
                "totalCount", result.getTotalElements()
        ));
    }

    // 탈퇴 회원
    @GetMapping("/members/withdrawal")
    public ApiResponseDTO<Map<String, Object>> getWithdrawalMembers(
            @RequestParam(defaultValue = "1") int page) {

        Page<MemberWithdrawal> result = memberService.findWithdrawalMembers(page, LIMIT);
        return ApiResponseDTO.success(Map.of(
                "list", result.getContent(),
                "totalCount", result.getTotalElements(),
                "totalPages", result.getTotalPages(),
                "page", page
        ));
    }

    // 비활성화 회원
    @GetMapping("/members/disabled")
    public ApiResponseDTO<Map<String, Object>> getDisabledMembers() {
        Page<Member> result = memberService.findDisabledMembers(1, 100);
        return ApiResponseDTO.success(Map.of(
                "list", result.getContent(),
                "totalCount", result.getTotalElements()
        ));
    }

    @GetMapping("/products")
    public ApiResponseDTO<Map<String, Object>> getProductList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "product_code") String columnName,
            @RequestParam(defaultValue = "asc") String orderBy) {

        Page<Product> result = productService.findAllPaged(page, LIMIT, columnName, orderBy);
        return ApiResponseDTO.success(Map.of(
                "list", result.getContent(),
                "totalCount", result.getTotalElements(),
                "totalPages", result.getTotalPages(),
                "page", page
        ));
    }

    @GetMapping("/products/search")
    public ApiResponseDTO<Map<String, Object>> searchProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String productType,
            @RequestParam(required = false) String productPrice,
            @RequestParam(required = false) String productUnit,
            @RequestParam(required = false) String productTier,
            @RequestParam(required = false) String productSoldOut) {

        Page<Product> result = productService.searchProducts(productCode, productName, productType,
                productPrice, productUnit, productTier, productSoldOut, page, LIMIT);
        return ApiResponseDTO.success(Map.of(
                "list", result.getContent(),
                "totalCount", result.getTotalElements(),
                "totalPages", result.getTotalPages(),
                "page", page
        ));
    }

    @GetMapping("/products/{productCode}")
    public ApiResponseDTO<Map<String, Object>> getProduct(@PathVariable String productCode) {
        Product product = productService.findById(productCode)
                .orElseThrow(() -> new NotFoundException("제품이 존재하지 않습니다."));

        Map<String, Object> data = new HashMap<>();
        data.put("productCode", product.getProductCode());
        data.put("productType", product.getProductType());
        data.put("productName", product.getProductName());
        data.put("productPrice", product.getProductPrice());
        data.put("productUnit", product.getProductUnit());
        data.put("productTier", product.getProductTier());
        data.put("productSoldOut", product.isProductSoldOut());

        if (product.getProductType() == 0) {
            productService.findBeanById(productCode).ifPresent(bean -> data.put("bean", BeanDataDTO.builder()
                    .beanSpecies(bean.getBeanSpecies())
                    .beanCompany(bean.getBeanCompany())
                    .beanUseByDate(bean.getBeanUseByDate())
                    .beanCountry(bean.getBeanCountry())
                    .build()));
        } else if (product.getProductType() == 1) {
            productService.findMixById(productCode).ifPresent(mix -> data.put("mix", MixDataDTO.builder()
                    .mixCompany(mix.getMixCompany())
                    .mixUseByDate(mix.getMixUseByDate())
                    .build()));
        }

        return ApiResponseDTO.success(data);
    }

    @PostMapping("/products")
    public ResponseEntity<ApiResponseDTO<Void>> uploadProduct(
            @ModelAttribute ProductRequestDTO dto,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal JwtUserDetails userDetails) throws IOException {

        String adminId = userDetails.getMemberId();

        if (productService.findById(dto.getProductCode()).isPresent()) {
            ApiResponseDTO<Void> response = ApiResponseDTO.error("이미 존재하는 제품입니다.");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }

        if (files == null || files.isEmpty() || files.stream().allMatch(MultipartFile::isEmpty)) {
            ApiResponseDTO<Void> response = ApiResponseDTO.error("업로드 된 파일이 없습니다.");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }

        String adminName = memberService.findById(adminId)
                .map(Member::getMemberName)
                .orElse("admin");

        String typeFolder = switch (dto.getProductType()) {
            case 1 -> "mix";
            case 2 -> "cafe";
            default -> "bean";
        };

        String filePath = fileUploadPath + "/" + typeFolder + "/" + dto.getProductCode();

        File uploadPath = new File(filePath);
        if (!uploadPath.exists()) {
            boolean created = uploadPath.mkdirs();
            if (!created) log.error("디렉토리 생성 실패: {}", filePath);
        }

        String thumbnailFileName = null;

        for (MultipartFile file : files) {
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

        // dto.setProductFile() 대신 thumbnailFileName을 builder에 직접 전달
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

        for (MultipartFile file : files) {
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

        return ResponseEntity.ok(ApiResponseDTO.success(null));
    }

    @DeleteMapping("/products/{productCode}")
    public ApiResponseDTO<Void> deleteProduct(@PathVariable String productCode) {
        productService.deleteProduct(productCode);
        return ApiResponseDTO.success(null);
    }

    @PatchMapping("/products/{productCode}/soldout")
    public ApiResponseDTO<Void> updateSoldOut(
            @PathVariable String productCode,
            @RequestParam boolean productSoldOut) {

        productService.updateSoldOut(productCode, productSoldOut);
        return ApiResponseDTO.success(null);
    }

    @GetMapping("/orders")
    public ApiResponseDTO<Map<String, Object>> getOrderHistory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "order_num") String columnName,
            @RequestParam(defaultValue = "desc") String orderBy) {

        Page<OrderHistory> result = orderHistoryService.findAllPaged(page, LIMIT, columnName, orderBy);
        return ApiResponseDTO.success(Map.of(
                "list", result.getContent(),
                "totalCount", result.getTotalElements(),
                "totalPages", result.getTotalPages(),
                "page", page
        ));
    }

    @GetMapping("/orders/search")
    public ApiResponseDTO<Map<String, Object>> searchOrders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String memberId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        LocalDateTime start = (startDate != null && !startDate.isEmpty())
                ? LocalDateTime.parse(startDate + "T00:00:00") : null;
        LocalDateTime end = (endDate != null && !endDate.isEmpty())
                ? LocalDateTime.parse(endDate + "T23:59:59") : null;

        Page<OrderHistory> result = orderHistoryService.searchOrders(
                orderId, memberId, start, end, page, LIMIT);

        return ApiResponseDTO.success(Map.of(
                "list", result.getContent(),
                "totalCount", result.getTotalElements(),
                "totalPages", result.getTotalPages(),
                "page", page
        ));
    }

    @GetMapping("/orders/{orderId}")
    public ApiResponseDTO<OrderHistory> getOrderHistory(
            @PathVariable String orderId,
            @RequestParam String productCode) {

        OrderHistory history = orderHistoryService.findByOrderIdAndProductCode(orderId, productCode)
                .orElseThrow(() -> new NotFoundException("주문 없음"));
        return ApiResponseDTO.success(history);
    }

    @PutMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponseDTO<Void>> updateOrderHistory(
            @RequestBody OrderHistoryRequestDTO dto,
            @PathVariable String orderId,
            @AuthenticationPrincipal JwtUserDetails userDetails) {

        String adminId = userDetails.getMemberId();

        String adminName = memberService.findById(adminId)
                .map(Member::getMemberName)
                .orElse("admin");

        boolean success = orderHistoryService.adminUpdate(adminName, dto);
        if (!success) {
            ApiResponseDTO<Void> response = ApiResponseDTO.error("주문 기록 수정에 실패했습니다.");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        return ResponseEntity.ok(ApiResponseDTO.success(null));
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteOrderHistory(
            @PathVariable String orderId,
            @RequestParam String productCode,
            @RequestParam String confirmDelete) {

        if (!orderId.equals(confirmDelete)) {
            ApiResponseDTO<Void> response = ApiResponseDTO.error("주문번호와 일치하지 않습니다.");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }

        boolean success = orderHistoryService.adminDelete(orderId, productCode);
        if (!success) {
            ApiResponseDTO<Void> response = ApiResponseDTO.error("주문 기록 삭제에 실패했습니다.");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        return ResponseEntity.ok(ApiResponseDTO.success(null));
    }

    @PutMapping("/products/{productCode}")
    public ResponseEntity<ApiResponseDTO<Void>> updateProduct(
            @PathVariable String productCode,
            @ModelAttribute ProductRequestDTO dto,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal JwtUserDetails userDetails) throws IOException {

        String adminId = userDetails.getMemberId();

        String adminName = memberService.findById(adminId)
                .map(Member::getMemberName)
                .orElse("admin");

        Product product = productService.findById(productCode)
                .orElseThrow(() -> new NotFoundException("제품이 존재하지 않습니다."));

        String typeFolder = switch (dto.getProductType()) {
            case 1 -> "mix";
            case 2 -> "cafe";
            default -> "bean";
        };

        String filePath = fileUploadPath + "/" + typeFolder + "/" + dto.getProductCode();

        File uploadPath = new File(filePath);
        if (!uploadPath.exists()) {
            boolean created = uploadPath.mkdirs();
            if (!created) log.error("디렉토리 생성 실패: {}", filePath);
        }

        String thumbnailFileName = product.getProductFile();

        if (files != null && !files.isEmpty() && !files.stream().allMatch(MultipartFile::isEmpty)) {
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                if (fileName == null || fileName.isEmpty()) continue;

                File dest = new File(filePath, fileName);
                if (dest.exists()) dest.delete();
                file.transferTo(dest);

                if (fileName.contains("thumbnail")) thumbnailFileName = fileName;

                final String finalFileName = fileName;
                boolean exists = productImageService.findByProductCode(productCode)
                        .stream()
                        .anyMatch(img -> img.getFileName().equals(finalFileName));

                if (exists) {
                    productImageService.findByProductCode(productCode)
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
        }

        product.adminUpdateProduct(dto, thumbnailFileName, adminName);
        productService.updateProduct(product);

        if (dto.getProductType() == 0) {
            productService.findBeanById(productCode)
                    .ifPresent(bean -> {
                        bean.adminUpdate(dto, adminName);
                        productService.updateBean(bean);
                    });
        } else if (dto.getProductType() == 1) {
            productService.findMixById(productCode)
                    .ifPresent(mix -> {
                        mix.adminUpdate(dto, adminName);
                        productService.updateMix(mix);
                    });
        }

        return ResponseEntity.ok(ApiResponseDTO.success(null));
    }

    @GetMapping("/excel/products")
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

        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
        out.close();
    }

    @GetMapping("/excel/members")
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

        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
        out.close();
    }

    @GetMapping("/excel/orders")
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

        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
        out.close();
    }
}