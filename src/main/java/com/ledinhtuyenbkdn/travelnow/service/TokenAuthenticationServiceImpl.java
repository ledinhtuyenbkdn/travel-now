package com.ledinhtuyenbkdn.travelnow.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledinhtuyenbkdn.travelnow.repository.AdminRepository;
import com.ledinhtuyenbkdn.travelnow.repository.TouristRepository;
import com.ledinhtuyenbkdn.travelnow.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TokenAuthenticationServiceImpl implements TokenAuthenticationService {
    private static final long EXPIRATION_TIME = 1000 * 3600 * 24 * 7; // 7 days
    private static final String HEADER_STRING = "Authorization";
    private static final String SECRET_KEY = "secret";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TouristRepository touristRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StorageService storageService;

    public void addAuthentication(HttpServletResponse response, String username) {
        String Jwt = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            response.addHeader(HEADER_STRING, Jwt);
            ObjectMapper mapper = new ObjectMapper();
            String json = "";
            if (userRepository.findByUserName(username).get().getRole().equals("ROLE_TOURIST")) {
                json = mapper.writeValueAsString(touristRepository.findByUserName(username).get());
            } else {
                json = mapper.writeValueAsString(adminRepository.findByUserName(username).get());
            }

            writer.print(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            String username = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            if (username != null) {
                List<GrantedAuthority> role = new ArrayList<>();
                role.add(new SimpleGrantedAuthority(
                        userRepository.findByUserName(username).get().getRole()
                ));
                return new UsernamePasswordAuthenticationToken(username, null, role);
            }
        }
        return null;
    }
}
