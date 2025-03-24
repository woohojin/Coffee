package org.daCoffee.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class CustomErrorHandler implements ErrorController {
  private static final Logger logger = LoggerFactory.getLogger(CustomErrorHandler.class);

  @RequestMapping("/error")
  public String handleError(HttpServletRequest request, Model model, HttpSession session) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
    Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
    Object requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

    Object sessionStatus = session.getAttribute("errorStatus");
    Object sessionMessage = session.getAttribute("errorMessage");

    logger.info("Error occurred: Status = {}, Message = {}, Exception = {}, URI = {}, Session Status = {}, Session Message = {}",
      status, message, exception, requestUri, sessionStatus, sessionMessage);

    // 상태 코드 처리
    if (status != null) {
      int statusCode = Integer.parseInt(status.toString());
      model.addAttribute("statusCode", statusCode);
      model.addAttribute("error", HttpStatus.valueOf(statusCode).getReasonPhrase());
    } else if (sessionStatus != null) {
      model.addAttribute("statusCode", sessionStatus);
      model.addAttribute("error", HttpStatus.valueOf(Integer.parseInt(sessionStatus.toString())).getReasonPhrase());
    } else {
      model.addAttribute("statusCode", "N/A");
      model.addAttribute("error", "Unknown");
    }

    // 메시지 처리
    model.addAttribute("message", message != null ? message : sessionMessage != null ? sessionMessage : "알 수 없는 에러");
    model.addAttribute("exception", exception != null ? exception.toString() : "없음");
    model.addAttribute("requestUri", requestUri != null ? requestUri : "N/A");
    model.addAttribute("requestMethod", request.getMethod() != null ? request.getMethod() : "N/A");

    // 세션 데이터 정리
    session.removeAttribute("errorStatus");
    session.removeAttribute("errorMessage");

    return "error"; // error.html로 렌더링
  }
}