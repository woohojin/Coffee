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
}
