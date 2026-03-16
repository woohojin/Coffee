package org.daCoffee.repository;

import org.daCoffee.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, String> {

  // 페이징용 제품등급(회원과 일치) + 타입 목록 조회
  Page<Product> findByProductTierAndProductType(Integer productTier, Integer productType, Pageable pageable);

  // 제품등급 + 타입으로 제품 수 조회
  int countByProductTierAndProductType(Integer productTier, Integer productType);

  // 제품등급 + 검색어 조회
  Page<Product> findByProductTierAndProductNameContaining(Integer productTier, String keyword, Pageable pageable);

  @Modifying
  @Query("UPDATE Product p SET p.productSoldOut = :soldOut WHERE p.productCode = :productCode")
  void updateSoldOut(@Param("productCode") String productCode, @Param("soldOut") boolean soldOut);

  // ===================== Admin =====================

  // 제품 이름 검색
  Page<Product> findByProductNameContaining(String keyword, Pageable pageable);

  // 제품 코드 검색
  Page<Product> findByProductCodeContaining(String keyword, Pageable pageable);

  // 제품 타입 검색
  Page<Product> findByProductType(int productType, Pageable pageable);

  // 제품 가격 검색
  Page<Product> findByProductPrice(int productPrice, Pageable pageable);

  // 제품 단위 검색
  Page<Product> findByProductUnitContaining(String keyword, Pageable pageable);

  // 제품 등급 검색
  Page<Product> findByProductTier(int productTier, Pageable pageable);

  // 제품 품절 상태 검색
  Page<Product> findByProductSoldOut(boolean soldOut, Pageable pageable);
}
