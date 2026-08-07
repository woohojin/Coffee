package org.daCoffee.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class MailService {
  private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);
  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String mailUsername;

  private String templateCache;

  public void sendEmail(String toEmail, String subject, String main, String code) {
    String htmlContent = readTemplate()
      .replace("{{main}}", main)
      .replace("{{code}}", code);

    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setFrom(mailUsername);
      helper.setTo(toEmail);
      helper.setSubject(subject);
      helper.setText(htmlContent, true);
      mailSender.send(message);
    } catch(MailException e) {
      LOGGER.error("메일 전송 중 오류 발생: {}", e.getMessage());
      throw e;
    } catch (MessagingException e) {
      LOGGER.error("메일 전송 중 메시징 예외 발생: {}", e.getMessage());
      throw new RuntimeException("메일 전송 실패", e);
    }
  }

  private String readTemplate() {
    if (templateCache == null) {
      try {
        ClassPathResource resource = new ClassPathResource("mail.html");
        try (InputStream is = resource.getInputStream()) {
          templateCache = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
      } catch (IOException e) {
        LOGGER.error("메일 템플릿 읽기 실패: {}", e.getMessage());
        throw new RuntimeException(e);
      }
    }
    return templateCache;
    }
}
