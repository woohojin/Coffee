package org.daCoffee.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dao.CookieDAO;
import org.daCoffee.dto.request.MemberSignUpRequestDTO;
import org.daCoffee.dto.request.MemberUpdateRequestDTO;
import org.daCoffee.entity.Member;
import org.daCoffee.entity.OrderHistory;
import org.daCoffee.jwt.JwtUserDetails;
import org.daCoffee.service.MemberService;
import org.daCoffee.service.OrderHistoryService;
import org.daCoffee.service.RedisService;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/member/")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j

public class MemberController {
  private final MemberService memberService;
  private final CookieDAO cookieDao;
  private final OrderHistoryService orderHistoryService;
  private final PasswordEncoder passwordEncoder;
  private final RedisService redisService;

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
  public String memberSignUpPro(HttpServletRequest request, Model model, MemberSignUpRequestDTO signUpDTO,
                                @RequestParam MultipartFile file) {

    String memberEmail = signUpDTO.getMemberEmail();
    if (!redisService.isVerified(memberEmail)) {
      model.addAttribute("url", "/member/memberSignUp");
      model.addAttribute("msg", "이메일 인증이 필요합니다.");
      return "alert";
    }
    redisService.deleteVerified(memberEmail);

    String msg = "회원 가입 중 문제가 발생 했습니다. 다시 시도해주세요.";
    String url = "/member/memberSignUp";

    String memberId = signUpDTO.getMemberId().toLowerCase();

    String filePath = request.getServletContext().getRealPath("/") + "view/files/";
    File uploadPath = new File(filePath);
    if (!uploadPath.exists()) {
      uploadPath.mkdirs();
    }

    try {
      if(!memberService.existsById(memberId)) {
        String memberCompanyName = signUpDTO.getMemberCompanyName();
        String memberCompanyTel = signUpDTO.getMemberCompanyTel();
        String memberFile = signUpDTO.getMemberFile();

        if(memberFile != null && !memberFile.trim().isEmpty()) {
          File uploadFile = new File(filePath, file.getOriginalFilename());
          try {
            file.transferTo(uploadFile);
          } catch (IOException e) {
            log.error("파일 업로드 실패", e);
            model.addAttribute("msg", "파일 업로드 중 오류가 발생했습니다.");
            model.addAttribute("url", "/member/memberSignUp");
            return "alert";
          }
        } else {
          memberFile = null;
        }

        if (memberCompanyName != null && memberCompanyName.trim().isEmpty()) memberCompanyName = null;
        if (memberCompanyTel != null && memberCompanyTel.trim().isEmpty()) memberCompanyTel = null;

        Member member = Member.builder()
          .memberId(memberId)
          .memberName(signUpDTO.getMemberName())
          .memberCompanyName(memberCompanyName)
          .memberPassword(passwordEncoder.encode(signUpDTO.getMemberPassword()))
          .memberTel(signUpDTO.getMemberTel())
          .memberCompanyTel(memberCompanyTel)
          .memberAddress(signUpDTO.getMemberAddress())
          .memberDetailAddress(signUpDTO.getMemberDetailAddress())
          .memberDeliveryAddress(signUpDTO.getMemberDeliveryAddress())
          .memberDetailDeliveryAddress(signUpDTO.getMemberDetailDeliveryAddress())
          .memberEmail(signUpDTO.getMemberEmail())
          .memberFile(memberFile)
          .memberTier(0)
          .memberDisabledStatus(false)
          .memberDate(LocalDate.now())
          .build();

        memberService.save(member);

        msg = memberId + "님의 가입이 완료되었습니다.";
        url = "/member/memberSignIn";
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
  public String memberWithdrawalPro(Model model, HttpServletResponse response, String memberPassword,
                                    @AuthenticationPrincipal JwtUserDetails userDetails) {

    String memberId = userDetails.getMemberId();
    Member member = memberService.findById(memberId)
      .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + memberId));

    String msg = "회원 탈퇴에 실패했습니다.";
    String url = "/member/memberWithdrawal";

    if (passwordEncoder.matches(memberPassword, member.getMemberPassword())) {
      deleteCookies(response, memberId);
      memberService.withdrawMember(memberId);

      ResponseCookie accessCookie = ResponseCookie.from("accessToken", "")
              .httpOnly(true).sameSite("Lax").path("/").maxAge(0).build();
      ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
              .httpOnly(true).sameSite("Lax").path("/api/auth/refresh").maxAge(0).build();

      response.addHeader("Set-Cookie", accessCookie.toString());
      response.addHeader("Set-Cookie", refreshCookie.toString());

      msg = "회원 탈퇴에 성공했습니다.";
      url = "/main";
    }

    model.addAttribute("msg", msg);
    model.addAttribute("url", url);

    return "alert";
  }

  @GetMapping("memberCart")
  public String memberCart(Model model,
                           @AuthenticationPrincipal JwtUserDetails userDetails) {

    String memberId = userDetails.getMemberId();
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
  public String memberProfile(Model model,
                              @AuthenticationPrincipal JwtUserDetails userDetails) {

    String memberId = userDetails.getMemberId();
    Member member = memberService.findById(memberId)
      .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + memberId));

    model.addAttribute("member", member);

    return "member/memberProfile";
  }

