package org.daCoffee.repository;

import org.daCoffee.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {

  // 페이징용 제품등급(회원과 일치) + 타입 목록 조회
  Page<Product> findByProductTierAndProductType(Integer productTier, Integer productType, Pageable pageable);

  // 제품등급 + 타입으로 제품 수 조회
  int countByProductTierAndProductType(Integer productTier, Integer productType);

  // 제품등급 + 검색어 조회
  Page<Product> findByProductTierAndProductNameContaining(Integer productTier, String keyword, Pageable pageable);

  @Modifying
  @Query("UPDATE Product p SET p.productSoldOut = :soldOut WHERE p.productCode = :productCode")
  void updateSoldOut(@Param("productCode") String productCode, @Param("soldOut") boolean soldOut);
}
