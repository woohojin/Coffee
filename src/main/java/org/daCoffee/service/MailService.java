package org.daCoffee.service;

import org.springframework.core.env.Environment;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class MailService {
  private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);
  private final JavaMailSender mailSender;
  private final Environment env;

  public MailService(JavaMailSender mailSender, Environment env) {
    this.mailSender = mailSender;
    this.env = env;
  }

  public void sendEmail(String toEmail, String subject, String main, String code) {
    String username = env.getProperty("spring.mail.username");
    String htmlContent = readTemplate();
    htmlContent = htmlContent.replace("{{main}}", main);
    htmlContent = htmlContent.replace("{{code}}", code);

    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setFrom(username);
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
    try{
      ClassPathResource resource = new ClassPathResource("mail.html");
      byte[] fileBytes = Files.readAllBytes(resource.getFile().toPath());
      return new String(fileBytes, StandardCharsets.UTF_8);
    } catch(IOException e) {
      LOGGER.error("메일 템플릿 읽기 실패: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
