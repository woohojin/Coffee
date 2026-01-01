package org.daCoffee.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dao.CartDAO;
import org.daCoffee.dao.CookieDAO;
import org.daCoffee.dao.HistoryDAO;
import org.daCoffee.dao.MemberDAO;
import org.daCoffee.dto.*;
import org.daCoffee.module.UUIDGenerateModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.daCoffee.service.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

import static org.daCoffee.util.SecurityUtil.getRandomPassword;

@Controller
@RequestMapping("/member/")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j

public class MemberController {
  private final MemberDAO memberDao;
  private final CartDAO cartDao;
  private final CookieDAO cookieDao;
  private final HistoryDAO historyDao;
  private final PasswordEncoder passwordEncoder;
  private final MailService mailService;
  private final PriceCalculator priceCalculator;

  @Value("${COOKIE_LOGIN}")
  private String COOKIE_LOGIN;

  public void sendEmail(String toEmail, String subject, String main, String code) {
    mailService.sendEmail(toEmail, subject, main, code);
  }

  @RequestMapping("memberTerms")
  public String memberTerms() {
    return "member/memberTerms";
  }

  @RequestMapping("memberSignUp")
  public String memberSignUp() {
    return "member/memberSignUpForm";
  }

  @RequestMapping("memberSignUpPro")
  public String memberSignUpPro(HttpServletRequest request, HttpSession session, Model model, MemberDTO memberDTO,
                                @RequestParam MultipartFile file,
                                @RequestParam String verifyCode,
                                @SessionAttribute String storedVerifyCode) {

    if(verifyCode == null || verifyCode.equals("timeout")) {
      String url = "/member/memberSignUp";
      String msg = "인증시간이 초과되었습니다.";

      model.addAttribute("url", url);
      model.addAttribute("msg", msg);

      return "alert";
    }

    if(!verifyCode.equals(storedVerifyCode)) {
      String url = "/member/memberSignUp";
      String msg = "인증번호가 일치하지 않습니다.";

      model.addAttribute("url", url);
      model.addAttribute("msg", msg);

      return "alert";
    }

    String msg = "회원 가입 중 문제가 발생 했습니다. 다시 시도해주세요.";
    String url = "/member/memberSignUp";

    String memberId = memberDTO.getMemberId().toLowerCase();
    memberDTO.setMemberId(memberId);

    MemberDTO mem = null;

    try{
      mem = memberDao.memberSelectOne(memberId);
    } catch (Exception e) {
      log.error(e.getMessage());
    }

    String filePath = request.getServletContext().getRealPath("/") + "view/files/";
    String fileName = file.getOriginalFilename();
    File uploadFile = new File(filePath, fileName);
    File uploadPath = new File(filePath);

    if (!uploadPath.exists()) {
      uploadPath.mkdirs(); // 경로가 없으면 생성
    }

    try{
      if(mem == null) {
        memberDTO.setMemberPassword(passwordEncoder.encode(memberDTO.getMemberPassword()));

        String memberCompanyName = memberDTO.getMemberCompanyName();
        String memberCompanyTel = memberDTO.getMemberCompanyTel();
        String memberFile = memberDTO.getMemberFile();

        if(memberFile != null && !memberFile.trim().isEmpty()) {
          file.transferTo(uploadFile);
        } else {
          memberDTO.setMemberFile(null);
        }

        if(memberCompanyName != null && memberCompanyName.trim().isEmpty()) {
          memberDTO.setMemberCompanyName(null);
        }

        if(memberCompanyTel != null && memberCompanyTel.trim().isEmpty()) {
          memberDTO.setMemberCompanyTel(null);
        }

        int num = memberDao.memberInsert(memberDTO);

        if (num > 0) {
          msg = memberId + "님의 가입이 완료되었습니다.";
          url = "/member/memberSignIn";
        } else {
          msg = "회원가입을 실패 했습니다.";
          url = "/member/memberSignUp";
        }
      } else {
        msg = "이미 있는 아이디 입니다.";
        url = "/member/memberSignUp";
      }
    } catch (Exception e) {
      log.error("Error is occurred :", e);
    }

    model.addAttribute("msg", msg);
    model.addAttribute("url", url);

    return "alert";
  }

