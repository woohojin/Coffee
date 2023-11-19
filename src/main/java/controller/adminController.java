package controller;

import model.Member;
import model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.cartDAO;
import service.cookieDAO;
import service.memberDAO;
import service.productDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin/")
public class adminController {

  @Autowired
  productDAO productDao;

  @Autowired
  memberDAO memberDao;

  @Autowired
  cartDAO cartDao;

  @Autowired
  cookieDAO cookieDao;

  HttpServletRequest request;
  HttpServletResponse response;
  Model m;
  HttpSession session;

  @ModelAttribute
  void init(HttpServletRequest request, Model m, HttpServletResponse response) {
    this.request = request;
    this.m = m;
    this.session = request.getSession();
    this.response = response;
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(adminController.class);

  @RequestMapping("dashboard")
  public String index() throws Exception {

    return "admin/dashboard";
  }

  @RequestMapping("productList")
  public String productList() throws Exception {

    String memberId = (String)session.getAttribute("memberId");
    int memberTier = 0;

    if(memberId != null && !memberId.isEmpty()) {
      Member mem = memberDao.memberSelectOne(memberId);
      memberTier = mem.getMemberTier();
    }

    String pageNum = request.getParameter("pageNum");
    if (pageNum == null) {
      pageNum = "1";
    }

    int pageInt = Integer.parseInt(pageNum);

    int limit = 32; // 한 page당 게시물 개수
    int bottomLine = 100; // pagination 개수

    int productCount = 0;

    if(memberTier == 9) {
      productDao.productSet();
      List<Product> list = productDao.productList(pageInt, limit);
      productCount = productDao.productCount();
      request.setAttribute("list", list);
    }

    int start = (pageInt - 1) / bottomLine * bottomLine + 1;
    int end = start + bottomLine - 1;
    int maxPage = (productCount / limit) + (productCount % limit == 0 ? 0 : 1);
    if (end > maxPage) {
      end = maxPage;
    }
    if (end > productCount) {
      end = productCount;
    }

    request.setAttribute("memberTier", memberTier);
    request.setAttribute("productCount", productCount);
    request.setAttribute("pageNum", pageNum);
    request.setAttribute("start", start);
    request.setAttribute("end", end);
    request.setAttribute("bottomLine", bottomLine);
    request.setAttribute("maxPage", maxPage);
    request.setAttribute("pageInt", pageInt);

    return "admin/productList";
  }

  @RequestMapping("productListPro")
  public String productListPro() throws Exception {
    String product = request.getParameter("product");
    String orderBy = request.getParameter("orderBy");

    String memberId = (String)session.getAttribute("memberId");
    int memberTier = 0;

    if(memberId != null && !memberId.isEmpty()) {
      Member mem = memberDao.memberSelectOne(memberId);
      memberTier = mem.getMemberTier();
    }

    String pageNum = request.getParameter("pageNum");
    if (pageNum == null) {
      pageNum = "1";
    }

    int pageInt = Integer.parseInt(pageNum);

    int limit = 30; // 한 page당 게시물 개수
    int bottomLine = 100; // pagination 개수

    int productCount = 0;

    if(memberTier == 9) {
      productDao.productSet();
      List<Product> list;

      switch (product) {
        case "product_type":
          list = ("desc".equals(orderBy)) ? productDao.productListDescByProductType(pageInt, limit) :
            productDao.productListByProductType(pageInt, limit);
          break;
        case "product_code":
          list = ("desc".equals(orderBy)) ? productDao.productListDescByProductCode(pageInt, limit) :
            productDao.productListByProductCode(pageInt, limit);
          break;
        case "product_name":
          list = ("desc".equals(orderBy)) ? productDao.productListDescByProductName(pageInt, limit) :
            productDao.productListByProductName(pageInt, limit);
          break;
        case "product_unit":
          list = ("desc".equals(orderBy)) ? productDao.productListDescByProductUnit(pageInt, limit) :
            productDao.productListByProductUnit(pageInt, limit);
          break;
        case "product_price":
          list = ("desc".equals(orderBy)) ? productDao.productListDescByProductPrice(pageInt, limit) :
            productDao.productListByProductPrice(pageInt, limit);
          break;
        case "product_country":
          list = ("desc".equals(orderBy)) ? productDao.productListDescByProductCountry(pageInt, limit) :
            productDao.productListByProductCountry(pageInt, limit);
          break;
        case "product_species":
          list = ("desc".equals(orderBy)) ? productDao.productListDescByProductSpecies(pageInt, limit) :
            productDao.productListByProductSpecies(pageInt, limit);
          break;
        case "product_company":
          list = ("desc".equals(orderBy)) ? productDao.productListDescByProductCompany(pageInt, limit) :
            productDao.productListByProductCompany(pageInt, limit);
          break;
        case "product_tier":
          list = ("desc".equals(orderBy)) ? productDao.productListDescByProductTier(pageInt, limit) :
            productDao.productListByProductTier(pageInt, limit);
          break;
        default:
          list = productDao.productListByProductType(pageInt, limit);
          break;
      }

      if(orderBy == "asc") {
        orderBy = "desc";
      } else if(orderBy == "desc") {
        orderBy = "asc";
      }

      productCount = productDao.productCount();
      request.setAttribute("list", list);
    }

    int start = (pageInt - 1) / bottomLine * bottomLine + 1;
    int end = start + bottomLine - 1;
    int maxPage = (productCount / limit) + (productCount % limit == 0 ? 0 : 1);
    if (end > maxPage) {
      end = maxPage;
    }
    if (end > productCount) {
      end = productCount;
    }
    request.setAttribute("memberTier", memberTier);
    request.setAttribute("productCount", productCount);
    request.setAttribute("product", product);
    request.setAttribute("orderBy", orderBy);
    request.setAttribute("pageNum", pageNum);
    request.setAttribute("start", start);
    request.setAttribute("end", end);
    request.setAttribute("bottomLine", bottomLine);
    request.setAttribute("maxPage", maxPage);
    request.setAttribute("pageInt", pageInt);

    return "admin/productList";
  }

  @RequestMapping("productSearch")
  public String productSearch() throws Exception {
    String memberId = (String)session.getAttribute("memberId");
    int memberTier = 0;

    if(memberId != null && !memberId.isEmpty()) {
      Member mem = memberDao.memberSelectOne(memberId);
      memberTier = mem.getMemberTier();
    }

    String pageNum = request.getParameter("pageNum");
    if (pageNum == null) {
      pageNum = "1";
    }

    int pageInt = Integer.parseInt(pageNum);

    int limit = 32; // 한 page당 게시물 개수
    int bottomLine = 100; // pagination 개수

    int productCount = 0;
    String searchText = "";
    List<Product> list = null;

    if(memberTier == 9) {
      LOGGER.info(request.getParameter("productCode"));
      if(!request.getParameter("productCode").isEmpty()) {
        searchText = request.getParameter("productCode");
        request.setAttribute("productCode", searchText);
        productDao.productSet();
        list = productDao.productSearchListByProductCode(pageInt, limit, searchText);
      } else if (!request.getParameter("productName").isEmpty()) {
        searchText = request.getParameter("productName");
        request.setAttribute("productName", searchText);
        productDao.productSet();
        list = productDao.productSearchListByProductName(pageInt, limit, searchText);
      } else if (!request.getParameter("productType").isEmpty()) {
        searchText = request.getParameter("productType");
        request.setAttribute("productType", searchText);
        productDao.productSet();
        list = productDao.productSearchListByProductType(pageInt, limit, searchText);
      } else if (!request.getParameter("productPrice").isEmpty()) {
        searchText = request.getParameter("productPrice");
        request.setAttribute("productPrice", searchText);
        productDao.productSet();
        list = productDao.productSearchListByProductPrice(pageInt, limit, searchText);
      } else if (!request.getParameter("productUnit").isEmpty()) {
        searchText = request.getParameter("productUnit");
        request.setAttribute("productUnit", searchText);
        productDao.productSet();
        list = productDao.productSearchListByProductUnit(pageInt, limit, searchText);
      } else if (!request.getParameter("productCountry").isEmpty()) {
        searchText = request.getParameter("productCountry");
        request.setAttribute("productCountry", searchText);
        productDao.productSet();
        list = productDao.productSearchListByProductCountry(pageInt, limit, searchText);
      } else if (!request.getParameter("productSpecies").isEmpty()) {
        searchText = request.getParameter("productSpecies");
        request.setAttribute("productSpecies", searchText);
        productDao.productSet();
        list = productDao.productSearchListByProductSpecies(pageInt, limit, searchText);
      } else if (!request.getParameter("productCompany").isEmpty()) {
        searchText = request.getParameter("productCompany");
        request.setAttribute("productCompany", searchText);
        productDao.productSet();
        list = productDao.productSearchListByProductCompany(pageInt, limit, searchText);
      } else if (!request.getParameter("productTier").isEmpty()) {
        searchText = request.getParameter("productTier");
        request.setAttribute("productTier", searchText);
        productDao.productSet();
        list = productDao.productSearchListByProductTier(pageInt, limit, searchText);
      }
      productCount = productDao.productCount();
    }

    int start = (pageInt - 1) / bottomLine * bottomLine + 1;
    int end = start + bottomLine - 1;
    int maxPage = (productCount / limit) + (productCount % limit == 0 ? 0 : 1);
    if (end > maxPage) {
      end = maxPage;
    }
    if (end > productCount) {
      end = productCount;
    }

    request.setAttribute("memberTier", memberTier);
    request.setAttribute("productCount", productCount);
    request.setAttribute("list", list);
    request.setAttribute("pageNum", pageNum);
    request.setAttribute("start", start);
    request.setAttribute("end", end);
    request.setAttribute("bottomLine", bottomLine);
    request.setAttribute("maxPage", maxPage);
    request.setAttribute("pageInt", pageInt);

    return "admin/productList";
  }

  @RequestMapping("memberTierUpdate")
  public String memberTierUpdate() throws Exception {

    return "admin/memberTierUpdate";
  }

  @RequestMapping("memberTierUpdatePro")
  public String memberTierUpdatePro(String memberId, int memberTier) throws Exception {

    memberDao.memberTierUpdate(memberId, memberTier);

    String url = "/board/main";

    request.setAttribute("url", url);
    return "anchor";
  }
}
