package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import service.cartDAO;
import service.cookieDAO;
import service.memberDAO;
import service.productDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

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
