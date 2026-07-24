package org.daCoffee.controller.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.daCoffee.dto.ApiResponseDTO;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("Authentication failure : {}", exception.getMessage());

        String errorMessage = switch (exception.getClass().getSimpleName()) {
            case "UsernameNotFoundException" -> "존재하지 않는 아이디입니다.";
            case "DisabledException" -> "비활성화된 아이디입니다.";
            default -> "아이디 또는 비밀번호가 잘못되었습니다.";
        };

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        ApiResponseDTO<Void> apiResponse = ApiResponseDTO.error(errorMessage, 401);
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
    }
}