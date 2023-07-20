package com.service.account.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;


@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println(
                ("{\"timestamp\": \"%s\"," +
                "\"status\": %d," +
                "\"error\": \"%s\"," +
                "\"message\": \"%s\"," +
                "\"path\": \"%s\"}").formatted(LocalDate.now(), response.getStatus(), "Unauthorized", authException.getMessage(), request.getRequestURI()));
    }
}
