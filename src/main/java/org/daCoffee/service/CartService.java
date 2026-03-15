package org.daCoffee.service;

import lombok.RequiredArgsConstructor;
import org.daCoffee.entity.Cart;
import org.daCoffee.entity.CartId;
import org.daCoffee.entity.Member;
import org.daCoffee.entity.Product;
import org.daCoffee.repository.CartRepository;
import org.daCoffee.repository.MemberRepository;
import org.daCoffee.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

  private final CartRepository cartRepository;
  private final MemberRepository memberRepository;
  private final ProductRepository productRepository;

  // 특정 회원의 장바구니 목록 조회
  @Transactional(readOnly = true)
  public List<Cart> getCartList(String memberId) {
    return cartRepository.findAllByMemberIdWithProduct(memberId);
  }

  // 특정 회원의 장바구니 단건 조회
  @Transactional(readOnly = true)
  public Optional<Cart> getCartItem(String memberId, String productCode) {
    return cartRepository.findById(new CartId(memberId, productCode));
  }

  // 특정 회원의 장바구니 상품 수 조회
  @Transactional(readOnly = true)
  public int getCartCount(String memberId) {
    return (int) cartRepository.countByMemberId(memberId);
  }

  // 특정 회원의 장바구니에 담긴 제품 코드 목록 조회
  @Transactional(readOnly = true)
  public List<String> getProductCodeList(String memberId) {
    return cartRepository.findProductCodesByMemberId(memberId);
  }

  // 특정 회원의 품절된 제품을 제외한 제품의 가격 총합
  @Transactional(readOnly = true)
  public int getSumPrice(String memberId) {
    return cartRepository.sumPriceByMemberId(memberId);
  }

  // 특정 회원의 장바구니에 특정 제품 종류가 담긴 갯수
  @Transactional(readOnly = true)
  public List<Integer> getQuantitiesByProductType(String memberId, int productType) {
    return cartRepository.findQuantitiesByMemberIdAndProductType(memberId, productType);
  }

  // 특정 회원의 제품 한개의 갯수
  @Transactional(readOnly = true)
  public int getQuantityByProductCode(String memberId, String productCode) {
    return cartRepository.findQuantityByMemberIdAndProductCode(memberId, productCode).orElse(0);
  }

  // 장바구니 추가 또는 수량 업데이트
  @Transactional
  public void addOrUpdate(String memberId, String productCode, int quantity) {
    CartId cartId = new CartId(memberId, productCode);
    Optional<Cart> existing = cartRepository.findById(cartId);

    if (existing.isPresent()) {
      existing.get().updateQuantity(existing.get().getQuantity() + quantity); // Dirty Checking
    } else {
      Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + memberId));
      Product product = productRepository.findById(productCode)
        .orElseThrow(() -> new IllegalArgumentException("상품 없음: " + productCode));

      Cart cart = Cart.builder()
        .id(cartId)
        .member(member)
        .product(product)
        .quantity(quantity)
        .build();
      cartRepository.save(cart);
    }
  }

  // 수량 변경 (increase/decrease)
  @Transactional
  public void updateQuantity(String memberId, String productCode, int delta) {
    Cart cart = cartRepository.findById(new CartId(memberId, productCode))
      .orElseThrow(() -> new IllegalArgumentException("장바구니 항목 없음"));

    int newQuantity = cart.getQuantity() + delta;
    if (newQuantity < 1) return;

    cart.updateQuantity(newQuantity); // Dirty Checking (자동 Update)
  }

  // 특정 회원의 장바구니 제품 단건 삭제
  @Transactional
  public void deleteCartItem(String memberId, String productCode) {
    cartRepository.deleteByMemberIdAndProductCode(memberId, productCode);
  }

  // 특정 회원의 장바구니 전체 삭제
  @Transactional
  public void deleteAllByMember(String memberId) {
    cartRepository.deleteAllByMemberId(memberId);
  }
}
