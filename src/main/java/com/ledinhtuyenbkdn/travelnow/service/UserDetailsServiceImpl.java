package com.ledinhtuyenbkdn.travelnow.service;

import com.ledinhtuyenbkdn.travelnow.model.User;
import com.ledinhtuyenbkdn.travelnow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(s).get();
        if (user == null) {
            throw new UsernameNotFoundException("Username Not Found");
        }
        List<GrantedAuthority> role = new ArrayList<>();
        role.add(new SimpleGrantedAuthority(user.getRole()));
        return new org.springframework.security.core.userdetails.User(s, user.getPassword(),
                role);
    }
}
