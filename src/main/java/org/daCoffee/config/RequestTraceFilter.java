package org.daCoffee.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RequestTraceFilter extends OncePerRequestFilter {

  private static final String TRACE_ID_KEY = "traceId";

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String uri = request.getRequestURI();
    return uri.startsWith("/css/") || uri.startsWith("/image/") || uri.startsWith("/js/")
            || uri.equals("/favicon.ico")
            || uri.equals("/api/member/me") || uri.equals("/api/admin/me");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {
    MDC.put(TRACE_ID_KEY, UUID.randomUUID().toString().substring(0, 8));
    long startTime = System.currentTimeMillis();

    try {
      log.info("--> {} {}", request.getMethod(), request.getRequestURI());
      filterChain.doFilter(request, response);
    } finally {
      log.info("<-- {} {} {} ({}ms)", request.getMethod(), request.getRequestURI(),
              response.getStatus(), System.currentTimeMillis() - startTime);
      MDC.remove(TRACE_ID_KEY);
    }
  }
}
