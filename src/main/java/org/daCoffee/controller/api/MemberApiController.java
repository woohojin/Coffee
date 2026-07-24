package org.daCoffee.controller.api;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dto.*;
import org.daCoffee.dto.request.MemberSignUpRequestDTO;
import org.daCoffee.dto.request.MemberUpdateRequestDTO;
import org.daCoffee.dto.request.PaymentsRequestDTO;
import org.daCoffee.dto.response.CartDataDTO;
import org.daCoffee.dto.response.CartPriceDTO;
import org.daCoffee.dto.response.MemberProfileDTO;
import org.daCoffee.dto.response.PaymentsDataDTO;
import org.daCoffee.entity.Cart;
import org.daCoffee.entity.Member;
import org.daCoffee.entity.OrderHistory;
import org.daCoffee.exception.NotFoundException;
import org.daCoffee.jwt.JwtUserDetails;
import org.daCoffee.module.UUIDGenerateModule;
import org.daCoffee.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.daCoffee.util.SecurityUtil.getRandomPassword;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {
  private final MemberService memberService;
  private final ProductService productService;
  private final CartService cartService;
  private final OrderHistoryService orderHistoryService;
  private final PriceCalculator priceCalculator;
  private final MailService mailService;
  private final PasswordEncoder passwordEncoder;
  private final RedisService redisService;

  @Value("${FILE_UPLOAD_PATH}")
  private String fileUploadPath;

  @Value("${SECRET_TOSS_WIDGET_KEY}")
  private String secretTossWidgetKey;

  private ApiResponseDTO<List<String>> findId(Map<String, String> body) {
    String memberName = body.get("memberName");
    String memberEmail = body.get("memberEmail");

    List<String> ids = memberService.findMemberIdByNameAndEmail(memberName, memberEmail);
    if (ids == null || ids.isEmpty()) {
      return ApiResponseDTO.error("이름 또는 이메일이 일치하지 않습니다.");
    }

    return ApiResponseDTO.success(ids);
  }

  private ApiResponseDTO<Void> findPassword(Map<String, String> body) {
    String memberId = body.get("memberId");
    String memberEmail = body.get("memberEmail");

    Optional<String> found = memberService.findPasswordByMemberIdAndEmail(memberId, memberEmail);
    if (found.isEmpty()) {
      return ApiResponseDTO.error("아이디 또는 이메일이 일치하지 않습니다.");
    }

    String tempPassword = getRandomPassword(8);
    mailService.sendEmail(memberEmail, "다올커피 임시 비밀번호", "임시 비밀번호: ", tempPassword);
    String encoded = passwordEncoder.encode(tempPassword);
    memberService.updatePassword(memberId, encoded);

    return ApiResponseDTO.success("임시 비밀번호가 이메일로 전송되었습니다.", null);
  }

  @PostMapping("/signup")
  public ResponseEntity<ApiResponseDTO<Void>> signUp(
    @ModelAttribute MemberSignUpRequestDTO dto,
    @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

    if (!redisService.isVerified(dto.getMemberEmail())) {
      ApiResponseDTO<Void> response = ApiResponseDTO.error("이메일 인증이 필요합니다.");
      return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    redisService.deleteVerified(dto.getMemberEmail());

    if (memberService.existsById(dto.getMemberId())) {
      ApiResponseDTO<Void> response = ApiResponseDTO.error("이미 사용 중인 아이디입니다.");
      return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    String memberFile = null;
    if (file != null && !file.isEmpty()) {
      String filePath = fileUploadPath + "/member/" + dto.getMemberId();

      File uploadPath = new File(filePath);
      if (!uploadPath.exists()) {
        boolean created = uploadPath.mkdirs();
        if (!created) log.error("디렉토리 생성 실패: {}", filePath);
      }

      memberFile = file.getOriginalFilename();
      try {
        file.transferTo(new File(filePath, memberFile));
      } catch (IOException e) {
        log.error("파일 업로드 실패: {}", e.getMessage());
      }
    }

    Member member = Member.builder()
      .memberId(dto.getMemberId())
      .memberName(dto.getMemberName())
      .memberCompanyName(dto.getMemberCompanyName())
      .memberPassword(passwordEncoder.encode(dto.getMemberPassword()))
      .memberTel(dto.getMemberTel())
      .memberCompanyTel(dto.getMemberCompanyTel())
      .memberAddress(dto.getMemberAddress())
      .memberDetailAddress(dto.getMemberDetailAddress())
      .memberDeliveryAddress(dto.getMemberDeliveryAddress())
      .memberDetailDeliveryAddress(dto.getMemberDetailDeliveryAddress())
      .memberEmail(dto.getMemberEmail())
      .memberFile(memberFile)
      .memberTier(0)
      .memberDisabledStatus(false)
      .memberDate(LocalDate.now())
      .build();

    memberService.save(member);

    return ResponseEntity.ok(ApiResponseDTO.success(null));
  }

  @GetMapping("/me") // React 로그인 여부 확인용 - 비로그인도 정상 응답(200, data: null)
  public ResponseEntity<ApiResponseDTO<MemberDTO>> getMe(
          @AuthenticationPrincipal JwtUserDetails userDetails) {

    if (userDetails == null) {
      return ResponseEntity.ok(ApiResponseDTO.success(null));
    }

    String memberId = userDetails.getMemberId();

    Member member = memberService.findById(memberId)
            .orElseThrow(() -> new NotFoundException("회원 없음"));

    MemberDTO dto = MemberDTO.builder()
            .memberId(member.getMemberId())
            .memberTier(member.getMemberTier())
            .memberName(member.getMemberName())
            .build();

    return ResponseEntity.ok(ApiResponseDTO.success(dto));
  }

  @GetMapping("/profile")
  public ApiResponseDTO<MemberProfileDTO> getProfile(
          @AuthenticationPrincipal JwtUserDetails userDetails) {

    String memberId = userDetails.getMemberId();
    Member member = memberService.findById(memberId)
            .orElseThrow(() -> new NotFoundException("회원 없음"));

    MemberProfileDTO dto = MemberProfileDTO.builder()
            .memberId(member.getMemberId())
            .memberName(member.getMemberName())
            .memberAddress(member.getMemberAddress())
            .memberDetailAddress(member.getMemberDetailAddress())
            .memberDeliveryAddress(member.getMemberDeliveryAddress())
            .memberDetailDeliveryAddress(member.getMemberDetailDeliveryAddress())
            .memberTel(member.getMemberTel())
            .memberCompanyName(member.getMemberCompanyName())
            .memberCompanyTel(member.getMemberCompanyTel())
            .memberEmail(member.getMemberEmail())
            .memberFile(member.getMemberFile())
            .build();

    return ApiResponseDTO.success(dto);
  }

  @PutMapping("/profile")
  public ResponseEntity<ApiResponseDTO<Void>> updateProfile(
          @ModelAttribute MemberUpdateRequestDTO dto,
          @RequestParam String memberExistingPassword,
          @RequestParam(value = "file", required = false) MultipartFile file,
          @AuthenticationPrincipal JwtUserDetails userDetails) throws IOException {

    String memberId = userDetails.getMemberId();
    Member member = memberService.findById(memberId)
            .orElseThrow(() -> new NotFoundException("회원 없음"));

    if (!passwordEncoder.matches(memberExistingPassword, member.getMemberPassword())) {
      ApiResponseDTO<Void> response = ApiResponseDTO.error("기존 비밀번호가 일치하지 않습니다.");
      return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    if (file != null && !file.isEmpty()) {
      String filePath = fileUploadPath + "/member/" + memberId;

      File uploadPath = new File(filePath);
      if (!uploadPath.exists()) {
        boolean created = uploadPath.mkdirs();
        if (!created) log.error("디렉토리 생성 실패: {}", filePath);
      }

      String fileName = file.getOriginalFilename();
      try {
        file.transferTo(new File(filePath, fileName));
        dto.setMemberFile(fileName);
      } catch (IOException e) {
        log.error("파일 업로드 실패: {}", e.getMessage());
      }
    } else {
      dto.setMemberFile(member.getMemberFile());
    }

    if (dto.getMemberPassword() != null && !dto.getMemberPassword().isBlank()) {
      String encoded = passwordEncoder.encode(dto.getMemberPassword());
      memberService.update(memberId, encoded, dto);
    } else {
      memberService.updateWithoutPassword(memberId, dto);
    }

    return ResponseEntity.ok(ApiResponseDTO.success(null));
  }

  @DeleteMapping("/profile")
  public ResponseEntity<ApiResponseDTO<Void>> memberWithdrawal(
          @RequestBody Map<String, String> body,
          @AuthenticationPrincipal JwtUserDetails userDetails,
          HttpServletResponse response) {

    String memberPassword = body.get("memberPassword");
    String memberId = userDetails.getMemberId();
    Member member = memberService.findById(memberId)
            .orElseThrow(() -> new NotFoundException("회원 없음"));

    if (!passwordEncoder.matches(memberPassword, member.getMemberPassword())) {
      return ResponseEntity.badRequest()
              .body(ApiResponseDTO.error("비밀번호가 틀렸습니다."));
    }

    memberService.withdrawMember(memberId);

    ResponseCookie accessCookie = ResponseCookie.from("accessToken", "")
            .httpOnly(true).sameSite("Lax").path("/").maxAge(0).build();
    ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
            .httpOnly(true).sameSite("Lax").path("/api/auth/refresh").maxAge(0).build();

    response.addHeader("Set-Cookie", accessCookie.toString());
    response.addHeader("Set-Cookie", refreshCookie.toString());

    return ResponseEntity.ok(ApiResponseDTO.success(null));
  }

  @GetMapping("/history")
  public ApiResponseDTO<List<OrderHistory>> memberHistory(
          @RequestParam(required = false) String startDate,
          @RequestParam(required = false) String endDate,
          @AuthenticationPrincipal JwtUserDetails userDetails) {

    String memberId = userDetails.getMemberId();
    LocalDateTime start;
    LocalDateTime end;

    if (startDate == null || endDate == null) {
      LocalDate now = LocalDate.now();
      start = LocalDateTime.of(now.minusMonths(3), LocalTime.MIN);
      end = LocalDateTime.of(now, LocalTime.MAX);
    } else {
      start = LocalDateTime.parse(startDate + "T00:00:00");
      end = LocalDateTime.parse(endDate + "T23:59:59");
    }

    List<OrderHistory> list = orderHistoryService.findByMemberIdBetween(memberId, start, end);
    return ApiResponseDTO.success(list);
  }

  private CartDataDTO buildCartData(String memberId) {
    CartPriceDTO cartPriceDTO = priceCalculator.calculatePrice(memberId);
    List<Cart> cartList = cartService.getCartList(memberId);

    List<CartDTO> list = cartList.stream()
      .map(c -> CartDTO.builder()
        .memberId(c.getId().getMemberId())
        .productCode(c.getId().getProductCode())
        .productName(c.getProduct().getProductName())
        .productUnit(c.getProduct().getProductUnit())
        .productPrice(c.getProduct().getProductPrice())
        .productFile(c.getProduct().getProductFile())
        .productSoldOut(c.getProduct().isProductSoldOut() ? 1 : 0)
        .productType(c.getProduct().getProductType())
        .quantity(c.getQuantity())
        .build())
      .toList();

    return CartDataDTO.builder()
      .cartCount(cartPriceDTO.getCartCount())
      .sumPrice(cartPriceDTO.getSumPrice())
      .deliveryFee(cartPriceDTO.getDeliveryFee())
      .totalPrice(cartPriceDTO.getTotalPrice())
      .list(list)
      .build();
  }

  @GetMapping("/cart")
  public ResponseEntity<ApiResponseDTO<CartDataDTO>> getCart(@AuthenticationPrincipal JwtUserDetails userDetails) {
    try {
      String memberId = userDetails.getMemberId();
      return ResponseEntity.ok(ApiResponseDTO.success(buildCartData(memberId)));
    } catch (Exception e) {
      log.error("장바구니 조회 실패", e);
      ApiResponseDTO<CartDataDTO> response = ApiResponseDTO.error("장바구니 조회 중 오류가 발생했습니다.");
      return ResponseEntity.status(response.getStatusCode()).body(response);
    }
  }

  @PostMapping("/cart/items")
  public ResponseEntity<ApiResponseDTO<CartDTO>> addToCart(
    @AuthenticationPrincipal JwtUserDetails userDetails,
    @RequestParam String productCode,
    @RequestParam(defaultValue = "1") int quantity,
    @RequestParam(value = "additionalProducts", required = false) List<String> additionalProductsCodes) {

    String memberId = userDetails.getMemberId();

    try {
      // 추가 상품 처리
      if (additionalProductsCodes != null && !additionalProductsCodes.isEmpty()) {
        for (String code : additionalProductsCodes) {
          if (!code.equals("none")) {
            cartService.addOrUpdate(memberId, code, 1);
          }
        }
      }

      if (quantity < 1) quantity = 1;

      productService.findById(productCode)
        .orElseThrow(() -> new NotFoundException("상품을 찾을 수 없습니다."));

      cartService.addOrUpdate(memberId, productCode, quantity);

      Cart cart = cartService.getCartItem(memberId, productCode)
        .orElseThrow(() -> new NotFoundException("장바구니 항목 없음"));

      CartDTO cartDTO = CartDTO.builder()
        .memberId(cart.getId().getMemberId())
        .productCode(cart.getId().getProductCode())
        .productName(cart.getProduct().getProductName())
        .productUnit(cart.getProduct().getProductUnit())
        .productPrice(cart.getProduct().getProductPrice())
        .productFile(cart.getProduct().getProductFile())
        .productSoldOut(cart.getProduct().isProductSoldOut() ? 1 : 0)
        .productType(cart.getProduct().getProductType())
        .quantity(cart.getQuantity())
        .build();

      return ResponseEntity.ok(ApiResponseDTO.success(cartDTO));
    } catch (Exception e) {
      log.error("장바구니 추가 실패", e);
      ApiResponseDTO<CartDTO> response = ApiResponseDTO.error("장바구니 추가 중 오류가 발생했습니다.");
      return ResponseEntity.status(response.getStatusCode()).body(response);
    }
  }

  @PatchMapping("/cart/items/{productCode}")
  public ResponseEntity<ApiResponseDTO<CartDataDTO>> updateCartItemQuantity(
    @PathVariable String productCode,
    @RequestParam int delta,
    @AuthenticationPrincipal JwtUserDetails userDetails) {

    String memberId = userDetails.getMemberId();

    try {
      cartService.updateQuantity(memberId, productCode, delta);
      return ResponseEntity.ok(ApiResponseDTO.success(buildCartData(memberId)));
    } catch (Exception e) {
      log.error("장바구니 수량 변경 실패", e);
      ApiResponseDTO<CartDataDTO> response = ApiResponseDTO.error("처리 중 오류 발생");
      return ResponseEntity.status(response.getStatusCode()).body(response);
    }
  }

  @DeleteMapping("/cart/items/{productCode}")
  public ResponseEntity<ApiResponseDTO<CartDataDTO>> deleteCartItem(
    @PathVariable String productCode,
    @AuthenticationPrincipal JwtUserDetails userDetails) {

    String memberId = userDetails.getMemberId();

    try {
      cartService.deleteCartItem(memberId, productCode);
      return ResponseEntity.ok(ApiResponseDTO.success(buildCartData(memberId)));
    } catch (Exception e) {
      log.error("장바구니 삭제 실패", e);
      ApiResponseDTO<CartDataDTO> response = ApiResponseDTO.error("처리 중 오류 발생");
      return ResponseEntity.status(response.getStatusCode()).body(response);
    }
  }

  @PostMapping("/payments")
  public ResponseEntity<ApiResponseDTO<PaymentsDataDTO>> getPaymentsData(
    @AuthenticationPrincipal JwtUserDetails userDetails) {

    String memberId = userDetails.getMemberId();
    // 회원 검증
    Member member = memberService.findById(memberId)
      .orElseThrow(() -> new NotFoundException("회원 정보를 찾을 수 없습니다."));

    // 장바구니 상품 검증
    List<Cart> cartList = cartService.getCartList(memberId);
    if (cartList == null || cartList.isEmpty()) {
      ApiResponseDTO<PaymentsDataDTO> response = ApiResponseDTO.error("장바구니에 상품이 존재하지 않습니다.");
      return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    CartPriceDTO cartPriceDTO = priceCalculator.calculatePrice(memberId);
    Integer totalPrice = cartPriceDTO.getTotalPrice();

    UUIDGenerateModule uuid = new UUIDGenerateModule();
    String orderId = uuid.generateOrderId();
    String customerKey = uuid.generateCustomerKey(memberId);

    List<String> productNames = cartList.stream()
      .map(c -> c.getProduct().getProductName())
      .toList();

    String orderName = productNames.get(0) + " 외 " + (productNames.size() - 1) + "건";

    redisService.savePaymentsData(memberId, orderId, customerKey, totalPrice);

    PaymentsDataDTO data = PaymentsDataDTO.builder()
      .orderId(orderId)
      .customerKey(customerKey)
      .orderName(orderName)
      .totalPrice(totalPrice)
      .member(member)
      .cartItems(cartList)
      .build();

    return ResponseEntity.ok(ApiResponseDTO.success(data));
  }

  @PostMapping("/payments/success")
  public ResponseEntity<ApiResponseDTO<Void>> paymentsSuccess(
    @AuthenticationPrincipal JwtUserDetails userDetails,
    @RequestParam String orderId,
    @RequestParam int amount) {

    String memberId = userDetails.getMemberId();

    try {
      // Redis에서 결제 데이터 검증
      Map<Object, Object> paymentsData = redisService.getPaymentsData(memberId);
      if (paymentsData.isEmpty()) {
        ApiResponseDTO<Void> response = ApiResponseDTO.error("결제 정보가 존재하지 않습니다.");
        return ResponseEntity.status(response.getStatusCode()).body(response);
      }

      // 장바구니에서 넘어오는 최종 가격 검증
      Integer savedTotal = Integer.parseInt((String) paymentsData.get("totalPrice"));
      if (savedTotal != amount) {
        ApiResponseDTO<Void> response = ApiResponseDTO.error("결제 금액 불일치");
        return ResponseEntity.status(response.getStatusCode()).body(response);
      }

      // 회원 검증
      Member member = memberService.findById(memberId)
        .orElseThrow(() -> new NotFoundException("회원 정보를 찾을 수 없습니다."));

      // 장바구니 상품 검증
      List<Cart> cartList = cartService.getCartList(memberId);
      if (cartList == null || cartList.isEmpty()) {
        ApiResponseDTO<Void> response = ApiResponseDTO.error("장바구니에 상품이 존재하지 않습니다.");
        return ResponseEntity.status(response.getStatusCode()).body(response);
      }

      for (Cart cart : cartList) {
        OrderHistory orderHistory = OrderHistory.builder()
          .orderId(orderId)
          .memberTier(member.getMemberTier())
          .memberId(memberId)
          .memberName(member.getMemberName())
          .memberCompanyName(member.getMemberCompanyName())
          .memberFranCode(member.getMemberFranCode())
          .productCode(cart.getProduct().getProductCode())
          .productName(cart.getProduct().getProductName())
          .productUnit(cart.getProduct().getProductUnit())
          .productPrice(cart.getProduct().getProductPrice())
          .quantity(cart.getQuantity())
          .orderDate(LocalDateTime.now())
          .deliveryAddress(member.getMemberDeliveryAddress())
          .detailDeliveryAddress(member.getMemberDetailDeliveryAddress())
          .totalPrice(amount)
          .build();

        orderHistoryService.save(orderHistory);
      }

      cartService.deleteAllByMember(memberId);
      redisService.deletePaymentsData(memberId);

      return ResponseEntity.ok(ApiResponseDTO.success("결제 완료되었습니다.", null));
    } catch (Exception e) {
      log.error("결제 성공 처리 중 오류", e);
      ApiResponseDTO<Void> response = ApiResponseDTO.error("결제 처리 중 오류 발생");
      return ResponseEntity.status(response.getStatusCode()).body(response);
    }
  }

  @PostMapping("/payments/confirm")
  public ResponseEntity<Map<String, Object>> memberPaymentsConfirm(
    @RequestBody PaymentsRequestDTO paymentsRequestDTO) {

    String encodedSecretKey = "Basic " + Base64.getEncoder().encodeToString((secretTossWidgetKey + ":").getBytes());

    String apiUrl = "https://api.tosspayments.com/v1/payments/confirm";

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", encodedSecretKey);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<PaymentsRequestDTO> entity = new HttpEntity<>(paymentsRequestDTO, headers);

    try {
      ResponseEntity<Map<String, Object>> responseEntity = new RestTemplate().exchange(
        apiUrl,
        HttpMethod.POST,
        entity,
          new ParameterizedTypeReference<>() {
          }
      );

      Map<String, Object> body = responseEntity.getBody();
      if (responseEntity.getStatusCode().is2xxSuccessful() && body != null) {
        log.info("토스페이먼츠 결제 확인 성공: {}", body);
        return ResponseEntity.ok(body);
      } else {
        log.warn("토스페이먼츠 결제 확인 실패: {}", body);
        return ResponseEntity.status(responseEntity.getStatusCode()).body(body);
      }

    } catch (Exception e) {
      log.error("토스페이먼츠 결제 확인 중 오류 발생", e);
      Map<String, Object> errorBody = new HashMap<>();
      errorBody.put("error", "결제 확인 중 오류가 발생했습니다.");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
    }
  }

  @PostMapping("/findAccount")
  public ResponseEntity<ApiResponseDTO<?>> findAccount(
    @RequestBody Map<String, String> body) {

    String findType = body.get("findType");
    String memberEmail = body.get("memberEmail");

    if (!redisService.isVerified(memberEmail)) {
      ApiResponseDTO<?> response = ApiResponseDTO.error("이메일 인증이 필요합니다.");
      return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    redisService.deleteVerified(memberEmail);

    ApiResponseDTO<?> response;
    if ("id".equals(findType)) {
      response = findId(body);
    } else if ("password".equals(findType)) {
      response = findPassword(body);
    } else {
      response = ApiResponseDTO.error("잘못된 요청입니다.");
    }

    return ResponseEntity.status(response.getStatusCode()).body(response);
  }

  @PostMapping("/verifyEmail")
  public ResponseEntity<ApiResponseDTO<Void>> verifyEmail(@RequestBody Map<String, String> body) {
    String memberEmail = body.get("memberEmail");

    try {
      String code = getRandomPassword(6);
      String subject = "다올커피 - 이메일 인증번호가 도착했습니다.";
      String main = "회원님의 이메일 인증번호는";

      mailService.sendEmail(memberEmail, subject, main, code);
      redisService.saveVerifyCode(memberEmail, code);

      return ResponseEntity.ok(ApiResponseDTO.success(null));
    } catch (Exception e) {
      log.error("이메일 전송 실패 : ", e);
      ApiResponseDTO<Void> response = ApiResponseDTO.error("이메일 전송에 실패했습니다.");
      return ResponseEntity.status(response.getStatusCode()).body(response);
    }
  }

  @PostMapping("/verifyCode")
  public ResponseEntity<ApiResponseDTO<Void>> verifyCode(@RequestBody Map<String, String> body) {
    String verifyCode = body.get("verifyCode");
    String memberEmail = body.get("memberEmail");

    String storedCode = redisService.getVerifyCode(memberEmail);

    if (storedCode == null) {
      return ResponseEntity.badRequest().body(ApiResponseDTO.error("인증번호를 먼저 요청해주세요."));
    }

    if (!verifyCode.equals(storedCode)) {
      return ResponseEntity.badRequest().body(ApiResponseDTO.error("인증번호가 일치하지 않습니다."));
    }

    redisService.deleteVerifyCode(memberEmail);
    redisService.saveVerified(memberEmail);

    return ResponseEntity.ok(ApiResponseDTO.success(null));
  }
}