  @RequestMapping("memberSignIn")
  public String memberSignIn() {
    return "member/memberSignInForm";
  }

//  @PostMapping("memberSignInPro")
//  public String memberSignInPro(HttpSession session, HttpServletResponse response, Model model, String memberId, String memberPassword, String autoLogin,
//                                @SessionAttribute(name="memberId", required=false) String sessionMemberId) {
//    SecurityUtil.SHA256 sha256 = new SecurityUtil.SHA256();
//
//    String msg = "";
//    String url = "/member/memberSignIn";
//
//    MemberDTO memberDTO = memberDao.memberSelectOne(memberId);
//    int isDisabled = memberDao.disabledMemberSelectOne(memberId);
//
//    if(sessionMemberId != null) {
//
//      msg = "이미 로그인된 상태입니다.";
//      url = "/main";
//
//      model.addAttribute("msg", msg);
//      model.addAttribute("url", url);
//
//      return "alert";
//    }
//
//    if(memberDTO != null) {
//      if(isDisabled < 1) {
//        if(passwordEncoder.matches(memberPassword, memberDTO.getMemberPassword())) {
//          Integer memberTier = memberDTO.getMemberTier();
//          session.setAttribute("memberId", memberId);
//          session.setAttribute("memberTier", memberTier);
//
//          log.info("memberTier : {}", session.getAttribute("memberTier"));
//
//          List<SimpleGrantedAuthority> authorities = memberTier == 9 ?
//            Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")) :
//            Collections.emptyList();
//          Authentication auth = new UsernamePasswordAuthenticationToken(memberId, null, authorities);
//          SecurityContextHolder.getContext().setAuthentication(auth);
//
//          SecurityContext context = SecurityContextHolder.createEmptyContext();
//          context.setAuthentication(auth);
//          session.setAttribute("SPRING_SECURITY_CONTEXT", context);
//
//          url = "/main";
//
//          if(autoLogin != null) {
//            String encryptKey = memberDTO.getMemberId() + COOKIE_LOGIN;
//
//            try {
//              String token = sha256.encrypt(encryptKey);
//
//              int num = cookieDao.cookieInsert(memberId, token);
//
//              if(num > 0) {
//                Cookie cookieId = new Cookie("memberId", memberId);
//                Cookie cookieToken = new Cookie("token", token);
//                cookieId.setMaxAge(60 * 60 * 24 * 30); // 60 * 60 * 24 * 30 == 30days
//                cookieId.setPath("/");
//                cookieToken.setMaxAge(60 * 60 * 24 * 30);
//                cookieToken.setPath("/");
//                response.addCookie(cookieId);
//                response.addCookie(cookieToken);
//              }
//            } catch (NoSuchAlgorithmException e) {
//              log.error("Encrypt failed : ", e);
//              model.addAttribute("msg", "로그인 중 에러가 발생했습니다. 자세한 사항은 관리자에게 문의해주세요.");
//              model.addAttribute("url", url);
//
//              return "alert";
//            }
//            cookieDao.cookieDelete(memberId);
//          }
//          return "redirect:/main";
//        } else {
//          msg = "비밀번호가 틀립니다.";
//        }
//      } else {
//        msg = "비활성화 된 아이디입니다."; // disable 된 아이디 일 떄
//      }
//    } else {
//      msg = "존재하지 않는 아이디입니다."; // db에 해당 아이디가 없을 때
//    }
//
//    model.addAttribute("msg", msg);
//    model.addAttribute("url", url);
//
//    return "alert";
//  }

//  @RequestMapping("memberLogout")
//  public String memberLogout(HttpSession session, Model model, HttpServletResponse response,
//                             @SessionAttribute String memberId) {
//
//    Cookie cookieId = new Cookie("memberId", null);
//    Cookie cookieToken = new Cookie("token", null);
//    cookieId.setMaxAge(0); // 0초 = 쿠키 삭제
//    cookieId.setPath("/");
//    cookieToken.setMaxAge(0);
//    cookieToken.setPath("/");
//    response.addCookie(cookieId);
//    response.addCookie(cookieToken);
//
//    cookieDao.cookieDelete(memberId);
//
//    String msg = "로그아웃 되었습니다.";
//    String url = "/main";
//
//    session.invalidate();
//
//    model.addAttribute("msg", msg);
//    model.addAttribute("url", url);
//
//    return "alert";
//  }

