package org.daCoffee.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dao.CartDAO;
import org.daCoffee.dao.ImageDAO;
import org.daCoffee.dao.ProductDAO;
import org.daCoffee.dto.CartDTO;
import org.daCoffee.dto.ProductDTO;
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
  private final CartDAO cartDao;

  int limit = 15; // 한 page당 게시물 개수
  int bottomLine = 100; // pagination 개수

  // 제품 페이지 페이지네이션 계산 함수
  public Map<String, Integer> calculatePagination(int pageInt, int count) {
    Map<String, Integer> paginationInfo = new HashMap<>();

    int start = (pageInt - 1) / bottomLine * bottomLine + 1;
    int end = start + bottomLine - 1;
    int maxPage = (count / limit) + (count % limit == 0 ? 0 : 1);

    if (end > maxPage) {
      end = maxPage;
    }
    if (end > count) {
      end = count;
    }

    paginationInfo.put("start", start);
    paginationInfo.put("end", end);

    return paginationInfo;
  }

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

    if(memberTier == null) memberTier = 0;

    model.addAttribute("memberTier", memberTier);
    model.addAttribute("productCode", productCode);
    model.addAttribute("pageType", "bean");

    return "products/beanDetail";
  }

  @RequestMapping("mixDetail")
  public String mixDetail(Model model,
                          @RequestParam String productCode,
                          @SessionAttribute Integer memberTier) {

    if(memberTier == null) memberTier = 0;

    model.addAttribute("memberTier", memberTier);
    model.addAttribute("productCode", productCode);
    model.addAttribute("pageType", "mix");

    return "products/mixDetail";
  }

  @RequestMapping("cafeDetail")
  public String cafeDetail(Model model,
                           @RequestParam String productCode,
                           @SessionAttribute Integer memberTier) {

    if(memberTier == null) memberTier = 0;

    model.addAttribute("memberTier", memberTier);
    model.addAttribute("productCode", productCode);
    model.addAttribute("pageType", "cafe");

    return "products/cafeDetail";
  }

  @RequestMapping("machineDetail")
  public String machineDetail() {
    return "products/machineDetail";
  }

  @PostMapping("productDetailPro")
  @ResponseBody
  public Map<String, Object> productDetailPro(HttpSession session, CartDTO cartDTO,
                                              @RequestParam(value = "additionalProducts", required = false) List<String> additionalProductsCodes) {
    Map<String, Object> map = new HashMap<>();

    String msg = "장바구니 추가 실패";
    String memberId = (String) session.getAttribute("memberId");

    //추가 상품 부분

    if (additionalProductsCodes != null && !additionalProductsCodes.isEmpty()) {
      for (String additionalProductsCode : additionalProductsCodes) {
        if(!additionalProductsCode.equals("none")) {
          CartDTO cartDTOCheck = cartDao.cartSelectOne(memberId, additionalProductsCode);

          if(cartDTOCheck == null) {
            CartDTO additionalCartDTO = new CartDTO();
            additionalCartDTO.setProductCode(additionalProductsCode);
            additionalCartDTO.setMemberId(memberId);
            additionalCartDTO.setQuantity(1);
            cartDao.cartInsert(additionalCartDTO);
          } else {
            cartDao.cartQuantityUpdate(memberId, additionalProductsCode, 1);
          }
        }
      }
    }

    //일반 상품 부분

    int quantity = cartDTO.getQuantity();

    if(quantity < 1) {
      quantity = 1;
      cartDTO.setQuantity(quantity);
    }

    String productCode = cartDTO.getProductCode();

    ProductDTO productDTO = productDao.productSelectOne(productCode);

    CartDTO cartDTOCheck = cartDao.cartSelectOne(memberId, productCode);
    if(cartDTOCheck == null) {
      cartDTO.setMemberId(memberId);
      int num = cartDao.cartInsert(cartDTO);

      if(num > 0) {
        msg = "장바구니 추가 성공";
        map.put("productCode", productDTO.getProductCode());
        map.put("productType", productDTO.getProductType());
        map.put("productName", productDTO.getProductName());
        map.put("productUnit", productDTO.getProductUnit());
//                map.put("productGrinding", cart.getProductGrinding());
        map.put("quantity", cartDTO.getQuantity());
        map.put("productPrice", productDTO.getProductPrice());
        map.put("productFile", productDTO.getProductFile());
      }
    } else {
      cartDao.cartQuantityUpdate(memberId, productCode, cartDTO.getQuantity());
      msg = "장바구니 추가 성공";
      map.put("productCode", productDTO.getProductCode());
      map.put("productType", productDTO.getProductType());
      map.put("productName", productDTO.getProductName());
      map.put("productUnit", productDTO.getProductUnit());
//            map.put("productGrinding", product.getProductGrinding());
      map.put("quantity", cartDTO.getQuantity());
      map.put("productPrice", productDTO.getProductPrice());
      map.put("productFile", productDTO.getProductFile());
    }

    map.put("cartStatus", msg);
    return map;
  }

  @RequestMapping("productSearch")
  public String productSearch(HttpServletRequest request, HttpSession session, Model model,
                              @RequestParam(defaultValue = "1") int pageInt,
                              @SessionAttribute Integer memberTier) {

    int limit = 4; // 한 page당 게시물 개수

    String searchText = request.getParameter("searchText");

    int productSearchCount = 0;

    if(memberTier == null) memberTier = 0;

    if(memberTier != 0) {
      productDao.rownumSet();
      List<ProductDTO> list = productDao.productSearchListByMemberTier(pageInt, limit, memberTier, searchText);
      productSearchCount = productDao.productSearchCountByTier(memberTier, searchText);
      model.addAttribute("list", list);
    }

    Map<String, Integer> paginationInfo = calculatePagination(pageInt, productSearchCount);
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
