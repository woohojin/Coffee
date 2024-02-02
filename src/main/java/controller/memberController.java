package controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import service.*;
import model.Member;
import model.Cart;
import model.History;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/member/")
@PropertySource("classpath:application.properties")
public class memberController {

  @Autowired
  private DataSource ds;

  @Autowired
  private Environment env;

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
  private JavaMailSender mailSender;

  @Autowired
  private ResourceLoader resourceLoader;

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

  public static String getRandomPassword(int size) { //난수 생성기
    char[] charSet = new char[] {
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    if (size <= 0 || charSet.length == 0) {
      throw new IllegalArgumentException("Invalid size or character set");
    }

    StringBuilder sb = new StringBuilder();
    SecureRandom sr = new SecureRandom();

    int len = charSet.length;
    for (int i = 0; i < size; i++) {
      int idx = sr.nextInt(len);
      sb.append(charSet[idx]);
    }

    return sb.toString();
  }

  @Bean
  public JavaMailSender mailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    String host = "smtp.naver.com";
    int port = 587;

    String username = env.getProperty("spring.mail.username");
    String password = env.getProperty("spring.mail.password");

    mailSender.setDefaultEncoding("UTF-8");
    mailSender.setHost(host);
    mailSender.setPort(port);
    mailSender.setUsername(username);
    mailSender.setPassword(password);

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");

    return mailSender;
  }

