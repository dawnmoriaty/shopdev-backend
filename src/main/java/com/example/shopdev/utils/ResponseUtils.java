package com.example.shopdev.utils;

import com.example.shopdev.dto.res.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ResponseUtils {
    private final ObjectMapper objectMapper;
    public void writeErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .success(false)
                .status(status)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status);
        objectMapper.writeValue(response.getOutputStream(), apiResponse);
    }
}
