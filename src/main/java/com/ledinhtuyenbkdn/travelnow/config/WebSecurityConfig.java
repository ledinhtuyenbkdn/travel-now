package com.ledinhtuyenbkdn.travelnow.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().disable();

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/tourists").permitAll()
                .antMatchers(HttpMethod.PUT, "/tourists/{touristId}").hasRole("TOURIST")
                .anyRequest().permitAll();
    }
}
