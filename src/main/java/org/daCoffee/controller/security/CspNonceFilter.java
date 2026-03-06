package org.daCoffee.controller.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class CspNonceFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
    throws ServletException, IOException {

    String nonce = Base64.getEncoder().encodeToString(
      UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
    request.setAttribute("cspNonce", nonce);

    String requestURI = request.getRequestURI();

    boolean isRelaxed = requestURI.startsWith("/member/memberSignUp") || requestURI.startsWith("/member/memberProfile");
    boolean isAdmin = requestURI.startsWith("/admin");

    String csp;

    if (isAdmin) {
      csp = "default-src 'self'; " +
        "script-src 'self' 'unsafe-inline' 'unsafe-eval' " +
        "https://code.jquery.com " +
        "https://code.jquery.com/ui/ " +
        "http://t1.daumcdn.net https://t1.daumcdn.net " +
        "http://postcode.map.daum.net https://postcode.map.daum.net " +
        "https://s1.daumcdn.net https://s3.daumcdn.net; " +
        "style-src 'self' 'unsafe-inline' " +
        "https://fonts.googleapis.com; " +
        "font-src 'self' data: https://fonts.gstatic.com; " +
        "img-src 'self' data:; " +
        "frame-src 'self' " +
        "http://postcode.map.daum.net https://postcode.map.daum.net; " +
        "frame-ancestors 'self'; ";
    } else if (isRelaxed) {
      csp = "default-src 'self'; " +
        "script-src 'self' 'nonce-" + nonce + "' " +
        "https://code.jquery.com " +
        "https://t1.daumcdn.net https://s1.daumcdn.net https://s3.daumcdn.net " +
        "http://postcode.map.daum.net https://postcode.map.daum.net " +
        "https://js.tosspayments.com; " +
        "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; " +
        "font-src 'self' data: https://fonts.gstatic.com; " +
        "img-src 'self' data:; " +
        "connect-src 'self' https://js.tosspayments.com " +
        "https://event.tosspayments.com wss://*.tosspayments.com; " +
        "frame-src 'self' http://postcode.map.daum.net https://postcode.map.daum.net " +
        "https://*.tosspayments.com; " +
        "frame-ancestors 'self';";
    } else {
      csp = "default-src 'self'; " +
        "script-src 'self' 'nonce-" + nonce + "' " +
        "https://code.jquery.com " +
        "https://t1.daumcdn.net https://s1.daumcdn.net https://s3.daumcdn.net " +
        "http://postcode.map.daum.net https://postcode.map.daum.net " +
        "https://js.tosspayments.com; " +
        "style-src 'self' https://fonts.googleapis.com; " +
        "font-src 'self' data: https://fonts.gstatic.com; " +
        "img-src 'self' data:; " +
        "connect-src 'self' https://js.tosspayments.com " +
        "https://event.tosspayments.com wss://*.tosspayments.com; " +
        "frame-src 'self' " +
        "https://t1.daumcdn.net http://postcode.map.daum.net https://postcode.map.daum.net " +
        "https://*.tosspayments.com; " +
        "frame-ancestors 'self';";
    }
    response.setHeader("Content-Security-Policy", csp);
    filterChain.doFilter(request, response);
  }
}