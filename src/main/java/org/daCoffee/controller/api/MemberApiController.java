package org.daCoffee.controller.api;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dao.CartDAO;
import org.daCoffee.dao.HistoryDAO;
import org.daCoffee.dao.MemberDAO;
import org.daCoffee.dto.*;
import org.daCoffee.module.UUIDGenerateModule;
import org.daCoffee.service.PriceCalculator;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {
  private final MemberDAO memberDao;
  private final CartDAO cartDao;
  private final HistoryDAO historyDao;
  private final PriceCalculator priceCalculator;

  @GetMapping("/cart")
  @ResponseBody
  public Map<String, Object> getCart(@SessionAttribute String memberId) {
    Map<String, Object> response = new HashMap<>();

    CartPriceDTO cartPriceDTO = priceCalculator.calculatePrice(memberId);
    List<CartDTO> list = cartDao.cartSelectMember(memberId);

    response.put("cartCount", cartPriceDTO.getCartCount());
    response.put("sumPrice", cartPriceDTO.getSumPrice());
    response.put("deliveryFee", cartPriceDTO.getDeliveryFee());
    response.put("totalPrice", cartPriceDTO.getTotalPrice());
    response.put("list", list);

    return response;
  }

  @PostMapping("/cart/update")
  @ResponseBody
  public Map<String, Object> updateCart(
    @RequestParam(defaultValue = "9") int status,
    @RequestParam(defaultValue = "1") int quantity,
    @RequestParam(defaultValue = "0") int productGrinding,
    @RequestParam String productCode,
    @SessionAttribute String memberId) {

    Map<String, Object> response = new HashMap<>();

    try {
      if (status == 0) { // status 0 = delete || status 1 = updateQuantity || status 2 = updateGrindingType
        cartDao.cartDelete(memberId, productCode);
      } else if (status == 1) {
        cartDao.cartQuantityUpdate(memberId, productCode, quantity);
      } // else if (status == 2) {
      //     cartDao.cartGrindingUpdate(memberId, productCode, productGrinding);
      // }

      // ==== 제품 갯수 변경 후 가격 계산 ====
      CartPriceDTO cartPriceDTO = priceCalculator.calculatePrice(memberId);

      final int totalPrice = cartPriceDTO.getTotalPrice();
      final int sumPrice = cartPriceDTO.getSumPrice();
      final int deliveryFee = cartPriceDTO.getDeliveryFee();
      final int cartCount = cartPriceDTO.getCartCount();

      List<CartDTO> list = cartDao.cartSelectMember(memberId);

      response.put("success", true);
      response.put("cartCount", cartCount);
      response.put("sumPrice", sumPrice);
      response.put("deliveryFee", deliveryFee);
      response.put("totalPrice", totalPrice);
      response.put("list", list);

      log.info("updateCart called: memberId={}, productCode={}, status={}, quantity={}",
        memberId, productCode, status, quantity);

    } catch (Exception e) {
      log.error("장바구니 업데이트 실패", e);
      response.put("success", false);
      response.put("message", "처리 중 오류 발생");
    }

    return response;
  }

  @GetMapping("/payments")
  @ResponseBody
  public Map<String, Object> getPaymentsData(
    HttpSession session,
    @SessionAttribute String memberId,
    @SessionAttribute(required = false) Integer totalPrice) {

    Map<String, Object> response = new HashMap<>();

    // 로그인 검증
    if (memberId == null) {
      response.put("success", false);
      response.put("message", "로그인 후 결제를 진행해주세요.");
      return response;
    }

    // 회원 검증
    MemberDTO memberDTO = memberDao.memberSelectOne(memberId);
    if (memberDTO == null) {
      response.put("success", false);
      response.put("message", "회원 정보를 찾을 수 없습니다.");
      return response;
    }

    // 장바구니에서 넘어오는 최종 가격 검증
    if (totalPrice == null) {
      response.put("success", false);
      response.put("message", "가격정보가 존재하지 않습니다.");
      return response;
    }

    // 장바구니 상품 검증
    List<CartDTO> list = cartDao.cartSelectMember(memberId);
    if (list == null || list.isEmpty()) {
      response.put("success", false);
      response.put("message", "장바구니에 상품이 존재하지 않습니다.");
      return response;
    }

    UUIDGenerateModule uuid = new UUIDGenerateModule();
    String orderId = uuid.generateOrderId();
    String customerKey = uuid.generateCustomerKey(memberId);

    List<String> productNames = list.stream()
      .map(CartDTO::getProductName)
      .toList();

    String orderName = productNames.get(0) + " 외 " + (productNames.size() - 1) + "건";

    //세션에 저장 (결제 성공 검증용)
    session.setAttribute("orderId", orderId);
    session.setAttribute("customerKey", customerKey);
    session.setAttribute("totalPrice", totalPrice);

    response.put("success", true);
    response.put("orderId", orderId);
    response.put("customerKey", customerKey);
    response.put("orderName", orderName);
    response.put("totalPrice", totalPrice);
    response.put("member", memberDTO);
    response.put("cartItems", list);

    return response;
  }

  @PostMapping("/payments/success")
  @ResponseBody
  public Map<String, Object> paymentsSuccess(
    HttpSession session,
    @SessionAttribute String memberId,
    @RequestParam String orderId,
    @RequestParam int amount) {

    Map<String, Object> response = new HashMap<>();

    String errorMessage = "로그인 후 결제를 진행해주세요.";

    try {
      // 로그인 검증
      if (memberId == null) {
        response.put("success", false);
        response.put("message", errorMessage);
        return response;
      }

      // 회원 검증
      MemberDTO memberDTO = memberDao.memberSelectOne(memberId);
      if (memberDTO == null) {
        response.put("success", false);
        response.put("message", errorMessage);
        return response;
      }

      // 장바구니에서 넘어오는 최종 가격 검증
      Integer sessionTotal = (Integer) session.getAttribute("totalPrice");
      if (sessionTotal == null || sessionTotal != amount) {
        response.put("success", false);
        response.put("message", "결제 금액 불일치");
        return response;
      }

      // 장바구니 상품 검증
      List<CartDTO> list = cartDao.cartSelectMember(memberId);
      if (list == null || list.isEmpty()) {
        response.put("success", false);
        response.put("message", "장바구니에 상품이 존재하지 않습니다.");
        return response;
      }

      for (CartDTO cartDTO : list) {
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

      cartDao.deleteCartByMember(memberId);
      session.removeAttribute("orderId");
      session.removeAttribute("totalPrice");

      response.put("success", true);
      response.put("message", "결제 완료되었습니다.");

    } catch (Exception e) {
      log.error("결제 성공 처리 중 오류", e);
      response.put("success", false);
      response.put("message", "결제 처리 중 오류 발생");
    }

    return response;
  }

  @PostMapping("/payments/confirm")
  @ResponseBody
  public ResponseEntity<Map<String, Object>> memberPaymentsConfirm(
    @RequestBody PaymentsRequestDTO paymentsRequestDTO) {

    String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
    String encodedSecretKey = "Basic " + Base64.getEncoder().encodeToString((widgetSecretKey + ":").getBytes());

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
}
