package org.daCoffee.handler;

import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dto.ApiResponseDTO;
import org.daCoffee.exception.BusinessException;
import org.daCoffee.exception.ForbiddenException;
import org.daCoffee.exception.NotFoundException;
import org.daCoffee.exception.UnauthorizedException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.thymeleaf.exceptions.TemplateInputException;

import java.security.NoSuchAlgorithmException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // ========== Business ========== //

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiResponseDTO<Void>> handleBusinessException(BusinessException e) {
    log.error("Business Exception: {}", e.getMessage());

    ApiResponseDTO<Void> response = ApiResponseDTO.error(
      e.getMessage(),
      e.getRedirectUrl(),
      e.getStatusCode()
    );

    return ResponseEntity
      .status(e.getStatusCode())
      .body(response);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ApiResponseDTO<Void>> handleUnauthorizedException(UnauthorizedException e) {
    log.error("Unauthorized: {}", e.getMessage());

    ApiResponseDTO<Void> response = ApiResponseDTO.error(
      e.getMessage(),
      e.getRedirectUrl(),
      401
    );

    return ResponseEntity
      .status(HttpStatus.UNAUTHORIZED)
      .body(response);
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ApiResponseDTO<Void>> handleForbiddenException(ForbiddenException e) {
    log.error("Forbidden: {}", e.getMessage());

    ApiResponseDTO<Void> response = ApiResponseDTO.error(
      e.getMessage(),
      e.getRedirectUrl(),
      403
    );

    return ResponseEntity
      .status(HttpStatus.FORBIDDEN)
      .body(response);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiResponseDTO<Void>> handleNotFoundException(NotFoundException e) {
    log.error("Not Found: {}", e.getMessage());

    ApiResponseDTO<Void> response = ApiResponseDTO.error(
      e.getMessage(),
      404
    );

    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(response);
  }

  // ========== Spring Security ========== //

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiResponseDTO<Void>> handleAuthenticationException(AuthenticationException e) {
    log.error("Authentication Exception: {}", e.getMessage());

    ApiResponseDTO<Void> response = ApiResponseDTO.error(
      "로그인이 필요합니다.",
      "/member/memberSignIn",
      401
    );

    return ResponseEntity
      .status(HttpStatus.UNAUTHORIZED)
      .body(response);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiResponseDTO<Void>> handleAccessDeniedException(AccessDeniedException e) {
    log.error("Access Denied: {}", e.getMessage());

    ApiResponseDTO<Void> response = ApiResponseDTO.error(
      "권한이 부족합니다.",
      "/member/memberSignIn",
      403
    );

    return ResponseEntity
      .status(HttpStatus.FORBIDDEN)
      .body(response);
  }

  // ========== Spring ========== //

  @ExceptionHandler(org.apache.commons.fileupload.FileUploadException.class)
  public ResponseEntity<ApiResponseDTO<Void>> handleFileUploadException(org.apache.commons.fileupload.FileUploadException e) {
    log.error("File upload error occurred", e);

    ApiResponseDTO<Void> response = ApiResponseDTO.error(
      "파일 업로드 중 오류가 발생했습니다.",
      400
    );

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(response);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ApiResponseDTO<Void>> handleNoHandlerFoundException(NoHandlerFoundException e) {
    log.error("Page not found: {}", e.getRequestURL(), e);

    ApiResponseDTO<Void> response = ApiResponseDTO.error(
      "요청한 리소스를 찾을 수 없습니다.",
      404
    );

    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(response);
  }

  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<ApiResponseDTO<Void>> handleDataAccessException(DataAccessException e) {
    log.error("Database error occurred", e);

    ApiResponseDTO<Void> response = ApiResponseDTO.error(
      "데이터베이스 오류가 발생했습니다.",
      500
    );

    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(response);
  }

  @ExceptionHandler(TemplateInputException.class)
  public ResponseEntity<ApiResponseDTO<Void>> handleTemplateInputException(TemplateInputException e) {
    log.error("Template input error occurred", e);

    ApiResponseDTO<Void> response = ApiResponseDTO.error(
      "템플릿 오류가 발생했습니다.",
      500
    );

    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(response);
  }

  @ExceptionHandler(NoSuchAlgorithmException.class)
  public ResponseEntity<ApiResponseDTO<Void>> handleNoSuchAlgorithmException(NoSuchAlgorithmException e) {
    log.error("Encryption error occurred", e);

    ApiResponseDTO<Void> response = ApiResponseDTO.error(
      "암호화 처리 중 오류가 발생했습니다.",
      500
    );

    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponseDTO<Void>> handleGeneralException(Exception e) {
    log.error("Unexpected error occurred", e);

    ApiResponseDTO<Void> response = ApiResponseDTO.error(
      "서버 오류가 발생했습니다. 관리자에게 문의해주세요.",
      500
    );

    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(response);
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiResponseDTO<Void>> handleResponseStatusException(ResponseStatusException e) {
    log.error("Response status error occurred: {}", e.getReason(), e);

    String message = e.getReason() != null ? e.getReason() : "서버 오류가 발생했습니다.";

    ApiResponseDTO<Void> response = ApiResponseDTO.error(
      message,
      e.getStatusCode().value()
    );

    return ResponseEntity
      .status(e.getStatusCode())
      .body(response);
  }
}
