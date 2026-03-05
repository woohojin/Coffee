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
  private final CookieDAO cookieDao;
  private final HistoryDAO historyDao;
  private final PasswordEncoder passwordEncoder;

  @Value("${COOKIE_LOGIN}")
  private String COOKIE_LOGIN;

  private void deleteCookies(HttpServletResponse response, String memberId) {
    Cookie cookieId = new Cookie("memberId", null);
    Cookie cookieToken = new Cookie("token", null);
    cookieId.setMaxAge(0);
    cookieId.setPath("/");
    cookieToken.setMaxAge(0);
    cookieToken.setPath("/");
    response.addCookie(cookieId);
    response.addCookie(cookieToken);
    cookieDao.cookieDelete(memberId);
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
                                @RequestParam MultipartFile file) {

    // MemberApiController에서 session에 isVerified = true 저장 됨
    Boolean isVerified = (Boolean) session.getAttribute("isVerified");
    if (isVerified == null || !isVerified) {
      model.addAttribute("url", "/member/memberSignUp");
      model.addAttribute("msg", "이메일 인증이 필요합니다.");
      return "alert";
    }

    session.removeAttribute("isVerified");

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
      deleteCookies(response, memberId);
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

  @RequestMapping("memberPaymentsFailure")
  public String memberPaymentsFailure() {
    return "member/memberPaymentsFailure";
  }

  @RequestMapping("memberFindAccount")
  public String memberFindAccount(Model model,
                                  @RequestParam(value = "findType", required = false, defaultValue = "id") String findType) {
    int isFind = 0;

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
  public String memberProfilePro(HttpServletRequest request, HttpSession session, Model model, MemberDTO memberDTO,
                                 String memberExistingPassword, @RequestParam MultipartFile file, @SessionAttribute String memberId) {

    MemberDTO existingMemberDTO = memberDao.memberSelectOne(memberId); // 기존 회원 정보
    memberDTO.setMemberId(memberId); // 사용자가 임의로 변경하는 것을 막기 위함

    String url = "/member/memberProfile";
    String msg = "회원 정보가 수정되었습니다.";

    String newEmail = memberDTO.getMemberEmail();
    String existingEmail = existingMemberDTO.getMemberEmail();

    if (!newEmail.equals(existingEmail)) {
      Boolean isVerified = (Boolean) session.getAttribute("isVerified");
      if (isVerified == null || !isVerified) {
        msg = "이메일 인증이 필요합니다.";
        model.addAttribute("msg", msg);
        model.addAttribute("url", url);
        return "alert";
      }
      session.removeAttribute("isVerified");
    }

    String filePath = request.getServletContext().getRealPath("/") + "view/files/";
    File uploadPath = new File(filePath);
    if (!uploadPath.exists()) {
      uploadPath.mkdirs();
    }

    String memberFile = memberDTO.getMemberFile();
    if (memberFile != null && !memberFile.trim().isEmpty()) {
      String fileName = file.getOriginalFilename();
      File uploadFile = new File(filePath, fileName);
      try {
        file.transferTo(uploadFile);
      } catch (IOException e) {
        log.error("파일 업로드 실패", e);
        msg = "파일 업로드 중 오류가 발생했습니다.";
        model.addAttribute("msg", msg);
        model.addAttribute("url", url);
        return "alert";
      }
    } else {
      memberDTO.setMemberFile(existingMemberDTO.getMemberFile());  // 기존 파일 유지 (없으면 null 값이 들어감)
    }

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
    LocalDate startLocalDate = now.minusMonths(3);

    String startDate = startLocalDate.toString();
    String endDate = now.toString();

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
}

