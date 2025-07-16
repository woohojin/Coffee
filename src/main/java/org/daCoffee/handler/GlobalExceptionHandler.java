package org.daCoffee.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.thymeleaf.exceptions.TemplateInputException;

import java.security.NoSuchAlgorithmException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  // 일반적인 모든 예외처리
  @ExceptionHandler(Exception.class)
  public String handleGeneralException(Exception ex, Model model) {
    log.error("Unexpected error occurred", ex);
    model.addAttribute("msg", "오류가 발생했습니다. 자세한 사항은 관리자에게 문의해주세요.");
    model.addAttribute("url", "/board/main");
    return "alert";
  }

  // 데이터베이스 관련 예외처리
  @ExceptionHandler(DataAccessException.class)
  public String handleDataAccessException(DataAccessException ex, Model model) {
    log.error("Database error occurred", ex);
    model.addAttribute("msg", "오류가 발생했습니다. 자세한 사항은 관리자에게 문의해주세요.");
    model.addAttribute("url", "/board/main");
    return "alert";
  }

  // Thymeleaf 관련 예외처리
  @ExceptionHandler(TemplateInputException.class)
  public String handleTemplateInputException(TemplateInputException ex, Model model) {
    log.error("Template input error occurred", ex);
    model.addAttribute("msg", "오류가 발생했습니다. 자세한 사항은 관리자에게 문의해주세요.");
    model.addAttribute("url", "/board/main");
    return "alert";
  }

  // 파일 시스템 관련 예외처리
  @ExceptionHandler(org.apache.commons.fileupload.FileUploadException.class)
  public String handleFileUploadException(org.apache.commons.fileupload.FileUploadException ex, Model model) {
    log.error("File upload error occurred", ex);
    model.addAttribute("msg", "오류가 발생했습니다. 자세한 사항은 관리자에게 문의해주세요.");
    model.addAttribute("url", "/board/main");
    return "alert";
  }

  // 암호화 관련 예외처리
  @ExceptionHandler(NoSuchAlgorithmException.class)
  public String handleNoSuchAlgorithmException(NoSuchAlgorithmException ex, Model model) {
    log.error("Encryption error occurred", ex);
    model.addAttribute("msg", "오류가 발생했습니다. 자세한 사항은 관리자에게 문의해주세요.");
    model.addAttribute("url", "/board/main");
    return "alert";
  }
}
