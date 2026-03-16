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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final BeanRepository beanRepository;
  private final MixRepository mixRepository;

  // 컬럼명 → Entity 필드명 변환
  private String resolveColumn(String column) {
    return switch (column) {
      case "product_type"     -> "productType";
      case "product_code"     -> "productCode";
      case "product_name"     -> "productName";
      case "product_unit"     -> "productUnit";
      case "product_price"    -> "productPrice";
      case "product_tier"     -> "productTier";
      case "product_sold_out" -> "productSoldOut";
      default                 -> "productCode";
    };
  }

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

  // 제품 검색 (검색어, 회원 등급)
  @Transactional(readOnly = true)
  public Page<Product> searchByName(String keyword, int memberTier, int pageInt, int limit) {
    PageRequest pageable = PageRequest.of(pageInt - 1, limit);
    return productRepository.findByProductTierAndProductNameContaining(memberTier, keyword, pageable);
  }

  // ===================== Admin =====================

  // 제품 삭제
  @Transactional
  public void deleteProduct(String productCode) {
    beanRepository.deleteById(productCode);
    mixRepository.deleteById(productCode);
    productRepository.deleteById(productCode);
  }

  // 제품 품절 상태 업데이트
  @Transactional
  public void updateSoldOut(String productCode, boolean soldOut) {
    productRepository.updateSoldOut(productCode, soldOut);
  }

  // Product 저장
  @Transactional
  public void saveProduct(Product product) {
    productRepository.save(product);
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

  // Product 수정
  @Transactional
  public void updateProduct(Product product) {
    productRepository.save(product);
  }

  // Bean 수정
  @Transactional
  public void updateBean(Bean bean) {
    beanRepository.save(bean);
  }

  // Mix 수정
  @Transactional
  public void updateMix(Mix mix) {
    mixRepository.save(mix);
  }

  // 전체 제품 목록 페이징 + 정렬
  @Transactional(readOnly = true)
  public Page<Product> findAllPaged(int pageInt, int limit, String column, String order) {
    Sort sort = Sort.by(
      "desc".equals(order) ? Sort.Direction.DESC : Sort.Direction.ASC,
      resolveColumn(column)
    );
    PageRequest pageable = PageRequest.of(pageInt - 1, limit, sort);
    return productRepository.findAll(pageable);
  }

  // 제품 검색 (컬럼 + 키워드)
  @Transactional(readOnly = true)
  public Page<Product> searchProducts(String column, String keyword, int pageInt, int limit) {
    PageRequest pageable = PageRequest.of(pageInt - 1, limit);
    return switch (column) {
      case "product_name"     -> productRepository.findByProductNameContaining(keyword, pageable);
      case "product_code"     -> productRepository.findByProductCodeContaining(keyword, pageable);
      case "product_unit"     -> productRepository.findByProductUnitContaining(keyword, pageable);
      case "product_type"     -> productRepository.findByProductType(Integer.parseInt(keyword), pageable);
      case "product_price"    -> productRepository.findByProductPrice(Integer.parseInt(keyword), pageable);
      case "product_tier"     -> productRepository.findByProductTier(Integer.parseInt(keyword), pageable);
      case "product_sold_out" -> productRepository.findByProductSoldOut("1".equals(keyword), pageable);
      default                 -> productRepository.findAll(pageable);
    };
  }
}
