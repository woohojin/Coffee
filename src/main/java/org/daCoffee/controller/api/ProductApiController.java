package org.daCoffee.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dao.ImageDAO;
import org.daCoffee.dao.ProductDAO;
import org.daCoffee.dto.ApiResponseDTO;
import org.daCoffee.dto.ProductDTO;
import org.daCoffee.exception.BusinessException;
import org.daCoffee.exception.NotFoundException;
import org.daCoffee.exception.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductApiController {
  private final ProductDAO productDao;
  private final ImageDAO imageDao;
  private static final int LIMIT = 15;
  private static final int BOTTOM_LINE = 100;

  private Map<String, Integer> calculatePagination(int pageInt, int count) {
    Map<String, Integer> paginationInfo = new HashMap<>();

    int start = (pageInt - 1) / BOTTOM_LINE * BOTTOM_LINE + 1;
    int end = start + BOTTOM_LINE - 1;
    int maxPage = (count / LIMIT) + (count % LIMIT == 0 ? 0 : 1);

    if (end > maxPage) end = maxPage;
    if (end > count) end = count;

    paginationInfo.put("start", start);
    paginationInfo.put("end", end);

    return paginationInfo;
  }

  @GetMapping
  public ResponseEntity<ApiResponseDTO<Map<String, Object>>> getProductList(
    @RequestParam(value = "pageType", defaultValue = "bean") String pageType,
    @RequestParam(defaultValue = "1") int pageInt,
    @SessionAttribute(required = false) Integer memberTier) {

    if(memberTier == null) memberTier = 0;

    // 직접 memberTier를 체크해서 예외처리가 이루어지기 때문에 파라미터에서는 required가 false
    if(memberTier == 0) {
      throw new UnauthorizedException();
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
    List<ProductDTO> list = productDao.productListByMemberTierByProductType(pageInt, LIMIT, memberTier, productType);
    int productCount = productDao.productCountByTierByProductType(memberTier, productType);

    Map<String, Integer> paginationInfo = calculatePagination(pageInt, productCount);
    int start = paginationInfo.get("start");
    int end = paginationInfo.get("end");

    Map<String, Object> data = new HashMap<>();

    data.put("list", list);
    data.put("productCount", productCount);
    data.put("start", start);
    data.put("end", end);
    data.put("pageInt", pageInt);
    data.put("pageType", pageType);
    data.put("memberTier", memberTier);

    return ResponseEntity.ok(ApiResponseDTO.success(data));
  }

  @GetMapping("/{productCode}")
  public ResponseEntity<ApiResponseDTO<Map<String, Object>>> getProductDetail(@PathVariable("productCode") String productCode,
                                                              @SessionAttribute(required = false) Integer memberTier,
                                                              @RequestParam(defaultValue = "bean") String pageType) {
    if(memberTier == null) memberTier = 0;

    if(memberTier == 0) {
      throw new UnauthorizedException();
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

    Map<String, Object> data = new HashMap<>();

    data.put("memberTier", memberTier);
    data.put("productCount", productCount);
    data.put("product", product);
    data.put("detailImageName", detailImageName);

    return ResponseEntity.ok(ApiResponseDTO.success(data));
  }
}
