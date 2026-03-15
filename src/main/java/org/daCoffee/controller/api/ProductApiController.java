package org.daCoffee.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dto.ApiResponseDTO;
import org.daCoffee.dto.ProductDTO;
import org.daCoffee.dto.response.ProductDetailDataDTO;
import org.daCoffee.dto.response.ProductListDataDTO;
import org.daCoffee.entity.Bean;
import org.daCoffee.entity.Mix;
import org.daCoffee.entity.Product;
import org.daCoffee.exception.BusinessException;
import org.daCoffee.exception.NotFoundException;
import org.daCoffee.exception.UnauthorizedException;
import org.daCoffee.service.ProductImageService;
import org.daCoffee.service.ProductService;
import org.daCoffee.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductApiController {
  private final ProductService productService;
  private final ProductImageService productImageService;

  @GetMapping
  public ResponseEntity<ApiResponseDTO<ProductListDataDTO>> getProductList(
    @RequestParam(value = "pageType", defaultValue = "bean") String pageType,
    @RequestParam(defaultValue = "1") int pageInt,
    @SessionAttribute(required = false) Integer memberTier) {

    // 직접 memberTier를 체크해서 예외처리가 이루어지기 때문에 파라미터에서는 required가 false
    if (memberTier == 0) {
      throw new UnauthorizedException("회원가입 진행 후 1566-0904로 연락 부탁드립니다.");
    }

    int productType = switch (pageType) {
      case "bean" -> 0;
      case "mix" -> 1;
      case "cafe" -> {
        memberTier = 1; // 카페용품은 등급 1 고정
        yield 2;
      }
      default -> throw new BusinessException("잘못된 상품 타입입니다.");
    };

    Page<Product> result = productService.getProductList(memberTier, productType, pageInt, PaginationUtil.LIMIT);

    ProductListDataDTO data = ProductListDataDTO.builder()
      .list(result.getContent())
      .productCount((int) result.getTotalElements())
      .totalPages(result.getTotalPages())
      .pageInt(pageInt)
      .pageType(pageType)
      .memberTier(memberTier)
      .build();

    return ResponseEntity.ok(ApiResponseDTO.success(data));
  }

  @GetMapping("/{productCode}")
  public ResponseEntity<ApiResponseDTO<ProductDetailDataDTO>> getProductDetail(@PathVariable("productCode") String productCode,
                                                                               @SessionAttribute(required = false) Integer memberTier,
                                                                               @RequestParam(defaultValue = "bean") String pageType) {

    if (memberTier == 0) {
      throw new UnauthorizedException("회원가입 진행 후 1566-0904로 연락 부탁드립니다.");
    }

    int productType = switch (pageType) {
      case "mix" -> 1;
      case "cafe" -> 2;
      default -> 0; // bean
    };

    Product product = productService.findById(productCode)
      .orElseThrow(() -> new NotFoundException("상품을 찾을 수 없습니다."));

    Bean bean = null;
    Mix mix = null;

    if (productType == 0) {
      bean = productService.findBeanById(productCode).orElse(null);
    } else if (productType == 1) {
      mix = productService.findMixById(productCode).orElse(null);
    }

    String detailImageName = productImageService.findDetailImage(productCode);
    int productCount = productService.countByTierAndType(memberTier, productType);

    ProductDetailDataDTO data = ProductDetailDataDTO.builder()
      .memberTier(memberTier)
      .productCount(productCount)
      .product(product)
      .bean(bean)
      .mix(mix)
      .detailImageName(detailImageName)
      .build();

    return ResponseEntity.ok(ApiResponseDTO.success(data));
  }
}
