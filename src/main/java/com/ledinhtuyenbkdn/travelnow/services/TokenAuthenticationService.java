package com.ledinhtuyenbkdn.travelnow.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class TokenAuthenticationService {
    private static final long EXPIRATION_TIME = 1000 * 3600 * 24 * 7; // 7 days
    private static final String HEADER_STRING = "Authorization";
    private static final String SECRET_KEY = "secret";

    public static void addAuthentication(HttpServletResponse response, String username) {
        String Jwt = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
        response.addHeader(HEADER_STRING, Jwt);
    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            String username = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return username != null ?
                    new UsernamePasswordAuthenticationToken(username, null) : null;
        }
        return null;
    }
}
