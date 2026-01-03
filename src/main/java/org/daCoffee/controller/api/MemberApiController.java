package org.daCoffee.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dao.CartDAO;
import org.daCoffee.dto.CartDTO;
import org.daCoffee.dto.CartPriceDTO;
import org.daCoffee.service.PriceCalculator;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {
  private final CartDAO cartDao;
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
}
