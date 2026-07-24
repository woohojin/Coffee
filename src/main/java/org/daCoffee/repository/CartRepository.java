package org.daCoffee.repository;

import org.daCoffee.entity.Cart;
import org.daCoffee.entity.CartId;
import org.daCoffee.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, CartId> {

  // 특정 회원의 장바구니 목록 조회
  @Query("SELECT c FROM Cart c JOIN FETCH c.product WHERE c.id.memberId = :memberId")
  List<Cart> findAllByMemberIdWithProduct(@Param("memberId") String memberId);

  // 특정 회원의 장바구니 상품 수 조회
  @Query("SELECT COUNT(c) FROM Cart c WHERE c.id.memberId = :memberId")
  long countByMemberId(@Param("memberId") String memberId);

  // 특정 회원의 장바구니에 담긴 제품 코드 목록
  @Query("SELECT c.id.productCode FROM Cart c WHERE c.id.memberId = :memberId")
  List<String> findProductCodesByMemberId(@Param("memberId") String memberId);

  // 특정 회원의 품절된 제품을 제외한 제품의 가격 총합
  @Query("SELECT COALESCE(SUM(c.quantity * c.product.productPrice), 0) FROM Cart c WHERE c.id.memberId = :memberId AND c.product.productSoldOut = false")
  int sumPriceByMemberId(@Param("memberId") String memberId);

  // 특정 회원의 제품 한개의 갯수
  @Query("SELECT c.quantity FROM Cart c WHERE c.id.memberId = :memberId AND c.id.productCode = :productCode")
  Optional<Integer> findQuantityByMemberIdAndProductCode(@Param("memberId") String memberId, @Param("productCode") String productCode);

  // 특정 회원의 장바구니에 특정 제품 종류가 담긴 갯수
  @Query("SELECT c.quantity FROM Cart c WHERE c.id.memberId = :memberId AND c.product.productType = :productType")
  List<Integer> findQuantitiesByMemberIdAndProductType(@Param("memberId") String memberId, @Param("productType") ProductType productType);

  // 특정 회원의 장바구니 전체 삭제
  @Modifying
  @Query("DELETE FROM Cart c WHERE c.id.memberId = :memberId")
  void deleteAllByMemberId(@Param("memberId") String memberId);

  // 특정 회원의 장바구니 제품 단건 삭제
  @Modifying
  @Query("DELETE FROM Cart c WHERE c.id.memberId = :memberId AND c.id.productCode = :productCode")
  void deleteByMemberIdAndProductCode(@Param("memberId") String memberId, @Param("productCode") String productCode);

}