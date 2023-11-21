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
    Integer memberTier = (Integer) session.getAttribute("memberTier");
    if(memberTier == null) {
      memberTier = 0;
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

    Integer memberTier = (Integer) session.getAttribute("memberTier");
    if(memberTier == null) {
      memberTier = 0;
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
    Integer memberTier = (Integer) session.getAttribute("memberTier");
    if(memberTier == null) {
      memberTier = 0;
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
    String[] array = {"productCode", "productName", "productType", "productPrice",
                      "productUnit", "productCountry", "productSpecies", "productCompany", "productTier"};

    if(memberTier == 9) {
      for(String param : array) {
        if(!request.getParameter(param).isEmpty()) {
          searchText = request.getParameter(param);
          request.setAttribute(param, searchText);
          productDao.productSet();
          switch(param) {
            case "productCode":
              list = productDao.productSearchListByProductCode(pageInt, limit, searchText);
              break;
            case "productName":
              list = productDao.productSearchListByProductName(pageInt, limit, searchText);
              break;
            case "productType":
              list = productDao.productSearchListByProductType(pageInt, limit, searchText);
              break;
            case "productPrice":
              list = productDao.productSearchListByProductPrice(pageInt, limit, searchText);
              break;
            case "productUnit":
              list = productDao.productSearchListByProductUnit(pageInt, limit, searchText);
              break;
            case "productCountry":
              list = productDao.productSearchListByProductCountry(pageInt, limit, searchText);
              break;
            case "productSpecies":
              list = productDao.productSearchListByProductSpecies(pageInt, limit, searchText);
              break;
            case "productCompany":
              list = productDao.productSearchListByProductCompany(pageInt, limit, searchText);
              break;
            case "productTier":
              list = productDao.productSearchListByProductTier(pageInt, limit, searchText);
              break;
          }
        }
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

  @RequestMapping("memberList")
  public String memberList() throws Exception {
    Integer memberTier = (Integer) session.getAttribute("memberTier");
    if(memberTier == null) {
      memberTier = 0;
    }

    String pageNum = request.getParameter("pageNum");
    if (pageNum == null) {
      pageNum = "1";
    }

    int pageInt = Integer.parseInt(pageNum);

    int limit = 32; // 한 page당 게시물 개수
    int bottomLine = 100; // pagination 개수

    int memberCount = 0;

    if(memberTier == 9) {
      memberDao.memberSet();
      List<Member> list = memberDao.memberList(pageInt, limit);
      memberCount = memberDao.memberCount();
      request.setAttribute("list", list);
      LOGGER.info(list.toString());
    }

    int start = (pageInt - 1) / bottomLine * bottomLine + 1;
    int end = start + bottomLine - 1;
    int maxPage = (memberCount / limit) + (memberCount % limit == 0 ? 0 : 1);
    if (end > maxPage) {
      end = maxPage;
    }
    if (end > memberCount) {
      end = memberCount;
    }

    request.setAttribute("memberTier", memberTier);
    request.setAttribute("memberCount", memberCount);
    request.setAttribute("pageNum", pageNum);
    request.setAttribute("start", start);
    request.setAttribute("end", end);
    request.setAttribute("bottomLine", bottomLine);
    request.setAttribute("maxPage", maxPage);
    request.setAttribute("pageInt", pageInt);

    return "admin/memberList";
  }

  @RequestMapping("memberListPro")
  public String memberListPro() throws Exception {
    String member = request.getParameter("member");
    String orderBy = request.getParameter("orderBy");

    Integer memberTier = (Integer) session.getAttribute("memberTier");
    if(memberTier == null) {
      memberTier = 0;
    }

    String pageNum = request.getParameter("pageNum");
    if (pageNum == null) {
      pageNum = "1";
    }

    int pageInt = Integer.parseInt(pageNum);

    int limit = 30; // 한 page당 게시물 개수
    int bottomLine = 100; // pagination 개수

    int memberCount = 0;

    if(memberTier == 9) {
      memberDao.memberSet();
      List<Member> list;

      switch (member) {
        case "member_company_name":
          list = ("desc".equals(orderBy)) ? memberDao.memberListDescByMemberCompanyName(pageInt, limit) :
            memberDao.memberListByMemberCompanyName(pageInt, limit);
          break;
        case "member_fran_code":
          list = ("desc".equals(orderBy)) ? memberDao.memberListDescByMemberFranCode(pageInt, limit) :
            memberDao.memberListByMemberFranCode(pageInt, limit);
          break;
        case "member_id":
          list = ("desc".equals(orderBy)) ? memberDao.memberListDescByMemberId(pageInt, limit) :
            memberDao.memberListByMemberId(pageInt, limit);
          break;
        case "member_name":
          list = ("desc".equals(orderBy)) ? memberDao.memberListDescByMemberName(pageInt, limit) :
            memberDao.memberListByMemberName(pageInt, limit);
          break;
        case "member_tel":
          list = ("desc".equals(orderBy)) ? memberDao.memberListDescByMemberTel(pageInt, limit) :
            memberDao.memberListByMemberTel(pageInt, limit);
          break;
        case "member_company_tel":
          list = ("desc".equals(orderBy)) ? memberDao.memberListDescByMemberCompanyTel(pageInt, limit) :
            memberDao.memberListByMemberCompanyTel(pageInt, limit);
          break;
        case "member_tier":
          list = ("desc".equals(orderBy)) ? memberDao.memberListDescByMemberTier(pageInt, limit) :
            memberDao.memberListByMemberTier(pageInt, limit);
          break;
        default:
          list = memberDao.memberListByMemberTier(pageInt, limit);
          break;
      }

      if(orderBy == "asc") {
        orderBy = "desc";
      } else if(orderBy == "desc") {
        orderBy = "asc";
      }

      memberCount = memberDao.memberCount();
      request.setAttribute("list", list);
    }

    int start = (pageInt - 1) / bottomLine * bottomLine + 1;
    int end = start + bottomLine - 1;
    int maxPage = (memberCount / limit) + (memberCount % limit == 0 ? 0 : 1);
    if (end > maxPage) {
      end = maxPage;
    }
    if (end > memberCount) {
      end = memberCount;
    }
    request.setAttribute("memberTier", memberTier);
    request.setAttribute("memberCount", memberCount);
    request.setAttribute("member", member);
    request.setAttribute("orderBy", orderBy);
    request.setAttribute("pageNum", pageNum);
    request.setAttribute("start", start);
    request.setAttribute("end", end);
    request.setAttribute("bottomLine", bottomLine);
    request.setAttribute("maxPage", maxPage);
    request.setAttribute("pageInt", pageInt);

    return "admin/memberList";
  }

  @RequestMapping("memberSearch")
  public String memberSearch() throws Exception {
    Integer memberTier = (Integer) session.getAttribute("memberTier");
    if(memberTier == null) {
      memberTier = 0;
    }

    String pageNum = request.getParameter("pageNum");
    if (pageNum == null) {
      pageNum = "1";
    }

    int pageInt = Integer.parseInt(pageNum);

    int limit = 32; // 한 page당 게시물 개수
    int bottomLine = 100; // pagination 개수

    int memberCount = 0;
    String searchText = "";
    List<Member> list = null;
    String[] array = {"memberCompanyName", "memberFranCode", "memberId", "memberName",
      "memberTel", "memberCompanyTel", "memberTier"};

    if(memberTier == 9) {
      for(String param : array) {
        if(!request.getParameter(param).isEmpty()) {
          searchText = request.getParameter(param);
          request.setAttribute(param, searchText);
          memberDao.memberSet();
          switch(param) {
            case "memberCompanyName":
              list = memberDao.memberSearchListByMemberCompanyName(pageInt, limit, searchText);
              break;
            case "memberFranCode":
              list = memberDao.memberSearchListByMemberFranCode(pageInt, limit, searchText);
              break;
            case "memberId":
              list = memberDao.memberSearchListByMemberId(pageInt, limit, searchText);
              break;
            case "memberName":
              list = memberDao.memberSearchListByMemberName(pageInt, limit, searchText);
              break;
            case "memberTel":
              list = memberDao.memberSearchListByMemberTel(pageInt, limit, searchText);
              break;
            case "memberCompanyTel":
              list = memberDao.memberSearchListByMemberCompanyTel(pageInt, limit, searchText);
              break;
            case "memberTier":
              list = memberDao.memberSearchListByMemberTier(pageInt, limit, searchText);
              break;
          }
        }
      }
      memberCount = memberDao.memberCount();
    }

    int start = (pageInt - 1) / bottomLine * bottomLine + 1;
    int end = start + bottomLine - 1;
    int maxPage = (memberCount / limit) + (memberCount % limit == 0 ? 0 : 1);
    if (end > maxPage) {
      end = maxPage;
    }
    if (end > memberCount) {
      end = memberCount;
    }

    request.setAttribute("memberTier", memberTier);
    request.setAttribute("memberCount", memberCount);
    request.setAttribute("list", list);
    request.setAttribute("pageNum", pageNum);
    request.setAttribute("start", start);
    request.setAttribute("end", end);
    request.setAttribute("bottomLine", bottomLine);
    request.setAttribute("maxPage", maxPage);
    request.setAttribute("pageInt", pageInt);

    return "admin/memberList";
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

  @RequestMapping("productUpload")
  public String productUpload() {
    return "admin/productUpload";
  }

  @RequestMapping("productUploadPro")
  public String productUploadPro(Product product) throws Exception {

    String msg = "제품 등록 실패";
    String url = "/admin/productUploadForm";

    int num = productDao.productInsert(product);

    if (num > 0) {
      msg = "제품을 등록하였습니다.";
    }

    request.setAttribute("msg", msg);
    request.setAttribute("url", url);

    return "alert";
  }

}
