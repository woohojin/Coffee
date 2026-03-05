package org.daCoffee.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dao.ImageDAO;
import org.daCoffee.dao.ProductDAO;
import org.daCoffee.dto.ApiResponseDTO;
import org.daCoffee.dto.ProductDTO;
import org.daCoffee.dto.ProductDetailDataDTO;
import org.daCoffee.dto.ProductListDataDTO;
import org.daCoffee.exception.BusinessException;
import org.daCoffee.exception.NotFoundException;
import org.daCoffee.exception.UnauthorizedException;
import org.daCoffee.util.PaginationUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductApiController {
  private final ProductDAO productDao;
  private final ImageDAO imageDao;

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

    productDao.rownumSet();
    List<ProductDTO> list = productDao.productListByMemberTierByProductType(pageInt, PaginationUtil.LIMIT, memberTier, productType);
    int productCount = productDao.productCountByTierByProductType(memberTier, productType);

    Map<String, Integer> paginationInfo = PaginationUtil.calculatePagination(pageInt, productCount);

    ProductListDataDTO data = ProductListDataDTO.builder()
      .list(list)
      .productCount(productCount)
      .start(paginationInfo.get("start"))
      .end(paginationInfo.get("end"))
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

    ProductDTO product = switch (pageType) {
      case "mix" -> productDao.mixSelectOne(productCode);
      case "cafe" -> productDao.productSelectOne(productCode); // cafe는 product 테이블에 필요 상품 데이터가 다 들어있음
      default -> productDao.beanSelectOne(productCode); // bean
    };

    if (product == null) {
      throw new NotFoundException("상품을 찾을 수 없습니다.");
    }

    String detailImageName = imageDao.selectDetailImage(productCode);
    int productCount = productDao.productCountByTierByProductType(memberTier, productType);

    ProductDetailDataDTO data = ProductDetailDataDTO.builder()
      .memberTier(memberTier)
      .productCount(productCount)
      .product(product)
      .detailImageName(detailImageName)
      .build();

    return ResponseEntity.ok(ApiResponseDTO.success(data));
  }
}