  @RequestMapping("memberWithdrawal")
  public String memberWithdrawal() {
    return "/member/memberWithdrawal";
  }

  @RequestMapping("memberWithdrawalPro")
  public String memberWithdrawalPro(HttpSession session, Model model, HttpServletResponse response, String memberPassword,
                                    @SessionAttribute String memberId) {

    MemberDTO memberDTO = memberDao.memberSelectOne(memberId);

    String msg = "회원 탈퇴에 실패했습니다.";
    String url = "/member/memberWithdrawal";

    if(passwordEncoder.matches(memberPassword, memberDTO.getMemberPassword())) {
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
      url = "/main";
    }

    model.addAttribute("msg", msg);
    model.addAttribute("url", url);

    return "alert";
  }

  @GetMapping("memberCart")
  public String memberCart(Model model,
                           @SessionAttribute String memberId) {

    model.addAttribute("memberId", memberId);

    return "member/memberCart";
  }

  @RequestMapping("memberCartPro")
  public String memberCartPro(HttpServletRequest request, HttpSession session, Model model,
                              @RequestParam(defaultValue = "9") int status,
                              @RequestParam(defaultValue = "1") int quantity,
                              @RequestParam(defaultValue = "0") int productGrinding,
                              @RequestParam String productCode,
                              @SessionAttribute String memberId) {

    // ==== 장바구니 제품 갯수 변경 ====
    if(status == 0) { // status 0 = delete || status 1 = updateQuantity || status 2 = updateGrindingType
      cartDao.cartDelete(memberId, productCode);
    } else if (status == 1) {
      cartDao.cartQuantityUpdate(memberId, productCode, quantity);
    } // else if (status == 2) {
    //  cartDao.cartGrindingUpdate(memberId, productCode, productGrinding);
   // }

    // ==== 제품 갯수 변경 후 가격 계산 ====
    CartPriceDTO cartPriceDTO;

    cartPriceDTO = priceCalculator.calculatePrice(memberId);

    final int totalPrice = cartPriceDTO.getTotalPrice();
    final int sumPrice = cartPriceDTO.getSumPrice();
    final int deliveryFee = cartPriceDTO.getDeliveryFee();
    final int cartCount = cartPriceDTO.getCartCount();

    List<CartDTO> list = cartDao.cartSelectMember(memberId);

    session.setAttribute("totalPrice", totalPrice);
    model.addAttribute("sumPrice", sumPrice);
    model.addAttribute("deliveryFee", deliveryFee);
    model.addAttribute("cartCount", cartCount);
    model.addAttribute("list", list);

    return "member/memberCart";
  }

  @RequestMapping("memberPayments")
  public String memberPayments(HttpSession session, Model model,
                               @SessionAttribute String memberId,
                               @SessionAttribute final Integer totalPrice) {

    UUIDGenerateModule uuidGenerateModule = new UUIDGenerateModule();

    String errorCode = "CANNOT_FIND_MEMBER_ID";
    String errorMessage = "로그인 후 결제를 진행해주세요.";

    if(memberId != null) {
      MemberDTO memberDTO = memberDao.memberSelectOne(memberId);
      if(memberDTO != null) {
        String orderId = uuidGenerateModule.generateOrderId();
        String customerKey = uuidGenerateModule.generateCustomerKey(memberId);
        List<String> productNames = new ArrayList<>();

        if(totalPrice != null) {
          List<CartDTO> list = cartDao.cartSelectMember(memberId);

          if(list != null) {
            for (CartDTO cartDTO : list) {
              productNames.add(cartDTO.getProductName());
            }

            String orderName = productNames.get(0) + " 외 " + (productNames.size() - 1) + "건";

            session.setAttribute("orderId", orderId);
            session.setAttribute("customerKey", customerKey);
            model.addAttribute("orderName", orderName);
            model.addAttribute("member", memberDTO);

            return "member/memberPayments";
          }
          errorCode = "CANNOT_FIND_CART_ITEMS";
          errorMessage = "장바구니에 상품이 존재하지 않습니다.";

          model.addAttribute("errorCode", errorCode);
          model.addAttribute("errorMessage", errorMessage);

          return "member/memberPaymentsFailure";
        }

        errorCode = "CANNOT_FIND_VALUE_INFO";
        errorMessage = "가격정보가 존재하지 않습니다.";

        model.addAttribute("errorCode", errorCode);
        model.addAttribute("errorMessage", errorMessage);

        return "member/memberPaymentsFailure";
      }
    }

    model.addAttribute("errorCode", errorCode);
    model.addAttribute("errorMessage", errorMessage);

    return "member/memberPaymentsFailure";
  }

