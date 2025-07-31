package org.daCoffee.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/test/")
public class TestController {

  @GetMapping("/error")
  public String testError() {
    throw new RuntimeException("test error");
  }

  @GetMapping("/response-status")
  public String testResponseStatus() {
    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Test Internal Server Error");
  }
}