  public void sendEmail(String toEmail, String subject, String main, String code) {
    String username = env.getProperty("spring.mail.username");
    String htmlContent = readTemplate();
    htmlContent = htmlContent.replace("{{main}}", main);
    htmlContent = htmlContent.replace("{{code}}", code);

    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setFrom(username);
      helper.setTo(toEmail);
      helper.setSubject(subject);
      helper.setText(htmlContent, true);
      mailSender.send(message);
    } catch (MailException e) {
      LOGGER.error("메일 전송 중 오류 발생: {}", e.getMessage());
      e.printStackTrace();
    } catch (MessagingException e) {
      LOGGER.error("메일 전송 중 메시징 예외 발생: {}", e.getMessage());
      e.printStackTrace();
    }
  }

  public String readTemplate() {
    Resource resource = resourceLoader.getResource("classpath:mail.html");
    StringBuilder contentBuilder = new StringBuilder();

    try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
      String line;
      while ((line = br.readLine()) != null) {
        contentBuilder.append(line).append("\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return contentBuilder.toString();
  }

  @RequestMapping("memberTerms")
  public String memberTerms() throws Exception {
    return "member/memberTerms";
  }


  @RequestMapping("memberSignUp")
  public String memberSignUp() throws Exception {
    return "member/memberSignUpForm";
  }

  @RequestMapping("memberSignUpPro")
  public String memberSignUpPro(@RequestParam("file") MultipartFile file, Member member) throws Exception {

    String verifyCode = request.getParameter("verifyCode");
    String storedVerifyCode = (String) session.getAttribute("storedVerifyCode");
    if(verifyCode == null || verifyCode.equals("timeout")) {
      String url = "/member/memberSignUp";
      String msg = "인증시간이 초과되었습니다.";

      request.setAttribute("url", url);
      request.setAttribute("msg", msg);

      return "alert";
    }

    if(!verifyCode.equals(storedVerifyCode)) {
      String url = "/member/memberSignUp";
      String msg = "인증번호가 일치하지 않습니다.";

      request.setAttribute("url", url);
      request.setAttribute("msg", msg);

      return "alert";
    }

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

      String memberCompanyName = member.getMemberCompanyName();
      String memberCompanyTel = member.getMemberCompanyTel();
      String memberFile = member.getMemberFile();

      if(memberFile != null && !memberFile.trim().isEmpty()) {
        file.transferTo(uploadFile);
      } else {
        member.setMemberFile(null);
      }

      if(memberCompanyName != null && memberCompanyName.trim().isEmpty()) {
        member.setMemberCompanyName(null);
      }

      if(memberCompanyTel != null && memberCompanyTel.trim().isEmpty()) {
        member.setMemberCompanyTel(null);
      }

      int num = memberDao.memberInsert(member);

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
    int isDisabled = memberDao.disabledMemberSelectOne(memberId);
    if(member != null) {
      if(isDisabled < 1) {
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

  @RequestMapping("memberWithdrawal")
  public String memberWithdrawal() throws Exception {
    return "/member/memberWithdrawal";
  }

  @RequestMapping("memberWithdrawalPro")
  public String memberWithdrawalPro(String memberPassword) throws Exception {
    String memberId = (String) session.getAttribute("memberId");
    Member member = memberDao.memberSelectOne(memberId);

    String msg = "회원 탈퇴에 실패했습니다.";
    String url = "/member/memberWithdrawal";

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
      memberDao.memberWithdrawal(memberId);
      memberDao.memberDelete(memberId);

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

    int productType = 0; // 원두

    int sumPrice = Integer.parseInt(cartDao.cartSumPrice(memberId));
    int cartCount = cartDao.cartCount(memberId);
    int deliveryFee = 3000;

    int beanCount = cartDao.cartCountByProductType(memberId, productType); // 담은 원두 상품의 종류 개수 확인
    List<Integer> beanQuantityList = cartDao.checkQuantityByProductType(memberId, productType); // 담은 원두 상품의 개수 확인

    boolean hasQuantityOverThanOne = beanQuantityList.stream().anyMatch(beanQuantity -> beanQuantity >= 2 ); // 개수가 2 이상이여야 true

    List<String> cartProductCodeList = cartDao.cartProductCodeList(memberId);

    List<String> freeShippingProductCodeList = Arrays.asList(
            "DA0002", "DA0002-2", "DA0011", "DA0011-2", "DA0012", "DA0012-2", "DA0013", "DA0013-2", "DA0013-3", "CA0003", "CA0010", "CA0101", "CA0105"
    ); // 무료배송 상품

    List<String> paidShippingProductCodeList = Arrays.asList(
            "AA0001", "AA0001-2", "AA0011", "AA0011-2", "AA0021", "AA0021-2", "AA0031", "AA0031-2", "AA0041", "AA0041-2", "AA0041-3", "AA0051", "AA0051-2", "AA0061", "AA0061-2",
            "CA0001", "CA0202", "CA0214", "CA0501", "CA0210", "CA0520"
    );

    boolean allAreFreeShipping = cartProductCodeList.stream().allMatch(code -> freeShippingProductCodeList.contains(code));

    String specificFeeProductCode = "CA0001"; // specificProduct = 배송비가 건당으로 붙는 특정 제품

    int quantityBySpecificProduct = cartDao.checkQuantityByProductCode(memberId, specificFeeProductCode);

    long paidShippingProductCount = cartProductCodeList.stream().filter(code -> paidShippingProductCodeList.contains(code)).count();


    if(paidShippingProductCount > 0) {
      deliveryFee += 3000 * (paidShippingProductCount - 1);
    }

    if(quantityBySpecificProduct >= 2) { // CA0001 제품은 건당으로 배송비 발생
      deliveryFee = deliveryFee + (3000 * (quantityBySpecificProduct - 1));
    }

    if(quantityBySpecificProduct < 2) {

    }

    if(beanCount >= 2 || hasQuantityOverThanOne || sumPrice == 0 || allAreFreeShipping) { // 2키로 이상시 전체 배송비 무료, 카트에 담은게 없어도 배송비 x
      deliveryFee = 0;
    }


    final int totalPrice = deliveryFee + sumPrice; // 총 가격 계산 후 변경 불가

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
    int status = 9;
    String statusParam = request.getParameter("status");

    int quantity = 1;
    String quantityParam = request.getParameter("quantity");

    int productGrinding = 0;
    String productGrindingParam = request.getParameter("productGrinding");

    String productCode = request.getParameter("productCode");
    String memberId = (String) session.getAttribute("memberId");

    if(statusParam != null && !statusParam.trim().isEmpty()) {
      status = Integer.parseInt(statusParam);
    }
    if(quantityParam != null && !quantityParam.trim().isEmpty()) {
      quantity = Integer.parseInt(quantityParam);
    }
    if(productGrindingParam != null && !productGrindingParam.trim().isEmpty()) {
      productGrinding = Integer.parseInt(productGrindingParam);
    }

    if(status == 0) { // status 0 = delete || status 1 = updateQuantity || status 2 = updateGrindingType
      cartDao.cartDelete(memberId, productCode);
    } else if (status == 1) {
      cartDao.cartQuantityUpdate(memberId, productCode, quantity);
    } // else if (status == 2) {
    //  cartDao.cartGrindingUpdate(memberId, productCode, productGrinding);
   // }

    int deliveryFee = 3000; // 배송비
    int sumPrice = Integer.parseInt(cartDao.cartSumPrice(memberId));
    int cartCount = cartDao.cartCount(memberId);

    int productType = 0; // 원두

    int beanCount = cartDao.cartCountByProductType(memberId, productType); // 담은 원두 상품의 종류 개수 확인
    List<Integer> beanQuantityList = cartDao.checkQuantityByProductType(memberId, productType); // 담은 원두 상품의 개수 확인

    boolean hasQuantityOverThanOne = beanQuantityList.stream().anyMatch(beanQuantity -> beanQuantity >= 2 ); // 개수가 2 이상이여야 true

    List<String> cartProductCodeList = cartDao.cartProductCodeList(memberId);

    List<String> freeShippingProductCodeList = Arrays.asList(
            "DA0002", "DA0002-2", "DA0011", "DA0011-2", "DA0012", "DA0012-2", "DA0013", "DA0013-2", "DA0013-3", "CA0003", "CA0010", "CA0101", "CA0105"
    ); // 무료배송 상품

    List<String> paidShippingProductCodeList = Arrays.asList(
      "AA0001", "AA0001-2", "AA0011", "AA0011-2", "AA0021", "AA0021-2", "AA0031", "AA0031-2", "AA0041", "AA0041-2", "AA0041-3", "AA0051", "AA0051-2", "AA0061", "AA0061-2",
      "CA0001", "CA0202", "CA0214", "CA0501", "CA0210", "CA0520"
    );

    boolean allAreFreeShipping = cartProductCodeList.stream().allMatch(code -> freeShippingProductCodeList.contains(code));

    String specificFeeProductCode = "CA0001"; // specificProduct = 배송비가 건당으로 붙는 특정 제품

    int quantityBySpecificProduct = cartDao.checkQuantityByProductCode(memberId, specificFeeProductCode);

    long paidShippingProductCount = cartProductCodeList.stream().filter(code -> paidShippingProductCodeList.contains(code)).count();

    if(quantityBySpecificProduct >= 2) {
      deliveryFee = deliveryFee + (3000 * (quantityBySpecificProduct - 1)); // CA0001 제품은 건당으로 배송비 발생
    }

    if(cartProductCodeList.size() > 0) {
      deliveryFee += 3000 * (paidShippingProductCount - 1);
    }

    if(beanCount >= 2 || hasQuantityOverThanOne || sumPrice == 0 || allAreFreeShipping) { // 2키로 이상시 전체 배송비 무료, 카트에 담은게 없어도 배송비 x
      deliveryFee = 0;
    }

    final int totalPrice = deliveryFee + sumPrice; // 총 가격 계산 후 변경 불가

    List<Cart> list = cartDao.cartSelectMember(memberId);

    request.setAttribute("totalPrice", totalPrice);
    request.setAttribute("sumPrice", sumPrice);
    request.setAttribute("deliveryFee", deliveryFee);
    request.setAttribute("cartCount", cartCount);
    request.setAttribute("list", list);

    return "member/memberCart";
  }

  @RequestMapping("memberFindAccount")
  public String memberFindAccount(@RequestParam(value = "findType", required = false, defaultValue = "id") String findType) throws Exception {
    int isFind = 0;

    request.setAttribute("isFind", isFind);
    request.setAttribute("findType", findType);

    return "member/memberFindAccount";
  }

  @RequestMapping("memberFindAccountPro")
  public String memberFindAccountPro() throws Exception {
    String verifyCode = request.getParameter("verifyCode");
    String storedVerifyCode = (String) session.getAttribute("storedVerifyCode");

    if(verifyCode == null || verifyCode.equals("timeout")) { // 인증시간이 초과되면 verifyCode가 timeout으로 바뀜
      String url = "/member/memberFindAccount";
      String msg = "인증시간이 초과되었습니다.";

      request.setAttribute("url", url);
      request.setAttribute("msg", msg);

      return "alert";
    }

    if(!verifyCode.equals(storedVerifyCode)) { // 사용자가 입력한 코드와 생성된 코드가 일치하는지 확인
      String url = "/member/memberFindAccount";
      String msg = "인증번호가 일치하지 않습니다.";

      request.setAttribute("url", url);
      request.setAttribute("msg", msg);

      return "alert";
    }

    String findType = request.getParameter("findType");

    if(findType == null || findType.isEmpty()) {
      findType = "id";
    }

    int isFind = 0;

    if(findType.equals("id")) {
      String memberName = request.getParameter("memberName");
      String memberEmail = request.getParameter("memberEmail");
      List<Member> list = memberDao.memberFindId(memberName, memberEmail);

      if(list != null && !list.isEmpty()) {
        isFind = 1;
        request.setAttribute("list", list);
      } else {
        String url = "/member/memberFindAccount";
        String msg = "아이디가 존재하지 않거나 이름 혹은 이메일이 일치하지 않습니다.";

        request.setAttribute("url", url);
        request.setAttribute("msg", msg);
        return "alert";
      }

    }

    if(findType.equals("password")) {
      String memberId = request.getParameter("memberId");
      String memberEmail = request.getParameter("memberEmail");
      String memberPassword = memberDao.memberFindPassword(memberId, memberEmail);

      String url = "/member/memberFindAccount";
      String msg = "존재하지 않는 아이디 혹은 이메일이 일치하지 않습니다."; // 밑의 if문의 조건에 부합하지 않을 경우 사용 됨

      if(memberPassword != null && !memberPassword.isEmpty()) {
        String subject = "다올커피 - 비밀번호가 임시 비밀번호로 변경되었습니다.";
        String main = "회원님의 임시 비밀번호는";
        String password = getRandomPassword(8);
        sendEmail(memberEmail, subject, main, password);

        String tempPassword = passwordEncoder.encode(password);
        LOGGER.info(tempPassword);
        memberDao.memberTempPasswordUpdate(memberId, tempPassword);

        url = "/member/memberSignIn";
        msg = "임시비밀번호가 이메일로 전송되었습니다.";
      }

      request.setAttribute("url", url);
      request.setAttribute("msg", msg);
      return "alert";
    }

    request.setAttribute("isFind", isFind);
    request.setAttribute("findType", findType);

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

  @PostMapping("verifyEmail")
  @ResponseBody
  public Map<String, Object> verifyEmail(String memberEmail) throws Exception {
    Map<String, Object> map = new HashMap<>();

    String code = getRandomPassword(6);
    String subject = "다올커피 - 이메일 인증번호가 도착했습니다.";
    String main = "회원님의 이메일 인증번호는";

    sendEmail(memberEmail, subject, main, code);

    session.setAttribute("storedVerifyCode", code);

    map.clear();
    map.put("code", code);

    return map;
  }

}

