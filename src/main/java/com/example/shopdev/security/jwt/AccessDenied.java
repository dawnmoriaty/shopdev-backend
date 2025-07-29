package com.example.shopdev.security.jwt;

import com.example.shopdev.utils.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccessDenied implements AccessDeniedHandler {
    private final ResponseUtils responseUtils;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("Access Denied: {}", accessDeniedException.getMessage());
        responseUtils.writeErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Forbidden: " + accessDeniedException.getMessage());
    }
}
