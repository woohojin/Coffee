package org.daCoffee.repository;

import org.daCoffee.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

  // 특정 제품의 이미지 목록 조회
  List<ProductImage> findByProduct_ProductCode(String productCode);

  // 특정 제품의 detail 이미지 조회
  Optional<ProductImage> findFirstByProduct_ProductCodeAndFileNameContaining(String productCode, String fileName);

}