  @RequestMapping("memberPaymentsSuccess")
  public String memberPaymentsSuccess(HttpSession session, Model model,
                                      @SessionAttribute String memberId,
                                      @SessionAttribute String orderId,
                                      @SessionAttribute final Integer totalPrice) {

    String errorCode = "CANNOT_FIND_MEMBER_ID";
    String errorMessage = "로그인 후 결제를 진행해주세요.";

    if(memberId != null) {
      MemberDTO memberDTO = memberDao.memberSelectOne(memberId);
      if (memberDTO != null) {

        if (totalPrice != null) {
          List<CartDTO> list = cartDao.cartSelectMember(memberId);

          if (list != null) {
            for (CartDTO cartDTO : list) {
              HistoryDTO historyDTO = new HistoryDTO();

              historyDTO.setOrderId(orderId);
              historyDTO.setMemberTier(memberDTO.getMemberTier());
              historyDTO.setMemberId(memberId);
              historyDTO.setMemberName(memberDTO.getMemberName());
              historyDTO.setMemberFranCode(memberDTO.getMemberFranCode());
              historyDTO.setProductCode(cartDTO.getProductCode());
              historyDTO.setQuantity(cartDTO.getQuantity());
              historyDTO.setDeliveryAddress(memberDTO.getMemberDeliveryAddress());
              historyDTO.setDetailDeliveryAddress(memberDTO.getMemberDetailDeliveryAddress());
              historyDTO.setTotalPrice(totalPrice);

              historyDao.historyInsert(historyDTO);
            }
            cartDao.deleteCartByMember(memberId);
            session.removeAttribute("orderId");
            session.removeAttribute("totalPrice");

            return "member/memberPaymentsSuccess";
          }
          errorCode = "CANNOT_FIND_CART_ITEMS";
          errorMessage = "장바구니에 상품이 존재하지 않습니다.";

          model.addAttribute("errorCode", errorCode);
          model.addAttribute("errorMessage", errorMessage);

          return "member/memberPaymentsFailure";
        }
        errorCode = "CANNOT_FIND_VALUE_INFO";
        errorMessage = "가격정보가 존재하지 않습니다.";

        model.addAttribute("errorCode", errorCode);
        model.addAttribute("errorMessage", errorMessage);

        return "member/memberPaymentsFailure";
      }
    }
    model.addAttribute("errorCode", errorCode);
    model.addAttribute("errorMessage", errorMessage);

    return "member/memberPaymentsFailure";
  }

  @RequestMapping("memberPaymentsFailure")
  public String memberPaymentsFailure() {
    return "member/memberPaymentsFailure";
  }

