package com.ledinhtuyenbkdn.travelnow.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledinhtuyenbkdn.travelnow.model.User;
import com.ledinhtuyenbkdn.travelnow.model.UserDTO;
import com.ledinhtuyenbkdn.travelnow.service.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    public JwtLoginFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/login", "POST"));
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String requestBody = httpServletRequest.getReader().lines()
                .collect(Collectors.joining(System.lineSeparator()));

        ObjectMapper object = new ObjectMapper();
        UserDTO user = object.readValue(requestBody, UserDTO.class);

        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName(),
                        user.getPassword())
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        tokenAuthenticationService.addAuthentication(response, authResult.getName());
    }
}
