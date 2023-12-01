package controller;

import model.Cart;
import model.History;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import service.*;
import model.Member;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/member/")
public class memberController {

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

  @Autowired
  historyDAO historyDao;

  @Autowired
  private PasswordEncoder passwordEncoder;

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

  private static final Logger LOGGER = LoggerFactory.getLogger(memberController.class);

  //암호화 SHA256
  public static class SHA256 {

    public String encrypt(String text) throws NoSuchAlgorithmException {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(text.getBytes());

      return bytesToHex(md.digest());
    }

    private String bytesToHex(byte[] bytes) {
      StringBuilder builder = new StringBuilder();
      for (byte b : bytes) {
        builder.append(String.format("%02x", b));
      }
      return builder.toString();
    }

  }

  @RequestMapping("memberSignUp")
  public String memberSignUp() throws Exception {
    return "member/memberSignUpForm";
  }

  @RequestMapping("memberSignUpPro")
  public String memberSignUpPro(@RequestParam("file") MultipartFile file, Member member) throws Exception {
    String msg = "이미 있는 아이디 입니다.";
    String url = "/member/memberSignUp";

    String memberId = member.getMemberId();
    Member mem = memberDao.memberSelectOne(memberId);

    String filePath = request.getServletContext().getRealPath("/") + "view/files/";
    String fileName = file.getOriginalFilename();
    File uploadFile = new File(filePath, fileName);
    File uploadPath = new File(filePath);

    if (!uploadPath.exists()) {
      uploadPath.mkdirs(); // 경로가 없으면 생성
    }

    if(mem == null) {
      member.setMemberPassword(passwordEncoder.encode(member.getMemberPassword()));
      LOGGER.info(String.valueOf(member));
      int num = memberDao.memberInsert(member);
      file.transferTo(uploadFile);
      if (num > 0) {
        msg = memberId + "님의 가입이 완료되었습니다.";
        url = "/member/memberSignIn";
      } else {
        msg = "회원가입을 실패 했습니다.";
        url = "/member/memberSignUp";
      }
    }

    request.setAttribute("msg", msg);
    request.setAttribute("url", url);

    return "alert";
  }

  @RequestMapping("memberSignIn")
  public String memberSignIn() throws Exception {
    return "member/memberSignInForm";
  }

  @RequestMapping("memberSignInPro")
  public String memberSignInPro(String memberId, String memberPassword, String autoLogin) throws Exception {

    //Context 생성
    ConfigurableApplicationContext context = new GenericXmlApplicationContext();
    //Environment 생성
    ConfigurableEnvironment environment = context.getEnvironment();
    //PropertySource 다 가져오기
    MutablePropertySources propertySources = environment.getPropertySources();

    propertySources.addLast(new ResourcePropertySource("classpath:application.properties"));

    String keyString = environment.getProperty("CookieLogin");

    SHA256 sha256 = new SHA256();

    String msg = "";
    String url = "/member/memberSignIn";

    Member member = memberDao.memberSelectOne(memberId);

    if(member != null) {
      if(member.getMemberDisable() != 1) {
        if(passwordEncoder.matches(memberPassword, member.getMemberPassword())) {
          Integer memberTier = member.getMemberTier();
          session.setAttribute("memberId", memberId);
          session.setAttribute("memberTier", memberTier);

          url = "/board/main";

          if(autoLogin != null) {
            String encryptKey = member.getMemberId() + keyString;

            String token = sha256.encrypt(encryptKey);

            int num = cookieDao.cookieInsert(memberId, token);

            if(num > 0) {
              Cookie cookieId = new Cookie("memberId", memberId);
              Cookie cookieToken = new Cookie("token", token);
              cookieId.setMaxAge(60 * 60 * 24 * 30); // 60 * 60 * 24 * 30 == 30days
              cookieId.setPath("/");
              cookieToken.setMaxAge(60 * 60 * 24 * 30);
              cookieToken.setPath("/");
              response.addCookie(cookieId);
              response.addCookie(cookieToken);
            }
          }
          return "anchor";
        } else {
          msg = "비밀번호가 틀립니다.";
        }
      } else {
        msg = "존재하지 않는 아이디입니다."; // disable 된 아이디 일 떄
      }
    } else {
      msg = "존재하지 않는 아이디입니다."; // db에 해당 아이디가 없을 때
    }

    request.setAttribute("msg", msg);
    request.setAttribute("url", url);

    return "alert";
  }

