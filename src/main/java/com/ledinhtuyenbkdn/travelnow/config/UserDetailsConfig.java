package com.ledinhtuyenbkdn.travelnow.config;

import com.ledinhtuyenbkdn.travelnow.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class UserDetailsConfig {
    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
}
