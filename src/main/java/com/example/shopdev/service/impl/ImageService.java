package com.example.shopdev.service.impl;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {
    @Value("${firebase.storage.bucket-name}")
    private String bucketName;

    @Autowired
    private Storage storage;

    public String uploadImage(MultipartFile file) {
        try {
            // Tạo tên file với timestamp để tránh trùng lặp
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String fileName = formatter.format(LocalDateTime.now()) + "_" + file.getOriginalFilename();

            // Cấu hình thông tin blob cho Firebase
            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType())
                    .build();

            // Thiết lập quyền truy cập công khai
            List<Acl> acls = new ArrayList<>();
            acls.add(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
            blobInfo = blobInfo.toBuilder().setAcl(acls).build();

            // Upload trực tiếp lên Firebase
            Blob blob = storage.create(blobInfo, file.getBytes());
            return String.format("https://storage.googleapis.com/%s/%s", bucketName, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi upload ảnh: " + e.getMessage(), e);
        }
    }
}