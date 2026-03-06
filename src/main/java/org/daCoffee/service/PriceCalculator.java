package org.daCoffee.service;

import lombok.RequiredArgsConstructor;
import org.daCoffee.dao.CartDAO;
import org.daCoffee.dto.response.CartPriceDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PriceCalculator {
  private final CartDAO cartDao;

  @Value("${DELIVERY_FEE:3000}")
  private int deliveryFee;

  private static final Set<String> FREE_SHIPPING_PRODUCT_CODES = Set.of(
    "DA0002", "DA0002-2", "DA0011", "DA0011-2", "DA0012", "DA0012-2", "DA0013", "DA0013-2", "DA0013-3", "CA0003", "CA0010", "CA0101", "CA0105"
  ); // 무료배송 상품

  private static final Set<String> PAID_SHIPPING_BEAN_PRODUCT_CODES = Set.of(
    "AA0001", "AA0001-2", "AA0011", "AA0011-2", "AA0021", "AA0021-2", "AA0031", "AA0031-2", "AA0041", "AA0041-2", "AA0041-3", "AA0051", "AA0051-2", "AA0061", "AA0061-2"
  );

  private static final Set<String> PAID_SHIPPING_CAFE_PRODUCT_CODES = Set.of(
    "CA0001", "CA0202", "CA0214", "CA0501", "CA0210", "CA0520"
  );

  private static final String SPECIFIC_FEE_PRODUCT_CODE = "CA0001"; // specificProduct = 배송비가 건당으로 붙는 특정 제품

  public CartPriceDTO calculatePrice(String memberId) {
    int sumPrice = cartDao.cartSumPrice(memberId);
    int cartCount = cartDao.cartCount(memberId);
    int deliveryFee = calculateDeliveryFee(memberId, sumPrice);

    final int totalPrice = deliveryFee + sumPrice; // 총 가격 계산 후 변경 불가

    CartPriceDTO cartPriceDTO = new CartPriceDTO();
    cartPriceDTO.setSumPrice(sumPrice);
    cartPriceDTO.setCartCount(cartCount);
    cartPriceDTO.setDeliveryFee(deliveryFee);
    cartPriceDTO.setTotalPrice(totalPrice);

    return cartPriceDTO;
  }

  private int calculateDeliveryFee(String memberId, int sumPrice) {
    List<String> cartProductCodeList = cartDao.cartProductCodeList(memberId);
    List<Integer> beanQuantityList = cartDao.checkQuantityByProductType(memberId, 0); // 담은 원두 상품의 개수 확인
    int quantityBySpecificProduct = cartDao.checkQuantityByProductCode(memberId, SPECIFIC_FEE_PRODUCT_CODE);

    boolean hasBeanQuantityOverThanOne = beanQuantityList.stream().anyMatch(beanQuantity -> beanQuantity >= 2 ); // 개수가 2 이상이여야 true
    boolean allAreFreeShipping = FREE_SHIPPING_PRODUCT_CODES.containsAll(cartProductCodeList);
    boolean allAreCafeProduct = PAID_SHIPPING_CAFE_PRODUCT_CODES.containsAll(cartProductCodeList);
    boolean onlySpecificFeeProduct = cartProductCodeList.size() == 1 && cartProductCodeList.contains(SPECIFIC_FEE_PRODUCT_CODE);
    long paidShippingBeanProductCount = cartProductCodeList.stream().filter(PAID_SHIPPING_BEAN_PRODUCT_CODES::contains).count();

    int fee = 0;

    if(paidShippingBeanProductCount > 0) {
      fee += (int) (deliveryFee * paidShippingBeanProductCount);
    }

    if(allAreCafeProduct) { // specificFeeProduct를 제외한 나머지 물품은 배송비는 1회만 부여
      fee = deliveryFee;
    }

    if(!hasBeanQuantityOverThanOne) {
      fee = deliveryFee;
    }

    if(onlySpecificFeeProduct && quantityBySpecificProduct >= 2) { // CA0001 제품은 건당으로 배송비 발생
      fee = fee + (deliveryFee * (quantityBySpecificProduct - 1));
    } else {
      fee = fee + (deliveryFee * (quantityBySpecificProduct));
    }

    if(paidShippingBeanProductCount >= 2 || hasBeanQuantityOverThanOne || sumPrice == 0 || allAreFreeShipping) { // 2키로 이상시 전체 배송비 무료, 카트에 담은게 없어도 배송비 x
      fee = 0;
    }

    return fee;
  }
}