  @RequestMapping("memberProfilePro")
  public String memberProfilePro(HttpServletRequest request, Model model,
                                 MemberUpdateRequestDTO updateDTO, String memberExistingPassword,
                                 @RequestParam(required = false) MultipartFile file, @AuthenticationPrincipal JwtUserDetails userDetails) {

    String memberId = userDetails.getMemberId();
    String memberEmail = updateDTO.getMemberEmail();
    Member existingMember = memberService.findById(memberId)
      .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + memberId));

    String url = "/member/memberProfile";
    String msg = "회원 정보가 수정되었습니다.";

    if (!updateDTO.getMemberEmail().equals(existingMember.getMemberEmail())) {
      if (!redisService.isVerified(memberEmail)) {
        model.addAttribute("msg", "이메일 인증이 필요합니다.");
        model.addAttribute("url", url);
        return "alert";
      }
      redisService.deleteVerified(memberEmail);
    }

    String filePath = request.getServletContext().getRealPath("/") + "view/files/";
    File uploadPath = new File(filePath);
    if (!uploadPath.exists()) {
      uploadPath.mkdirs();
    }

    String memberFile = updateDTO.getMemberFile();
    if (memberFile != null && !memberFile.trim().isEmpty()) {
      File uploadFile = new File(filePath, file.getOriginalFilename());
      try {
        file.transferTo(uploadFile);
      } catch (IOException e) {
        log.error("파일 업로드 실패", e);
        model.addAttribute("msg", "파일 업로드 중 오류가 발생했습니다.");
        model.addAttribute("url", url);
        return "alert";
      }
    } else {
      updateDTO.setMemberFile(existingMember.getMemberFile());  // 기존 파일 유지 (없으면 null 값이 들어감)
    }

    if(memberExistingPassword != null) {
      if(passwordEncoder.matches(memberExistingPassword, existingMember.getMemberPassword())) { // 입력한 기존 비밀번호와 db의 비밀번호가 일치 할 때 변경
        if (updateDTO.getMemberPassword() == null || updateDTO.getMemberPassword().isEmpty()) {
          memberService.updateWithoutPassword(memberId, updateDTO);
        } else {
          memberService.update(memberId, passwordEncoder.encode(updateDTO.getMemberPassword()), updateDTO);
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
  public String memberHistory(HttpServletRequest request,
                              @AuthenticationPrincipal JwtUserDetails userDetails) {

    String memberId = userDetails.getMemberId();
    LocalDate now = LocalDate.now();
    LocalDate startLocalDate = now.minusMonths(3);

    String startDate = startLocalDate.toString();
    String endDate = now.toString();

    List<OrderHistory> list = orderHistoryService.findByMemberIdBetween(
      memberId,
      LocalDateTime.of(startLocalDate, LocalTime.MIN),
      LocalDateTime.of(now, LocalTime.MAX)
    );

    request.setAttribute("startDate", startDate);
    request.setAttribute("endDate", endDate);
    request.setAttribute("historyCount", list.size());
    request.setAttribute("list", list);

    return "member/memberHistory";
  }

  @RequestMapping("memberHistoryPro")
  public String memberHistoryPro(HttpServletRequest request,
                                 @RequestParam String startDate,
                                 @RequestParam String endDate,
                                 @AuthenticationPrincipal JwtUserDetails userDetails) {

    String memberId = userDetails.getMemberId();
    List<OrderHistory> list = orderHistoryService.findByMemberIdBetween(
      memberId,
      LocalDateTime.parse(startDate + "T00:00:00"),
      LocalDateTime.parse(endDate + "T23:59:59")
    );

    request.setAttribute("startDate", startDate);
    request.setAttribute("endDate", endDate);
    request.setAttribute("historyCount", list.size());
    request.setAttribute("list", list);

    return "member/memberHistory";
  }
}

