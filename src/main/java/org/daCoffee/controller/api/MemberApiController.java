package org.daCoffee.controller.api;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dao.HistoryDAO;
import org.daCoffee.dao.MemberDAO;
import org.daCoffee.dao.ProductDAO;
import org.daCoffee.dto.*;
import org.daCoffee.dto.request.PaymentsRequestDTO;
import org.daCoffee.dto.response.CartDataDTO;
import org.daCoffee.dto.response.CartPriceDTO;
import org.daCoffee.dto.response.PaymentsDataDTO;
import org.daCoffee.entity.Cart;
import org.daCoffee.entity.Member;
import org.daCoffee.exception.NotFoundException;
import org.daCoffee.module.UUIDGenerateModule;
import org.daCoffee.service.CartService;
import org.daCoffee.service.MailService;
import org.daCoffee.service.MemberService;
import org.daCoffee.service.PriceCalculator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.daCoffee.util.SecurityUtil.getRandomPassword;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {
  private final MemberService memberService;
  private final ProductDAO productDao;
  private final CartService cartService;
  private final HistoryDAO historyDao;
  private final PriceCalculator priceCalculator;
  private final MailService mailService;
  private final PasswordEncoder passwordEncoder;

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

  @GetMapping("/cart")
  public ApiResponseDTO<CartDataDTO> getCart(@SessionAttribute String memberId) {
    try {
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
          .productSoldOut(Integer.parseInt(c.getProduct().getProductSoldOut()))
          .productType(c.getProduct().getProductType())
          .quantity(c.getQuantity())
          .productGrinding(Integer.parseInt(c.getProductGrinding()))
          .build())
        .toList();

      CartDataDTO data = CartDataDTO.builder()
        .cartCount(cartPriceDTO.getCartCount())
        .sumPrice(cartPriceDTO.getSumPrice())
        .deliveryFee(cartPriceDTO.getDeliveryFee())
        .totalPrice(cartPriceDTO.getTotalPrice())
        .list(list)
        .build();

      return ApiResponseDTO.success(data);
    } catch (Exception e) {
      log.error("장바구니 조회 실패", e);
      return ApiResponseDTO.error("장바구니 조회 중 오류가 발생했습니다.");
    }
  }

  @PostMapping("/cart/add")
  public ApiResponseDTO<CartDTO> addToCart(
    @SessionAttribute String memberId,
    @RequestParam String productCode,
    @RequestParam(defaultValue = "1") int quantity,
    @RequestParam(value = "additionalProducts", required = false) List<String> additionalProductsCodes) {

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

      ProductDTO productDTO = productDao.productSelectOne(productCode);
      if (productDTO == null) {
        throw new NotFoundException("상품을 찾을 수 없습니다.");
      }

      cartService.addOrUpdate(memberId, productCode, quantity);

      CartDTO data = CartDTO.builder()
        .productCode(productDTO.getProductCode())
        .productType(productDTO.getProductType())
        .productName(productDTO.getProductName())
        .productUnit(productDTO.getProductUnit())
        .quantity(quantity)
        .productPrice(productDTO.getProductPrice())
        .productFile(productDTO.getProductFile())
        .build();

      return ApiResponseDTO.success(data);
    } catch (Exception e) {
      log.error("장바구니 추가 실패", e);
      return ApiResponseDTO.error("장바구니 추가 중 오류가 발생했습니다.");
    }
  }

  @PostMapping("/cart/update")
  public ApiResponseDTO<CartDataDTO> updateCart(
    @RequestParam String status,
    @RequestParam String productCode,
    @SessionAttribute String memberId) {

    int delta = 0;

    try {
      if ("delete".equals(status)) {
        cartService.deleteCartItem(memberId, productCode);
      } else if ("increase".equals(status)) {
        delta = 1;
        cartService.updateQuantity(memberId, productCode, 1);
      } else if("decrease".equals(status)) {
        delta = -1;
        cartService.updateQuantity(memberId, productCode, -1);
      }

      // ==== 제품 갯수 변경 후 가격 계산 ====
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
          .productSoldOut(Integer.parseInt(c.getProduct().getProductSoldOut()))
          .productType(c.getProduct().getProductType())
          .quantity(c.getQuantity())
          .productGrinding(Integer.parseInt(c.getProductGrinding()))
          .build())
        .toList();

      CartDataDTO data = CartDataDTO.builder()
        .cartCount(cartPriceDTO.getCartCount())
        .sumPrice(cartPriceDTO.getSumPrice())
        .deliveryFee(cartPriceDTO.getDeliveryFee())
        .totalPrice(cartPriceDTO.getTotalPrice())
        .list(list)
        .build();

      log.info("updateCart called: memberId={}, productCode={}, status={}, delta={}",
        memberId, productCode, status, delta);

      return ApiResponseDTO.success(data);

    } catch (Exception e) {
      log.error("장바구니 업데이트 실패", e);
      return ApiResponseDTO.error("처리 중 오류 발생");
    }
  }

  @GetMapping("/payments")
  public ApiResponseDTO<PaymentsDataDTO> getPaymentsData(
    HttpSession session,
    @SessionAttribute String memberId,
    @SessionAttribute(required = false) Integer totalPrice) {

    // 회원 검증
    Member member = memberService.findById(memberId)
      .orElseThrow(() -> new NotFoundException("회원 정보를 찾을 수 없습니다."));

    // 장바구니에서 넘어오는 최종 가격 검증
    if (totalPrice == null) {
      return ApiResponseDTO.error("가격정보가 존재하지 않습니다.");
    }

    // 장바구니 상품 검증
    List<Cart> cartList = cartService.getCartList(memberId);
    if (cartList == null || cartList.isEmpty()) {
      return ApiResponseDTO.error("장바구니에 상품이 존재하지 않습니다.");
    }

    UUIDGenerateModule uuid = new UUIDGenerateModule();
    String orderId = uuid.generateOrderId();
    String customerKey = uuid.generateCustomerKey(memberId);

    List<String> productNames = cartList.stream()
      .map(c -> c.getProduct().getProductName())
      .toList();

    String orderName = productNames.get(0) + " 외 " + (productNames.size() - 1) + "건";

    //세션에 저장 (결제 성공 검증용)
    session.setAttribute("orderId", orderId);
    session.setAttribute("customerKey", customerKey);
    session.setAttribute("totalPrice", totalPrice);

    PaymentsDataDTO data = PaymentsDataDTO.builder()
      .orderId(orderId)
      .customerKey(customerKey)
      .orderName(orderName)
      .totalPrice(totalPrice)
      .member(member)
      .cartItems(cartList)
      .build();

    return ApiResponseDTO.success(data);
  }

  @PostMapping("/payments/success")
  public ApiResponseDTO<Void> paymentsSuccess(
    HttpSession session,
    @SessionAttribute String memberId,
    @RequestParam String orderId,
    @RequestParam int amount) {

    try {
      // 회원 검증
      Member member = memberService.findById(memberId)
        .orElseThrow(() -> new NotFoundException("회원 정보를 찾을 수 없습니다."));

      // 장바구니에서 넘어오는 최종 가격 검증
      Integer sessionTotal = (Integer) session.getAttribute("totalPrice");
      if (sessionTotal == null || sessionTotal != amount) {
        return ApiResponseDTO.error("결제 금액 불일치");
      }

      // 장바구니 상품 검증
      List<Cart> cartList = cartService.getCartList(memberId);
      if (cartList == null || cartList.isEmpty()) {
        return ApiResponseDTO.error("장바구니에 상품이 존재하지 않습니다.");
      }

      for (Cart cart : cartList) {
        HistoryDTO historyDTO = new HistoryDTO();

        historyDTO.setOrderId(orderId);
        historyDTO.setMemberTier(memberDTO.getMemberTier());
        historyDTO.setMemberId(memberId);
        historyDTO.setMemberName(memberDTO.getMemberName());
        historyDTO.setMemberFranCode(memberDTO.getMemberFranCode());
        historyDTO.setProductCode(cartDTO.getProductCode());
        historyDTO.setQuantity(cartDTO.getQuantity());
        historyDTO.setDeliveryAddress(memberDTO.getMemberDeliveryAddress());
        historyDTO.setDetailDeliveryAddress(memberDTO.getMemberDetailDeliveryAddress());
        historyDTO.setTotalPrice(amount);

        historyDao.historyInsert(historyDTO);
      }

      cartService.deleteAllByMember(memberId);
      session.removeAttribute("orderId");
      session.removeAttribute("totalPrice");

      return ApiResponseDTO.success("결제 완료되었습니다.", null);
    } catch (Exception e) {
      log.error("결제 성공 처리 중 오류", e);
      return ApiResponseDTO.error("결제 처리 중 오류 발생");
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
        new ParameterizedTypeReference<Map<String, Object>>() {}
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
  public ApiResponseDTO<?> findAccount(
    @RequestBody Map<String, String> body,
    HttpSession session) {

    String findType = body.get("findType");

    Boolean isVerified = (Boolean) session.getAttribute("isVerified");
    if (isVerified == null || !isVerified) {
      return ApiResponseDTO.error("이메일 인증이 필요합니다.");
    }
    session.removeAttribute("isVerified");

    if ("id".equals(findType)) {
      return findId(body);
    } else if ("password".equals(findType)) {
      return findPassword(body);
    }

    return ApiResponseDTO.error("잘못된 요청입니다.");
  }

  @PostMapping("/verifyEmail")
  public ApiResponseDTO<Void> verifyEmail(HttpSession session, @RequestBody Map<String, String> body) {
    String memberEmail = body.get("memberEmail");

    try {
      String code = getRandomPassword(6);
      String subject = "다올커피 - 이메일 인증번호가 도착했습니다.";
      String main = "회원님의 이메일 인증번호는";

      mailService.sendEmail(memberEmail, subject, main, code);
      session.setAttribute("storedVerifyCode", code);
      session.setAttribute("verifyCodeExpiry", System.currentTimeMillis() + 180000L); // 3분

      return ApiResponseDTO.success(null);
    } catch (Exception e) {
      log.error("이메일 전송 실패 : ", e);
      return ApiResponseDTO.error("이메일 전송에 실패했습니다.");
    }
  }

  @PostMapping("/verifyCode")
  public ApiResponseDTO<Void> verifyCode(HttpSession session, @RequestBody Map<String, String> body) {
    String verifyCode = body.get("verifyCode");

    String storedCode = (String) session.getAttribute("storedVerifyCode");
    Long expiry = (Long) session.getAttribute("verifyCodeExpiry");

    if (storedCode == null || expiry == null) {
      return ApiResponseDTO.error("인증번호를 먼저 요청해주세요.");
    }

    if (System.currentTimeMillis() > expiry) {
      return ApiResponseDTO.error("인증시간이 초과되었습니다.");
    }

    if (!verifyCode.equals(storedCode)) {
      return ApiResponseDTO.error("인증번호가 일치하지 않습니다.");
    }

    session.setAttribute("isVerified", true);
    return ApiResponseDTO.success(null);
  }
}
