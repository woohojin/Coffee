package org.daCoffee.service;

import lombok.RequiredArgsConstructor;
import org.daCoffee.entity.ProductImage;
import org.daCoffee.repository.ProductImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageService {

  private final ProductImageRepository productImageRepository;

  // 특정 제품의 이미지 목록 조회
  @Transactional(readOnly = true)
  public List<ProductImage> findByProductCode(String productCode) {
    return productImageRepository.findByProduct_ProductCode(productCode);
  }

  // detail 이미지 파일명 조회
  @Transactional(readOnly = true)
  public String findDetailImage(String productCode) {
    return productImageRepository
      .findFirstByProduct_ProductCodeAndFileNameContaining(productCode, "detail")
      .map(ProductImage::getFileName)
      .orElse(null);
  }

  // 이미지 저장
  @Transactional
  public void save(ProductImage productImage) {
    productImageRepository.save(productImage);
  }

  // 수정자 업데이트
  @Transactional
  public void updateModifier(int fileId, String fileName, String modifierName) {
    productImageRepository.findById(fileId).ifPresent(img ->
      img.updateModifier(fileName, modifierName));
  }
}
