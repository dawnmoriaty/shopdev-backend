package com.example.shopdev.controller;

import com.example.shopdev.dto.res.ApiResponse;
import com.example.shopdev.dto.res.ImageResponse;
import com.example.shopdev.service.impl.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    @PostMapping("upload")
    public ResponseEntity<ApiResponse<ImageResponse>> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = imageService.uploadImage(file);
        ImageResponse imageResponse = new ImageResponse(imageUrl);
        return ResponseEntity.ok(
                ApiResponse.<ImageResponse>builder()
                        .success(true)
                        .status(200)
                        .message("Image uploaded successfully")
                        .data(imageResponse)
                        .build()
        );
    }

}
