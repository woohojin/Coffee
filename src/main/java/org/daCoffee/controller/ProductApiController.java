package org.daCoffee.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dao.ImageDAO;
import org.daCoffee.dao.ProductDAO;
import org.daCoffee.dto.ProductDTO;
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
  public ResponseEntity<Map<String, Object>> getProductList(
    @RequestParam(value = "pageType", defaultValue = "bean") String pageType,
    @RequestParam(defaultValue = "1") int pageInt,
    @SessionAttribute Integer memberTier) {

    if(memberTier == null) memberTier = 0;

    if (memberTier == 0) {
      // 로그인 안 된 경우 빈 리스트 반환
      Map<String, Object> response = new HashMap<>();
      response.put("list", List.of());
      response.put("productCount", 0);
      response.put("start", 1);
      response.put("end", 1);
      response.put("pageInt", pageInt);
      return ResponseEntity.ok(response);
    }

    int productType = switch (pageType) {
      case "bean" -> 0;
      case "mix" -> 1;
      case "cafe" -> {
        memberTier = 1; // 카페용품은 등급 1 고정
        yield 2;
      }
      default -> throw new IllegalArgumentException("Invalid pageType: " + pageType);
    };

    productDao.rownumSet();
    List<ProductDTO> list = productDao.productListByMemberTierByProductType(pageInt, LIMIT, memberTier, productType);
    int productCount = productDao.productCountByTierByProductType(memberTier, productType);

    Map<String, Integer> paginationInfo = calculatePagination(pageInt, productCount);
    int start = paginationInfo.get("start");
    int end = paginationInfo.get("end");

    Map<String, Object> response = new HashMap<>();
    response.put("list", list);
    response.put("productCount", productCount);
    response.put("start", start);
    response.put("end", end);
    response.put("pageInt", pageInt);
    response.put("pageType", pageType);
    response.put("memberTier", memberTier);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{productCode}")
  public ResponseEntity<Map<String, Object>> getProductDetail(@PathVariable("productCode") String productCode,
                                                              @SessionAttribute Integer memberTier) {
    if(memberTier == null) memberTier = 0;

    Map<String, Object> response = new HashMap<>();

    if(memberTier == 0) {
      response.put("memberTier", 0);
      return ResponseEntity.ok(response);
    }

    ProductDTO product = productDao.productSelectOne(productCode);
    if (product == null) {
      return ResponseEntity.notFound().build();
    }

    int productType = product.getProductType();

    String detailImageName = imageDao.selectDetailImage(productCode);
    int productCount = productDao.productCountByTierByProductType(memberTier, productType);

    response.put("memberTier", memberTier);
    response.put("productCount", productCount);
    response.put("product", product);
    response.put("detailImageName", detailImageName);

    return ResponseEntity.ok(response);
  }
}
