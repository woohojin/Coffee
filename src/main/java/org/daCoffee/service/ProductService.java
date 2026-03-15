package org.daCoffee.service;

import lombok.RequiredArgsConstructor;
import org.daCoffee.entity.Bean;
import org.daCoffee.entity.Mix;
import org.daCoffee.entity.Product;
import org.daCoffee.repository.BeanRepository;
import org.daCoffee.repository.MixRepository;
import org.daCoffee.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final BeanRepository beanRepository;
  private final MixRepository mixRepository;

  // 제품 단건 조회
  @Transactional(readOnly = true)
  public Optional<Product> findById(String productCode) {
    return productRepository.findById(productCode);
  }


  // Bean 단건 조회 (product + bean join)
  @Transactional(readOnly = true)
  public Optional<Bean> findBeanById(String productCode) {
    return beanRepository.findById(productCode);
  }

  // Mix 단건 조회 (product + mix join)
  @Transactional(readOnly = true)
  public Optional<Mix> findMixById(String productCode) {
    return mixRepository.findById(productCode);
  }

  // 페이징용 제품등급(회원과 일치) + 타입 목록 조회
  @Transactional(readOnly = true)
  public Page<Product> getProductList(int memberTier, int productType, int pageInt, int limit) {
    PageRequest pageable = PageRequest.of(pageInt - 1, limit);
    return productRepository.findByProductTierAndProductType(memberTier, productType, pageable);
  }

  // 제품등급 + 타입으로 제품 수 조회
  @Transactional(readOnly = true)
  public int countByTierAndType(int memberTier, int productType) {
    return productRepository.countByProductTierAndProductType(memberTier, productType);
  }

  // Bean 저장
  @Transactional
  public void saveBean(Bean bean) {
    beanRepository.save(bean);
  }

  // Mix 저장
  @Transactional
  public void saveMix(Mix mix) {
    mixRepository.save(mix);
  }
  // 제품 품절 상태 업데이트
  @Transactional
  public void updateSoldOut(String productCode, boolean soldOut) {
    productRepository.updateSoldOut(productCode, soldOut);
  }

  // 제품 삭제 (product 삭제 시 cascade로 bean/mix도 삭제됨)
  @Transactional
  public void deleteProduct(String productCode) {
    productRepository.deleteById(productCode);
  }

  // 제품 검색 (이름 기준, 페이징)
  @Transactional(readOnly = true)
  public Page<Product> searchByName(String keyword, int pageInt, int limit) {
    PageRequest pageable = PageRequest.of(pageInt - 1, limit);
    return productRepository.findByProductNameContaining(keyword, pageable);
  }

}
