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
import java.util.Arrays;
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
                config.setExposedHeaders(Arrays.asList("Authorization"));
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
                //Category crud
                .antMatchers(HttpMethod.GET, "/categories").permitAll()
                .antMatchers(HttpMethod.POST, "/categories").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/categories/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/categories/{id}").hasRole("ADMIN")
                //Province crud
                .antMatchers(HttpMethod.GET, "/provinces").permitAll()
                .antMatchers(HttpMethod.POST, "/provinces").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/provinces/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/provinces/{id}").hasRole("ADMIN")
                //Place crud
                .antMatchers(HttpMethod.POST, "/places").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/places/{id}").permitAll()
                .antMatchers(HttpMethod.PUT, "/places/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/places/{id}").hasRole("ADMIN")
                //Comment place crud
                .antMatchers(HttpMethod.POST, "/places/{idPlace}/comments").hasRole("TOURIST")
                .antMatchers(HttpMethod.GET, "/places/{idPlace}/comments").permitAll()
                .antMatchers(HttpMethod.PUT, "/places/{idPlace}/comments/{idComment}").hasRole("TOURIST")
                .antMatchers(HttpMethod.DELETE, "/places/{idPlace}/comments/{idComment}").hasRole("TOURIST")
                //Post crud
                .antMatchers(HttpMethod.POST, "/posts").hasRole("TOURIST")
                .antMatchers(HttpMethod.GET, "/posts/{postId}").permitAll()
                .antMatchers(HttpMethod.GET, "/tourists/{touristId}/posts").permitAll()
                .antMatchers(HttpMethod.PUT, "/posts/{postId}").hasRole("TOURIST")
                .antMatchers(HttpMethod.DELETE, "/posts/{postId}").hasRole("TOURIST")
                //Like crud
                .antMatchers(HttpMethod.POST, "/posts/{postId}/likes").hasRole("TOURIST")
                .antMatchers(HttpMethod.GET, "/posts/{postId}/likes").permitAll()
                .antMatchers(HttpMethod.DELETE, "/posts/{postId}/likes/{likeId}").hasRole("TOURIST")
                //Comment post crud
                .antMatchers(HttpMethod.POST, "/posts/{postId}/comments").hasRole("TOURIST")
                .antMatchers(HttpMethod.GET, "/posts/{postId}/comments").permitAll()
                .antMatchers(HttpMethod.PUT, "/posts/{postId}/comments/{idComment}").hasRole("TOURIST")
                .antMatchers(HttpMethod.DELETE, "/posts/{postId}/comments/{idComment}").hasRole("TOURIST")
                //follow
                .antMatchers(HttpMethod.POST, "/tourists/{id}/follows").hasRole("TOURIST")
                .antMatchers(HttpMethod.DELETE, "/tourists/{id}/follows").hasRole("TOURIST")
                .antMatchers(HttpMethod.GET, "/tourists/{id}/follows").permitAll()
                .antMatchers(HttpMethod.GET, "/newsfeed").hasRole("TOURIST")
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
