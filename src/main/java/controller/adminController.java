package controller;

import model.Member;
import model.Product;
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
import javax.sql.DataSource;
import java.util.List;

@Controller
@RequestMapping("/admin/")
public class adminController {

  @Autowired
  private DataSource ds;

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

    System.out.println("orderBy: " + orderBy);
    System.out.println("product : " + product);

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
      List<Product> list = null;
      if ("asc".equals(orderBy)) {
        list = productDao.productListAscByAll(product, pageInt, limit);
      } else if ("desc".equals(orderBy)) {
        list = productDao.productListDescByAll(product, pageInt, limit);
      }
      productCount = productDao.productCount();
      request.setAttribute("list", list);
      System.out.println(list);
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
