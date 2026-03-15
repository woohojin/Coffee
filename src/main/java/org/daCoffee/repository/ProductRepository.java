package org.daCoffee.repository;

import org.daCoffee.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

  // 페이징용 제품등급(회원과 일치) + 타입 목록 조회
  Page<Product> findByProductTierAndProductType(Integer productTier, Integer productType, Pageable pageable);

  // 제품등급 + 타입으로 제품 수 조회
  int countByProductTierAndProductType(Integer productTier, Integer productType);

  @Modifying
  @Query("UPDATE Product p SET p.productSoldOut = :soldOut WHERE p.productCode = :productCode")
  void updateSoldOut(@Param("productCode") String productCode, @Param("soldOut") boolean soldOut);

  // 제품 이름으로 검색
  Page<Product> findByProductNameContaining(String keyword, Pageable pageable);

}