  @RequestMapping("memberLogout")
  public String memberLogout() throws Exception {
    String memberId = (String) session.getAttribute("memberId");

    Cookie cookieId = new Cookie("memberId", null);
    Cookie cookieToken = new Cookie("token", null);
    cookieId.setMaxAge(0); // 0초 = 쿠키 삭제
    cookieId.setPath("/");
    cookieToken.setMaxAge(0);
    cookieToken.setPath("/");
    response.addCookie(cookieId);
    response.addCookie(cookieToken);

    cookieDao.cookieDelete(memberId);

    String msg = "로그아웃 되었습니다.";
    String url = "/board/main";

    session.invalidate();

    request.setAttribute("msg", msg);
    request.setAttribute("url", url);

    return "alert";
  }

  @RequestMapping("memberDisable")
  public String memberDisable() throws Exception {
    return "member/memberDisable";
  }

  @RequestMapping("memberDisablePro")
  public String memberDisablePro(String memberPassword) throws Exception {
    String memberId = (String) session.getAttribute("memberId");
    Member member = memberDao.memberSelectOne(memberId);

    String msg = "회원 탈퇴에 실패했습니다.";
    String url = "/member/memberDisable";

    if(passwordEncoder.matches(memberPassword, member.getMemberPassword())) {
      Cookie cookieId = new Cookie("memberId", null);
      Cookie cookieToken = new Cookie("token", null);
      cookieId.setMaxAge(0); // 0초 = 쿠키 삭제
      cookieId.setPath("/");
      cookieToken.setMaxAge(0);
      cookieToken.setPath("/");
      response.addCookie(cookieId);
      response.addCookie(cookieToken);

      cookieDao.cookieDelete(memberId);
      memberDao.memberDisable(memberId, 1);

      session.invalidate();

      msg = "회원 탈퇴에 성공했습니다.";
      url = "/board/main";
    }

    request.setAttribute("msg", msg);
    request.setAttribute("url", url);

    return "alert";
  }

  @RequestMapping("memberCart")
  public String memberCart() throws Exception {
    String memberId = (String) session.getAttribute("memberId");

    int sumPrice = Integer.parseInt(cartDao.cartSumPrice(memberId));
    int cartCount = cartDao.cartCount(memberId);

    int deliveryFee = 3000;
    int minimumPrice = 30000;

    if(sumPrice > minimumPrice || sumPrice == 0) {
      deliveryFee = 0;
    }

    int totalPrice = deliveryFee + sumPrice;

    List<Cart> list = cartDao.cartSelectMember(memberId);

    System.out.println(list);

    request.setAttribute("totalPrice", totalPrice);
    request.setAttribute("sumPrice", sumPrice);
    request.setAttribute("deliveryFee", deliveryFee);
    request.setAttribute("cartCount", cartCount);
    request.setAttribute("list", list);

    return "member/memberCart";
  }

