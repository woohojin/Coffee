package org.daCoffee.service;

import lombok.RequiredArgsConstructor;
import org.daCoffee.dto.response.CartPriceDTO;
import org.daCoffee.entity.ProductType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceCalculator {
  private final CartService cartService;

  @Value("${DELIVERY_FEE:3000}")
  private int deliveryFee;

  private static final String SPECIFIC_FEE_PRODUCT_CODE = "CA0001"; // specificProduct = 배송비가 건당으로 붙는 특정 제품

  public CartPriceDTO calculatePrice(String memberId) {
    int sumPrice = cartService.getSumPrice(memberId);
    long cartCount = cartService.getCartCount(memberId);
    int deliveryFee = calculateDeliveryFee(memberId, sumPrice);

    final int totalPrice = deliveryFee + sumPrice; // 총 가격 계산 후 변경 불가

    return CartPriceDTO.builder()
      .sumPrice(sumPrice)
      .cartCount((int) cartCount)
      .deliveryFee(deliveryFee)
      .totalPrice(totalPrice)
      .build();
  }

  private int calculateDeliveryFee(String memberId, int sumPrice) {
    if (sumPrice == 0) return 0;

    List<Integer> beanQuantityList = cartService.getQuantitiesByProductType(memberId, ProductType.BEAN.getCode());
    int quantityBySpecificProduct = cartService.getQuantityByProductCode(memberId, SPECIFIC_FEE_PRODUCT_CODE);

    boolean twoKgOrMore = beanQuantityList.stream().anyMatch(q -> q >= 2);
    int specificFee = deliveryFee * quantityBySpecificProduct;

    if (twoKgOrMore) {
      return specificFee;
    }

    return deliveryFee + specificFee;
  }
}
