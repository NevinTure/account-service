package com.service.account.handlers;

import com.service.account.services.LogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final LogService logService;

    public RestAccessDeniedHandler(LogService logService) {
        this.logService = logService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        logService.accessDenied();
        response.getOutputStream().println(
                ("{\"timestamp\": \"%s\"," +
                        "\"status\": %d," +
                        "\"error\": \"%s\"," +
                        "\"message\": \"%s\"," +
                        "\"path\": \"%s\"}").formatted(LocalDate.now(), response.getStatus(), "Forbidden", "Access Denied!", request.getRequestURI()));
    }
}