  @RequestMapping("memberCartPro")
  public String memberCartPro() throws Exception {
    int status = Integer.parseInt(request.getParameter("status"));
    int quantity = Integer.parseInt(request.getParameter("quantity"));
    String productCode = request.getParameter("productCode");
    String memberId = (String) session.getAttribute("memberId");

    if(quantity < 1) {
      quantity = 1;
    }

    if(status == 0) {
      cartDao.cartDelete(memberId, productCode);
    } else if (status == 1) {
      cartDao.cartQuantityUpdate(memberId, productCode, quantity);
    }

    int sumPrice = Integer.parseInt(cartDao.cartSumPrice(memberId));
    int cartCount = cartDao.cartCount(memberId);

    int deliveryFee = 3000;
    int minimumPrice = 30000;

    if(sumPrice > minimumPrice || sumPrice == 0) {
      deliveryFee = 0;
    }

    int totalPrice = deliveryFee + sumPrice;

    List<Cart> list = cartDao.cartSelectMember(memberId);

    System.out.println(list);

    request.setAttribute("totalPrice", totalPrice);
    request.setAttribute("sumPrice", sumPrice);
    request.setAttribute("deliveryFee", deliveryFee);
    request.setAttribute("cartCount", cartCount);
    request.setAttribute("list", list);

    return "member/memberCart";
  }

  @RequestMapping("memberFindAccount")
  public String memberFindAccount() throws Exception {

    return "member/memberFindAccount";
  }

  @RequestMapping("memberMyPage")
  public String memberMyPage() throws Exception {

    return "member/memberMyPage";
  }

  @RequestMapping("memberProfile")
  public String memberProfile() throws Exception {
    String memberId = (String) session.getAttribute("memberId");

    Member member = memberDao.memberSelectOne(memberId);

    request.setAttribute("member", member);

    return "member/memberProfile";
  }

  @RequestMapping("memberProfilePro")
  public String memberProfilePro(Member member, String memberExistingPassword) throws Exception {
    String memberId = (String) session.getAttribute("memberId");

    Member existingMember = memberDao.memberSelectOne(memberId); //기존 회원 정보
    member.setMemberId(memberId); // 사용자가 임의로 변경하는 것을 막기 위함

    String url = "/member/memberProfile";
    String msg = "회원 정보가 수정되었습니다.";

    if(memberExistingPassword != null) {
      if(passwordEncoder.matches(memberExistingPassword, existingMember.getMemberPassword())) { // 입력한 기존 비밀번호와 db의 비밀번호가 일치 할 때 변경
        if(member.getMemberPassword() == null || member.getMemberPassword().isEmpty()) {
          memberDao.memberUpdateNotPassword(member);
        } else {
          member.setMemberPassword(passwordEncoder.encode(member.getMemberPassword()));
          memberDao.memberUpdate(member);
        }
      } else {
        msg = "기존 비밀번호가 틀렸습니다.";
      }
    }

    request.setAttribute("url", url);
    request.setAttribute("msg", msg);

    return "alert";
  }

  @RequestMapping("memberHistory")
  public String memberHistory() throws Exception {
    String memberId = (String) session.getAttribute("memberId");

    LocalDate now = LocalDate.now();
    int year = now.getYear();
    int dayOfMonth = now.getDayOfMonth();
    int monthValue = now.getMonthValue() - 3;
    String month = String.valueOf(monthValue);
    String day = String.valueOf(dayOfMonth);

    if(monthValue < 10 ) {
      month = "0" + monthValue;
    }

    if(dayOfMonth < 10 ) {
      day = "0" + dayOfMonth;
    }

    String startDate = year + "-" + month + "-" + day;
    String endDate = String.valueOf(now);

    List<History> list = historyDao.historySelectBetween(memberId, startDate, endDate);
    int historyCount = historyDao.historyCountBetween(memberId, startDate, endDate);

    request.setAttribute("startDate", startDate);
    request.setAttribute("endDate", endDate);
    request.setAttribute("historyCount", historyCount);
    request.setAttribute("list", list);

    return "member/memberHistory";
  }

  @RequestMapping("memberHistoryPro")
  public String memberHistoryPro() throws Exception {
    String memberId = (String) session.getAttribute("memberId");
    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");

    List<History> list = historyDao.historySelectBetween(memberId, startDate, endDate);
    int historyCount = historyDao.historyCountBetween(memberId, startDate, endDate);

    request.setAttribute("startDate", startDate);
    request.setAttribute("endDate", endDate);
    request.setAttribute("historyCount", historyCount);
    request.setAttribute("list", list);

    return "member/memberHistory";
  }

}

