package controller;

import model.Image;
import model.Member;
import model.Product;
import model.History;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import service.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
  historyDAO historyDao;

  @Autowired
  cookieDAO cookieDao;

  @Autowired
  imageDAO imageDao;

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
      productDao.rownumSet();
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
    String columnName = request.getParameter("columnName");
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
      productDao.rownumSet();
      List<Product> list;

      switch (columnName) {
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
        case "product_tier":
          list = ("desc".equals(orderBy)) ? productDao.productListDescByProductTier(pageInt, limit) :
            productDao.productListByProductTier(pageInt, limit);
          break;
        case "product_sold_out":
          list = ("desc".equals(orderBy)) ? productDao.productListDescByProductSoldOut(pageInt, limit) :
            productDao.productListByProductSoldOut(pageInt, limit);
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
    request.setAttribute("columnName", columnName);
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
                      "productUnit", "productCountry", "productSpecies", "productCompany", "productTier", "productSoldOut"};

    if(memberTier == 9) {
      for(String param : array) {
        if(!request.getParameter(param).isEmpty()) {
          searchText = request.getParameter(param);
          request.setAttribute(param, searchText);
          productDao.rownumSet();
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
            case "productSoldOut":
              list = productDao.productSearchListByProductSoldOut(pageInt, limit, searchText);
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


  @RequestMapping("productSoldOutUpdate")
  public String productSoldOutUpdate() throws Exception {
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
      productDao.rownumSet();
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

    return "admin/productSoldOutUpdate";
  }


  @RequestMapping("productSoldOutUpdatePro")
  public String productSoldOutUpdatePro(String productCode, int productSoldOut) throws Exception {

    productDao.productSoldOutUpdate(productCode, productSoldOut);

    String url = "/admin/productSoldOutUpdate";
    String msg ="제품 품절 수정 성공";

    request.setAttribute("url", url);
    request.setAttribute("msg", msg);

    return "alert";
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
      memberDao.rownumSet();
      List<Member> list = memberDao.memberList(pageInt, limit);
      LOGGER.info(list.toString());
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
    String columnName = request.getParameter("columnName");
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
      memberDao.rownumSet();
      List<Member> list;

      switch (columnName) {
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
        case "member_date":
          list = ("desc".equals(orderBy)) ? memberDao.memberListDescByMemberDate(pageInt, limit) :
            memberDao.memberListByMemberDate(pageInt, limit);
          break;
        case "member_disable":
          list = ("desc".equals(orderBy)) ? memberDao.memberListDescByMemberDisable(pageInt, limit) :
            memberDao.memberListByMemberDisable(pageInt, limit);
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
    request.setAttribute("columnName", columnName);
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
          memberDao.rownumSet();
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

  @RequestMapping("memberWithdrawalList")
  public String memberWithdrawalList() throws Exception {
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
      memberDao.rownumSet();
      List<Member> list = memberDao.memberWithdrawalList(pageInt, limit);
      LOGGER.info(list.toString());
      memberCount = memberDao.memberWithdrawalCount();
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

    return "admin/memberWithdrawalList";
  }

  @RequestMapping("orderHistory")
  public String orderHistory() throws Exception {
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

    int historyCount = 0;

    if(memberTier == 9) {
      historyDao.rownumSet();
      List<History> list = historyDao.historyList(pageInt, limit);
      historyCount = historyDao.historyCount();
      request.setAttribute("list", list);
    }

    int start = (pageInt - 1) / bottomLine * bottomLine + 1;
    int end = start + bottomLine - 1;
    int maxPage = (historyCount / limit) + (historyCount % limit == 0 ? 0 : 1);
    if (end > maxPage) {
      end = maxPage;
    }
    if (end > historyCount) {
      end = historyCount;
    }

    request.setAttribute("memberTier", memberTier);
    request.setAttribute("historyCount", historyCount);
    request.setAttribute("pageNum", pageNum);
    request.setAttribute("start", start);
    request.setAttribute("end", end);
    request.setAttribute("bottomLine", bottomLine);
    request.setAttribute("maxPage", maxPage);
    request.setAttribute("pageInt", pageInt);

    return "admin/orderHistory";
  }

  @RequestMapping("orderHistoryPro")
  public String orderHistoryPro() throws Exception {
    String columnName = request.getParameter("columnName");
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

    int historyCount = 0;

    if(memberTier == 9) {
      historyDao.rownumSet();
      List<History> list;

      switch (columnName) {
        case "history_code":
          list = ("desc".equals(orderBy)) ? historyDao.historyListDescByHistoryCode(pageInt, limit) :
            historyDao.historyListByHistoryCode(pageInt, limit);
          break;
        case "member_id":
          list = ("desc".equals(orderBy)) ? historyDao.historyListDescByMemberId(pageInt, limit) :
            historyDao.historyListByMemberId(pageInt, limit);
          break;
        case "product_code":
          list = ("desc".equals(orderBy)) ? historyDao.historyListDescByProductCode(pageInt, limit) :
            historyDao.historyListByProductCode(pageInt, limit);
          break;
        case "product_name":
          list = ("desc".equals(orderBy)) ? historyDao.historyListDescByProductName(pageInt, limit) :
            historyDao.historyListByProductName(pageInt, limit);
          break;
        case "product_unit":
          list = ("desc".equals(orderBy)) ? historyDao.historyListDescByProductUnit(pageInt, limit) :
            historyDao.historyListByProductUnit(pageInt, limit);
          break;
        case "product_price":
          list = ("desc".equals(orderBy)) ? historyDao.historyListDescByProductPrice(pageInt, limit) :
            historyDao.historyListByProductPrice(pageInt, limit);
          break;
        case "quantity":
          list = ("desc".equals(orderBy)) ? historyDao.historyListDescByQuantity(pageInt, limit) :
            historyDao.historyListByQuantity(pageInt, limit);
          break;
        case "deliveryAddress":
          list = ("desc".equals(orderBy)) ? historyDao.historyListDescByDeliveryAddress(pageInt, limit) :
            historyDao.historyListByDeliveryAddress(pageInt, limit);
          break;
        case "order_date":
          list = ("desc".equals(orderBy)) ? historyDao.historyListDescByOrderDate(pageInt, limit) :
            historyDao.historyListByOrderDate(pageInt, limit);
          break;
        default:
          list = historyDao.historyList(pageInt, limit);
          break;
      }

      if(orderBy == "asc") {
        orderBy = "desc";
      } else if(orderBy == "desc") {
        orderBy = "asc";
      }

      historyCount = historyDao.historyCount();
      request.setAttribute("list", list);
    }

    int start = (pageInt - 1) / bottomLine * bottomLine + 1;
    int end = start + bottomLine - 1;
    int maxPage = (historyCount / limit) + (historyCount % limit == 0 ? 0 : 1);
    if (end > maxPage) {
      end = maxPage;
    }
    if (end > historyCount) {
      end = historyCount;
    }
    request.setAttribute("memberTier", memberTier);
    request.setAttribute("historyCount", historyCount);
    request.setAttribute("columnName", columnName);
    request.setAttribute("orderBy", orderBy);
    request.setAttribute("pageNum", pageNum);
    request.setAttribute("start", start);
    request.setAttribute("end", end);
    request.setAttribute("bottomLine", bottomLine);
    request.setAttribute("maxPage", maxPage);
    request.setAttribute("pageInt", pageInt);

    return "admin/orderHistory";
  }

  @RequestMapping("historySearch")
  public String historySearch() throws Exception {
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

    int historyCount = 0;
    List<History> list = null;
    String searchText = "";
    String searchText1 = "";
    String[] array = {"historyCode", "memberId"};
    List<String> arrayList = new ArrayList<String>();
    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");

    if(memberTier == 9) {
      historyDao.rownumSet();
      historyCount = historyDao.historyCount();

      for(String param : array) {
        if(!request.getParameter(param).isEmpty()) {
          arrayList.add(param);
        }
      }

      if(arrayList.size() == 2) {
        searchText = request.getParameter(arrayList.get(0));
        searchText1 = request.getParameter(arrayList.get(1));
        if(!startDate.isEmpty() && !endDate.isEmpty()) {
          list = historyDao.historySearchListByHistoryCodeAndMemberIdWithOrderDate(pageInt, limit, searchText, searchText1 ,startDate, endDate);
          request.setAttribute("startDate", startDate);
          request.setAttribute("endDate", endDate);
          request.setAttribute("historyCode", searchText);
          request.setAttribute("memberId", searchText1);
        } else {
          list = historyDao.historySearchListByHistoryCodeAndMemberId(pageInt, limit, searchText, searchText1);
          request.setAttribute("historyCode", searchText);
          request.setAttribute("memberId", searchText1);
        }
      } else if (arrayList.size() == 1) {
        searchText = request.getParameter(arrayList.get(0));
        if(arrayList.get(0) == "historyCode") {
          if(!startDate.isEmpty() && !endDate.isEmpty()) {
            list = historyDao.historySearchListByHistoryCodeWithOrderDate(pageInt, limit, searchText, startDate, endDate);
            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);
            request.setAttribute("historyCode", searchText);
          } else {
            list = historyDao.historySearchListByHistoryCode(pageInt, limit, searchText);
            request.setAttribute("historyCode", searchText);
          }
        } else if(arrayList.get(0) == "memberId") {
          if(!startDate.isEmpty() && !endDate.isEmpty()) {
            list = historyDao.historySearchListByMemberIdWithOrderDate(pageInt, limit, searchText, startDate, endDate);
            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);
            request.setAttribute("memberId", searchText);
          } else {
            list = historyDao.historySearchListByMemberId(pageInt, limit, searchText);
            request.setAttribute("memberId", searchText);
          }
        }
      } else if(!startDate.isEmpty() && !endDate.isEmpty()) {
        list = historyDao.historySearchListByOrderDate(pageInt, limit, startDate, endDate);
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
      }
    }

    int start = (pageInt - 1) / bottomLine * bottomLine + 1;
    int end = start + bottomLine - 1;
    int maxPage = (historyCount / limit) + (historyCount % limit == 0 ? 0 : 1);
    if (end > maxPage) {
      end = maxPage;
    }
    if (end > historyCount) {
      end = historyCount;
    }

    request.setAttribute("memberTier", memberTier);
    request.setAttribute("historyCount", historyCount);
    request.setAttribute("list", list);
    request.setAttribute("pageNum", pageNum);
    request.setAttribute("start", start);
    request.setAttribute("end", end);
    request.setAttribute("bottomLine", bottomLine);
    request.setAttribute("maxPage", maxPage);
    request.setAttribute("pageInt", pageInt);

    return "admin/orderHistory";
  }

  @RequestMapping("memberTierUpdate")
  public String memberTierUpdate() throws Exception {
    Integer memberTier = (Integer) session.getAttribute("memberTier");
    if(memberTier == null) {
      memberTier = 0;
    }

    List<Member> list;

    if(memberTier == 9) {
      list = memberDao.memberSearchListByMemberTier(1, 32, "0");
      request.setAttribute("list", list);
    }

    request.setAttribute("memberTier", memberTier);

    return "admin/memberTierUpdate";
  }

  @RequestMapping("memberTierUpdatePro")
  public String memberTierUpdatePro(String memberId, int memberTier) throws Exception {

    LOGGER.info(memberId, memberTier);

    memberDao.memberTierUpdate(memberId, memberTier);

    String url = "/admin/memberTierUpdate";
    String msg ="멤버 등급 수정 성공";

    request.setAttribute("url", url);
    request.setAttribute("msg", msg);

    return "alert";
  }

  @RequestMapping("memberFranCodeUpdate")
  public String memberFranCodeUpdate() throws Exception {
    Integer memberTier = (Integer) session.getAttribute("memberTier");
    if(memberTier == null) {
      memberTier = 0;
    }

    List<Member> list;

    if(memberTier == 9) {
      list = memberDao.memberSearchListByMemberFranCodeByNull(1, 32);
      request.setAttribute("list", list);
    }

    request.setAttribute("memberTier", memberTier);

    return "admin/memberFranCodeUpdate";
  }

  @RequestMapping("memberFranCodeUpdatePro")
  public String memberFranCodeUpdatePro(String memberId, String memberFranCode) throws Exception {
    memberDao.memberFranCodeUpdate(memberId, memberFranCode);

    String url = "/admin/memberFranCodeUpdate";
    String msg ="멤버 가맹점코드 수정 성공";

    request.setAttribute("url", url);
    request.setAttribute("msg", msg);

    return "alert";
  }

  @RequestMapping("memberDisableUpdate")
  public String memberDisableUpdate() throws Exception {
    Integer memberTier = (Integer) session.getAttribute("memberTier");
    if(memberTier == null) {
      memberTier = 0;
    }

    List<Member> list;

    if(memberTier == 9) {
      list = memberDao.memberSearchListByMemberDisable(1, 32);
      request.setAttribute("list", list);
    }

    request.setAttribute("memberTier", memberTier);

    return "admin/memberDisable";
  }

  @RequestMapping("memberDisableUpdatePro")
  public String memberDisableUpdatePro(String memberId) throws Exception {
    Member member = memberDao.memberSelectOne(memberId);
    Member disabledMember = memberDao.memberDisableSelectOne(memberId);

    if(member != null) {
      memberDao.memberDisable(memberId);
      memberDao.memberDelete(memberId);
      memberDao.updateDisabledDate(memberId);
    } else if(disabledMember != null) {
      memberDao.memberEnable(memberId);
      memberDao.disabledMemberDelete(memberId);
      memberDao.updateDisabledDateToNull(memberId);
    }

    String url = "/admin/memberDisableUpdate";
    String msg = "멤버 비활성화 상태 수정 성공";

    request.setAttribute("url", url);
    request.setAttribute("msg", msg);

    return "alert";
  }

  @RequestMapping("productUpload")
  public String productUpload() {
    return "admin/productUpload";
  }

  @RequestMapping("productUploadPro")
  public String productUploadPro(MultipartHttpServletRequest files, Product product) throws Exception {
    String msg = "제품 등록에 실패하였습니다.";
    String url = "/admin/productUpload";

    Image image = new Image();
    String productCode = product.getProductCode();
    int productType = product.getProductType();

    String filePath = request.getServletContext().getRealPath("/") + "view/files/bean/" + productCode;

    if(productType == 1) {
      filePath = request.getServletContext().getRealPath("/") + "view/files/mix/" + productCode;
    }

    if(productType == 2) {
      filePath = request.getServletContext().getRealPath("/") + "view/files/cafe/" + productCode;
    }

    String fileName;
    File uploadPath = new File(filePath);

    if (!uploadPath.exists()) {
      uploadPath.mkdirs(); // 경로가 없으면 생성
    }

    List<MultipartFile> fileList = files.getFiles("files");

    Product check = productDao.productSelectOne(productCode);

    if (check == null) {
      if (fileList.size() > 0) {
        for(int i = 0; i < fileList.size(); i++) {
          fileName = fileList.get(i).getOriginalFilename();
          File file = new File(filePath, fileName);

          image.setProductCode(productCode);
          image.setFileName(fileName);
          image.setFileRegisterName(product.getProductRegisterName());
          imageDao.insertProductImage(image);

          if(fileName.contains("thumbnail")) {
            product.setProductFile(fileName);
          }

          try {
            fileList.get(i).transferTo(file);
          } catch (IllegalStateException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }

        productDao.productInsert(product);

        if(productType == 0) {
          product.setBeanRegisterName(product.getProductRegisterName());
          productDao.beanInsert(product);
        }

        if(productType == 1) {
          product.setMixRegisterName(product.getProductRegisterName());
          productDao.mixInsert(product);
        }

//        if(productType == 2) { // cafe 제품에 필요한 데이터 컬럼은 product 테이블에 전부 있어서 비활성화
//          product.setCafeRegisterName(product.getProductRegisterName());
//          productDao.cafeInsert(product);
//        }

        msg = "제품 등록에 성공하였습니다.";
      } else {
        msg = "업로드 된 파일이 없습니다.";
      }
    } else {
      msg = "이미 존재하는 제품입니다.";
    }

    request.setAttribute("msg", msg);
    request.setAttribute("url", url);

    return "alert";
  }


  @RequestMapping("productUpdate")
  public String productUpdate(@RequestParam(name = "productCode") String productCode) {

    Product product = productDao.productSelectOne(productCode);

    // 제품 타입을 확인 하고 제품 타입에 맞는 데이터 가져와서 jsp에 제공하기

    request.setAttribute("product", product);
    request.setAttribute("existProductCode", productCode);

    return "admin/productUpdate";
  }

  @RequestMapping("productUpdatePro")
  public String productUpdatePro(MultipartHttpServletRequest files, Product product) throws Exception {
    String msg = "제품 수정에 실패하였습니다.";
    String url = "/admin/productUpdate";

    Image image = new Image();
    String productCode = product.getProductCode();
    int productType = product.getProductType();

    String filePath = request.getServletContext().getRealPath("/") + "view/files/bean/" + productCode;

    if(productType == 1) {
      filePath = request.getServletContext().getRealPath("/") + "view/files/mix/" + productCode;
    }

    if(productType == 2) {
      filePath = request.getServletContext().getRealPath("/") + "view/files/cafe/" + productCode;
    }

    String fileName;
    File uploadPath = new File(filePath);

    if (!uploadPath.exists()) {
      uploadPath.mkdirs(); // 경로가 없으면 생성
    }

    List<MultipartFile> fileList = files.getFiles("files");

    Product check = productDao.productSelectOne(productCode);

    if (check != null) {
      if (fileList.size() > 0) {
        for(int i = 0; i < fileList.size(); i++) {
          fileName = fileList.get(i).getOriginalFilename();
          File file = new File(filePath, fileName);

          image.setProductCode(productCode);
          image.setFileName(fileName);
          image.setFileModifierName(product.getProductModifierName());
          imageDao.updateProductImage(image);

          if(fileName.contains("thumbnail")) {
            product.setProductFile(fileName);
          }

          try {
            fileList.get(i).transferTo(file);
          } catch (IllegalStateException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }

        productDao.productUpdate(product);

        if(productType == 0) {
          product.setBeanModifierName(product.getProductModifierName());
          productDao.beanUpdate(product);
        }

        if(productType == 1) {
          product.setMixModifierName(product.getProductModifierName());
          productDao.mixUpdate(product);
        }

//        if(productType == 2) { // cafe 제품에 필요한 데이터 컬럼은 product 테이블에 전부 있어서 비활성화
//          product.setCafeRegisterName(product.getProductRegisterName());
//          productDao.cafeInsert(product);
//        }

        msg = "제품 수정에 성공하였습니다.";
      } else {
        msg = "업로드 된 파일이 없습니다.";
      }
    } else {
      msg = "제품이 존재하지 않습니다.";
    }

    request.setAttribute("msg", msg);
    request.setAttribute("url", url);

    return "alert";
  }

  @RequestMapping("productDelete")
  public String productDelete() throws Exception {
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
      productDao.rownumSet();
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

    return "admin/productDelete";
  }

  @RequestMapping("productDeletePro")
  public String productDeletePro(String productCode) throws Exception {
    Integer memberTier = (Integer) session.getAttribute("memberTier");
    if(memberTier == null) {
      memberTier = 0;
    }

    String msg = "제품 삭제에 실패했습니다.";
    String url = "/admin/productDelete";

    if(memberTier == 9) {
      productDao.productDelete(productCode);
      msg = "제품 삭제에 성공했습니다.";
    }

    request.setAttribute("msg", msg);
    request.setAttribute("url", url);

    return "alert";
  }

  @RequestMapping("excelProductDownload")
  public void excelProductDownload(HttpServletResponse response) throws IOException {
    List<Product> productList = productDao.productListAll();

    String excelFileName = "productList.xlsx";

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment; filename=" + excelFileName);

    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("제품 리스트");

    Row headerRow = sheet.createRow(0);
    headerRow.createCell(0).setCellValue("제품번호");
    headerRow.createCell(1).setCellValue("제품종류");
    headerRow.createCell(2).setCellValue("제품명");
    headerRow.createCell(3).setCellValue("가격");
    headerRow.createCell(4).setCellValue("단위");
    headerRow.createCell(5).setCellValue("등급");
    headerRow.createCell(6).setCellValue("품절여부");
    headerRow.createCell(7).setCellValue("등록자");
    headerRow.createCell(8).setCellValue("등록일");
    headerRow.createCell(9).setCellValue("수정자");
    headerRow.createCell(10).setCellValue("수정일");

    int rowNum = 1;
    for (Product product : productList) {
      Row row = sheet.createRow(rowNum++);
      int colNum = 0;
      row.createCell(colNum++).setCellValue(product.getProductCode());
      row.createCell(colNum++).setCellValue(product.getProductType());
      row.createCell(colNum++).setCellValue(product.getProductName());
      row.createCell(colNum++).setCellValue(product.getProductPrice());
      row.createCell(colNum++).setCellValue(product.getProductUnit());
      row.createCell(colNum++).setCellValue(product.getProductTier());
      row.createCell(colNum++).setCellValue(product.getProductSoldOut());
      row.createCell(colNum++).setCellValue(product.getProductRegisterName());
      row.createCell(colNum++).setCellValue(product.getProductRegisterDate());
      row.createCell(colNum++).setCellValue(product.getProductModifierName());
      row.createCell(colNum++).setCellValue(product.getProductModifierDate());
    }

    ServletOutputStream outputStream = response.getOutputStream();
    workbook.write(outputStream);
    workbook.close();
    outputStream.close();
  }

  @RequestMapping("excelMemberDownload")
  public void excelMemberDownload(HttpServletResponse response) throws IOException {
    List<Member> memberList = memberDao.memberListAll();

    String excelFileName = "memberList.xlsx";

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment; filename=" + excelFileName);

    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("회원 리스트");

    Row headerRow = sheet.createRow(0);
    headerRow.createCell(0).setCellValue("등급");
    headerRow.createCell(1).setCellValue("아이디");
    headerRow.createCell(2).setCellValue("이름");
    headerRow.createCell(3).setCellValue("회사명");
    headerRow.createCell(4).setCellValue("비밀번호");
    headerRow.createCell(5).setCellValue("전화번호");
    headerRow.createCell(6).setCellValue("회사번호");
    headerRow.createCell(7).setCellValue("주소");
    headerRow.createCell(8).setCellValue("배송지");
    headerRow.createCell(9).setCellValue("이메일");
    headerRow.createCell(10).setCellValue("가맹점코드");
    headerRow.createCell(11).setCellValue("가입일");
    headerRow.createCell(12).setCellValue("비활성화일");
    headerRow.createCell(13).setCellValue("수정자");
    headerRow.createCell(14).setCellValue("수정일");

    int rowNum = 1;
    for (Member member : memberList) {
      Row row = sheet.createRow(rowNum++);
      int colNum = 0;
      row.createCell(colNum++).setCellValue(member.getMemberTier());
      row.createCell(colNum++).setCellValue(member.getMemberId());
      row.createCell(colNum++).setCellValue(member.getMemberName());
      row.createCell(colNum++).setCellValue(member.getMemberCompanyName());
      row.createCell(colNum++).setCellValue(member.getMemberPassword());
      row.createCell(colNum++).setCellValue(member.getMemberTel());
      row.createCell(colNum++).setCellValue(member.getMemberCompanyTel());
      row.createCell(colNum++).setCellValue(member.getMemberAddress() + " " + member.getMemberDetailAddress());
      row.createCell(colNum++).setCellValue(member.getMemberDeliveryAddress() + " " + member.getMemberDetailDeliveryAddress());
      row.createCell(colNum++).setCellValue(member.getMemberEmail());
      row.createCell(colNum++).setCellValue(member.getMemberFranCode());
      row.createCell(colNum++).setCellValue(member.getMemberDate());
      row.createCell(colNum++).setCellValue(member.getMemberDisableDate());
      row.createCell(colNum++).setCellValue(member.getMemberModifierName());
      row.createCell(colNum++).setCellValue(member.getMemberModifierDate());
    }

    ServletOutputStream outputStream = response.getOutputStream();
    workbook.write(outputStream);
    workbook.close();
    outputStream.close();
  }

  @RequestMapping("excelHistoryDownload")
  public void excelHistoryDownload(HttpServletResponse response) throws IOException {
    List<History> historyList = historyDao.historyListAll();

    String excelFileName = "historyList.xlsx";

    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment; filename=" + excelFileName);

    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("주문기록");

    Row headerRow = sheet.createRow(0);

    headerRow.createCell(0).setCellValue("주문번호");
    headerRow.createCell(1).setCellValue("회원등급");
    headerRow.createCell(2).setCellValue("아이디");
    headerRow.createCell(3).setCellValue("주문자명");
    headerRow.createCell(4).setCellValue("가맹점코드");
    headerRow.createCell(5).setCellValue("제품번호");
    headerRow.createCell(6).setCellValue("제품이름");
    headerRow.createCell(7).setCellValue("수량");
    headerRow.createCell(8).setCellValue("용량");
    headerRow.createCell(9).setCellValue("가격");
    headerRow.createCell(10).setCellValue("합계");
    headerRow.createCell(11).setCellValue("주문일");
    headerRow.createCell(12).setCellValue("배송지");

    int rowNum = 1;
    for (History history : historyList) {
      Row row = sheet.createRow(rowNum++);
      int colNum = 0;
      row.createCell(colNum++).setCellValue(history.getOrderId());
      row.createCell(colNum++).setCellValue(history.getMemberTier());
      row.createCell(colNum++).setCellValue(history.getMemberId());
      row.createCell(colNum++).setCellValue(history.getMemberName());
      row.createCell(colNum++).setCellValue(history.getMemberFranCode());
      row.createCell(colNum++).setCellValue(history.getProductCode());
      row.createCell(colNum++).setCellValue(history.getProductName());
      row.createCell(colNum++).setCellValue(history.getQuantity());
      row.createCell(colNum++).setCellValue(history.getProductUnit());
      row.createCell(colNum++).setCellValue(history.getProductPrice());
      row.createCell(colNum++).setCellValue(history.getTotalPrice());
      row.createCell(colNum++).setCellValue(history.getOrderDate());
      row.createCell(colNum++).setCellValue(history.getDeliveryAddress() + " " + history.getDetailDeliveryAddress());
    }

    ServletOutputStream outputStream = response.getOutputStream();
    workbook.write(outputStream);
    workbook.close();
    outputStream.close();
  }

}
