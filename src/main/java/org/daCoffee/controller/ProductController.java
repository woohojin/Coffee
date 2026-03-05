package org.daCoffee.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dao.ProductDAO;
import org.daCoffee.dto.ProductDTO;
import org.daCoffee.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class ProductController {
  private final ProductDAO productDao;

  @GetMapping("productList")
  public String productList(Model model,
                        @RequestParam(value = "pageType", defaultValue = "bean") String pageType,
                        @SessionAttribute Integer memberTier) {

    if(memberTier == null) memberTier = 0;

    model.addAttribute("pageType", pageType);
    model.addAttribute("memberTier", memberTier);

    return "products/productList";
  }

  @GetMapping("beanDetail")
  public String beanDetail(Model model,
                           @RequestParam String productCode,
                           @SessionAttribute Integer memberTier) {

    model.addAttribute("memberTier", memberTier);
    model.addAttribute("productCode", productCode);
    model.addAttribute("pageType", "bean");

    return "products/beanDetail";
  }

  @GetMapping("mixDetail")
  public String mixDetail(Model model,
                          @RequestParam String productCode,
                          @SessionAttribute Integer memberTier) {

    model.addAttribute("memberTier", memberTier);
    model.addAttribute("productCode", productCode);
    model.addAttribute("pageType", "mix");

    return "products/mixDetail";
  }

  @GetMapping("cafeDetail")
  public String cafeDetail(Model model,
                           @RequestParam String productCode,
                           @SessionAttribute Integer memberTier) {

    model.addAttribute("memberTier", memberTier);
    model.addAttribute("productCode", productCode);
    model.addAttribute("pageType", "cafe");

    return "products/cafeDetail";
  }

  @RequestMapping("machineDetail")
  public String machineDetail() {
    return "products/machineDetail";
  }

  @RequestMapping("productSearch")
  public String productSearch(Model model,
                              @RequestParam(defaultValue = "1") int pageInt,
                              @RequestParam(required = false) String searchText,
                              @SessionAttribute Integer memberTier) {

    int productSearchCount = 0;

    if(memberTier != 0) {
      productDao.rownumSet();
      List<ProductDTO> list = productDao.productSearchListByMemberTier(pageInt, PaginationUtil.LIMIT, memberTier, searchText);
      productSearchCount = productDao.productSearchCountByTier(memberTier, searchText);
      model.addAttribute("list", list);
    }

    Map<String, Integer> paginationInfo = PaginationUtil.calculatePagination(pageInt, productSearchCount);
    int start = paginationInfo.get("start");
    int end = paginationInfo.get("end");

    model.addAttribute("searchText", searchText);
    model.addAttribute("memberTier", memberTier);
    model.addAttribute("productSearchCount", productSearchCount);
    model.addAttribute("pageInt", pageInt);
    model.addAttribute("start", start);
    model.addAttribute("end", end);

    return "products/productList";
  }
}
