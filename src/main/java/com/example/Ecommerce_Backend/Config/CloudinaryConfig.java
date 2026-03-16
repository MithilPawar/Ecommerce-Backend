package com.example.Ecommerce_Backend.Config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "doapro3gk",
                "api_key", "173859127992146",
                "api_secret", "UIE4kyRd5J1PFdFZBNU1vzL9Vdw"
        ));
    }
}
