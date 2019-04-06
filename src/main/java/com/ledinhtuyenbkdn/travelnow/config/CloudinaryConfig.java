package com.ledinhtuyenbkdn.travelnow.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "ledinhtuyenbkdn");
        config.put("api_key", "355879137395444");
        config.put("api_secret", "GVvBFQi_CQ1DB3AdHRR93scJ2us");
        return new Cloudinary(config);
    }
}