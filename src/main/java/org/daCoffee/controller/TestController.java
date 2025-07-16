package org.daCoffee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

  @GetMapping("/test-error")
  public String testError() {
    throw new RuntimeException("test error");
  }
}
