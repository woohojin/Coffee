package org.daCoffee.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class CustomErrorHandler implements ErrorController {
  private static final Logger logger = LoggerFactory.getLogger(CustomErrorHandler.class);

  @RequestMapping("/error")
  public String handleError(HttpServletRequest request, Model model, HttpSession session) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
    Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

    Object sessionStatus = session.getAttribute("errorStatus");
    Object sessionMessage = session.getAttribute("errorMessage");

    logger.info("Error occurred: Status = {}, Message = {}, Exception = {}, URI = {}, Session Status = {}, Session Message = {}",
      status, message, exception, request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI), sessionStatus, sessionMessage);

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

    model.addAttribute("message", message != null ? message : sessionMessage != null ? sessionMessage : "Unknown error");
    model.addAttribute("exception", exception != null ? exception.toString() : "N/A");
    model.addAttribute("requestUri", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));

    // 세션 데이터 정리
    session.removeAttribute("errorStatus");
    session.removeAttribute("errorMessage");

    return "error"; // error.jsp로 렌더링
  }
}