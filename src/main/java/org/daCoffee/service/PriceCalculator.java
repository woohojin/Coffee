package org.daCoffee.service;

import lombok.RequiredArgsConstructor;
import org.daCoffee.dao.CartDAO;
import org.daCoffee.dto.CartPriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PriceCalculator {
  private final CartDAO cartDao;

  @Value("${DELIVERY_FEE:3000}")
  private int deliveryFee;

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
    List<String> freeShippingProductCodeList = Arrays.asList(
      "DA0002", "DA0002-2", "DA0011", "DA0011-2", "DA0012", "DA0012-2", "DA0013", "DA0013-2", "DA0013-3", "CA0003", "CA0010", "CA0101", "CA0105"
    ); // 무료배송 상품

    List<String> paidShippingBeanProductCodeList = Arrays.asList(
      "AA0001", "AA0001-2", "AA0011", "AA0011-2", "AA0021", "AA0021-2", "AA0031", "AA0031-2", "AA0041", "AA0041-2", "AA0041-3", "AA0051", "AA0051-2", "AA0061", "AA0061-2"
    );

    List<String> paidShippingCafeProductCodeList = Arrays.asList(
      "CA0001", "CA0202", "CA0214", "CA0501", "CA0210", "CA0520"
    );

    String specificFeeProductCode = "CA0001"; // specificProduct = 배송비가 건당으로 붙는 특정 제품

    List<String> cartProductCodeList = cartDao.cartProductCodeList(memberId);
    List<Integer> beanQuantityList = cartDao.checkQuantityByProductType(memberId, 0); // 담은 원두 상품의 개수 확인
    int quantityBySpecificProduct = cartDao.checkQuantityByProductCode(memberId, specificFeeProductCode);

    boolean hasBeanQuantityOverThanOne = beanQuantityList.stream().anyMatch(beanQuantity -> beanQuantity >= 2 ); // 개수가 2 이상이여야 true
    boolean allAreFreeShipping = cartProductCodeList.stream().allMatch(code -> freeShippingProductCodeList.contains(code));
    boolean allAreCafeProduct = cartProductCodeList.stream().allMatch(code -> paidShippingCafeProductCodeList.contains(code));
    boolean onlySpecificFeeProduct = cartProductCodeList.size() == 1 && cartProductCodeList.contains(specificFeeProductCode);
    long paidShippingBeanProductCount = cartProductCodeList.stream().filter(code -> paidShippingBeanProductCodeList.contains(code)).count();

    if(paidShippingBeanProductCount > 0) {
      deliveryFee += (int) (3000 * (paidShippingBeanProductCount));
    }

    if(allAreCafeProduct) { // specificFeeProduct를 제외한 나머지 물품은 배송비는 1회만 부여
      deliveryFee = 3000;
    }

    if(!hasBeanQuantityOverThanOne) {
      deliveryFee = 3000;
    }

    if(onlySpecificFeeProduct && quantityBySpecificProduct >= 2) { // CA0001 제품은 건당으로 배송비 발생
      deliveryFee = deliveryFee + (3000 * (quantityBySpecificProduct - 1));
    } else {
      deliveryFee = deliveryFee + (3000 * (quantityBySpecificProduct));
    }

    if(paidShippingBeanProductCount >= 2 || hasBeanQuantityOverThanOne || sumPrice == 0 || allAreFreeShipping) { // 2키로 이상시 전체 배송비 무료, 카트에 담은게 없어도 배송비 x
      deliveryFee = 0;
    }

    return deliveryFee;
  }
}