  @PostMapping("memberPaymentsConfirm")
  @ResponseBody
  public ResponseEntity<Object> memberPaymentsConfirm(@RequestBody PaymentsRequestDTO paymentsRequestDTO) {
    try{
      String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
      String encodedSecretKey = "Basic " + Base64.getEncoder().encodeToString((widgetSecretKey + ":").getBytes());

      String apiUrl = "https://api.tosspayments.com/v1/payments/confirm";

      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", encodedSecretKey);
      headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

      // RestTemplate을 사용하여 API 호출
      ResponseEntity<Map<String, Object>> responseEntity = new RestTemplate().exchange(
              apiUrl,
              HttpMethod.POST,
              new HttpEntity<>(paymentsRequestDTO, headers),
              new ParameterizedTypeReference<Map<String, Object>>() {
              }
      );

      if (responseEntity.getStatusCode().is2xxSuccessful()) {
        // 결제 성공 로직
        log.info(Objects.requireNonNull(responseEntity.getBody()).toString());
        return ResponseEntity.ok(responseEntity.getBody());
      } else {
        // 결제 실패 로직
        log.info(Objects.requireNonNull(responseEntity.getBody()).toString());
        return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
      }

    } catch (Exception e) {
      log.error("Error in memberPaymentsConfirm", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Internal Server Error"));
    }
  }

  @RequestMapping("memberFindAccount")
  public String memberFindAccount(Model model,
                                  @RequestParam(value = "findType", required = false, defaultValue = "id") String findType) {
    int isFind = 0;

    model.addAttribute("isFind", isFind);
    model.addAttribute("findType", findType);

    return "member/memberFindAccount";
  }

  @RequestMapping("memberFindAccountPro")
  public String memberFindAccountPro(HttpServletRequest request, HttpSession session, Model model,
                                     @RequestParam(value = "findType", required = false, defaultValue = "id") String findType,
                                     @RequestParam(required = false) String memberName,
                                     @RequestParam(required = false) String memberEmail,
                                     @RequestParam(required = false) String memberId,
                                     @RequestParam String verifyCode,
                                     @SessionAttribute String storedVerifyCode) {

    if(verifyCode == null || verifyCode.equals("timeout")) { // 인증시간이 초과되면 verifyCode가 timeout으로 바뀜
      String url = "/member/memberFindAccount";
      String msg = "인증시간이 초과되었습니다.";

      model.addAttribute("url", url);
      model.addAttribute("msg", msg);

      return "alert";
    }

    if(!verifyCode.equals(storedVerifyCode)) { // 사용자가 입력한 코드와 생성된 코드가 일치하는지 확인
      String url = "/member/memberFindAccount";
      String msg = "인증번호가 일치하지 않습니다.";

      model.addAttribute("url", url);
      model.addAttribute("msg", msg);

      return "alert";
    }

    int isFind = 0;

    if(findType.equals("id")) {
      List<MemberDTO> list = memberDao.memberFindId(memberName, memberEmail);

      if(list != null && !list.isEmpty()) {
        isFind = 1;
        request.setAttribute("list", list);
      } else {
        String url = "/member/memberFindAccount";
        String msg = "아이디가 존재하지 않거나 이름 혹은 이메일이 일치하지 않습니다.";

        model.addAttribute("url", url);
        model.addAttribute("msg", msg);
        return "alert";
      }
    }

    if(findType.equals("password")) {
      String memberPassword = memberDao.memberFindPassword(memberId, memberEmail);

      String url = "/member/memberFindAccount";
      String msg = "존재하지 않는 아이디 혹은 이메일이 일치하지 않습니다."; // 밑의 if문의 조건에 부합하지 않을 경우 사용 됨

      if(memberPassword != null && !memberPassword.isEmpty()) {
        String subject = "다올커피 - 비밀번호가 임시 비밀번호로 변경되었습니다.";
        String main = "회원님의 임시 비밀번호는";
        String password = getRandomPassword(8);
        sendEmail(memberEmail, subject, main, password);

        String tempPassword = passwordEncoder.encode(password);
        memberDao.memberTempPasswordUpdate(memberId, tempPassword);

        url = "/member/memberSignIn";
        msg = "임시비밀번호가 이메일로 전송되었습니다.";
      }

      model.addAttribute("url", url);
      model.addAttribute("msg", msg);
      return "alert";
    }

    model.addAttribute("isFind", isFind);
    model.addAttribute("findType", findType);

    return "member/memberFindAccount";
  }

  @RequestMapping("memberMyPage")
  public String memberMyPage() {

    return "member/memberMyPage";
  }

  @RequestMapping("memberProfile")
  public String memberProfile(HttpSession session, Model model,
                              @SessionAttribute String memberId) {

    MemberDTO memberDTO = memberDao.memberSelectOne(memberId);

    model.addAttribute("member", memberDTO);

    return "member/memberProfile";
  }

  @RequestMapping("memberProfilePro")
  public String memberProfilePro(HttpSession session, Model model, MemberDTO memberDTO, String memberExistingPassword,
                                 @SessionAttribute String memberId) {

    MemberDTO existingMemberDTO = memberDao.memberSelectOne(memberId); // 기존 회원 정보
    memberDTO.setMemberId(memberId); // 사용자가 임의로 변경하는 것을 막기 위함

    String url = "/member/memberProfile";
    String msg = "회원 정보가 수정되었습니다.";

    if(memberExistingPassword != null) {
      if(passwordEncoder.matches(memberExistingPassword, existingMemberDTO.getMemberPassword())) { // 입력한 기존 비밀번호와 db의 비밀번호가 일치 할 때 변경
        if(memberDTO.getMemberPassword() == null || memberDTO.getMemberPassword().isEmpty()) {
          memberDao.memberUpdateNotPassword(memberDTO);
        } else {
          memberDTO.setMemberPassword(passwordEncoder.encode(memberDTO.getMemberPassword()));
          memberDao.memberUpdate(memberDTO);
        }
      } else {
        msg = "기존 비밀번호가 틀렸습니다.";
      }
    }

    model.addAttribute("url", url);
    model.addAttribute("msg", msg);

    return "alert";
  }

  @RequestMapping("memberHistory")
  public String memberHistory(HttpServletRequest request, HttpSession session,
                              @SessionAttribute String memberId) {

    LocalDate now = LocalDate.now();
    int year = now.getYear();
    int dayOfMonth = now.getDayOfMonth();
    int monthValue = now.getMonthValue() - 3;
    String month = String.valueOf(monthValue);
    String day = String.valueOf(dayOfMonth);

    if(monthValue < 10 ) {
      month = "0" + monthValue;
    }

    if(monthValue <= 2) {
      month = "01";
    }

    if(dayOfMonth < 10 ) {
      day = "0" + dayOfMonth;
    }

    String startDate = year + "-" + month + "-" + day;
    String endDate = String.valueOf(now);

    List<HistoryDTO> list = historyDao.historySelectBetween(memberId, startDate, endDate);
    int historyCount = historyDao.historyCountBetween(memberId, startDate, endDate);

    request.setAttribute("startDate", startDate);
    request.setAttribute("endDate", endDate);
    request.setAttribute("historyCount", historyCount);
    request.setAttribute("list", list);

    return "member/memberHistory";
  }

  @RequestMapping("memberHistoryPro")
  public String memberHistoryPro(HttpServletRequest request, HttpSession session,
                                 @RequestParam String startDate,
                                 @RequestParam String endDate,
                                 @SessionAttribute String memberId) {

    List<HistoryDTO> list = historyDao.historySelectBetween(memberId, startDate, endDate);
    int historyCount = historyDao.historyCountBetween(memberId, startDate, endDate);

    request.setAttribute("startDate", startDate);
    request.setAttribute("endDate", endDate);
    request.setAttribute("historyCount", historyCount);
    request.setAttribute("list", list);

    return "member/memberHistory";
  }

  @ResponseBody
  @PostMapping(value = "verifyEmail", produces = "application/json")
  public Map<String, Object> verifyEmail(HttpSession session, String memberEmail) {
    Map<String, Object> map = new HashMap<>();

    try{
      String code = getRandomPassword(6);
      String subject = "다올커피 - 이메일 인증번호가 도착했습니다.";
      String main = "회원님의 이메일 인증번호는";

      sendEmail(memberEmail, subject, main, code);

      session.setAttribute("storedVerifyCode", code);

      map.clear();
      map.put("code", code);

    } catch (Exception e) {
      log.error("이메일 전송 실패 : ", e);
      map.put("error", "이메일 전송 실패 : " + e.getMessage());
      return map;
    }

    log.debug("응답 : {}", map);
    return map;
  }

}

