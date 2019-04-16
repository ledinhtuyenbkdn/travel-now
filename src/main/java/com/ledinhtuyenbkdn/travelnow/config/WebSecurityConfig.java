package com.ledinhtuyenbkdn.travelnow.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtLoginFilter jwtLoginFilter() {
        try {
            return new JwtLoginFilter(authenticationManager());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().configurationSource(new CorsConfigurationSource() {

            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setAllowedMethods(Collections.singletonList("*"));
                config.addAllowedOrigin("*");
                config.setAllowCredentials(true);
                return config;
            }
        });

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/tourists").permitAll()
                .antMatchers(HttpMethod.GET, "/tourists/{id}").permitAll()
                .antMatchers(HttpMethod.PUT, "/tourists/{id}").hasRole("TOURIST")
                .antMatchers(HttpMethod.GET, "/admins").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/categories").permitAll()
                .antMatchers(HttpMethod.POST, "/categories").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/categories/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/categories/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/provinces").permitAll()
                .antMatchers(HttpMethod.POST, "/provinces").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/provinces/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/provinces/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/places").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/places/{id}").permitAll()
                .antMatchers(HttpMethod.PUT, "/places/{id}").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(jwtLoginFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
