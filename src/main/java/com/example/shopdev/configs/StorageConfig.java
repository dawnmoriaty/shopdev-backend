package com.example.shopdev.configs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;


@Configuration
public class StorageConfig {
    @Value("${firebase.credentials.path}")
    private String firebaseConfigPath;

    @Bean
    public Storage storage() throws IOException {
        ClassPathResource resource = new ClassPathResource(firebaseConfigPath.replace("classpath:", ""));
        return StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                .build()
                .getService();
    }
}
