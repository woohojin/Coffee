package org.daCoffee.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class AlertController {
  @GetMapping("/alert")
  public String showAlert(Model model,
                          @RequestParam(defaultValue = "오류가 발생했습니다.") String msg,
                          @RequestParam(defaultValue = "/main") String url) {

    log.info("Rendering alert page with msg : {}, url : {}", msg, url);
    model.addAttribute("msg", msg);
    model.addAttribute("url", url);

    return "alert";
  }
}
