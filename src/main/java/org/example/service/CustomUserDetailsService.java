package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        User user = findByUsername(username);
        return new org.springframework.security.core.userdetails
                .User(user.getUsername(), user.getPassword(), Collections.singleton(mapRoleToAuthority(user.getRole())));
    }

    private SimpleGrantedAuthority mapRoleToAuthority(Role role) {
        return new SimpleGrantedAuthority(role.toString());
    }

    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User with username " + username + " not found"));
    }

    public String getRole(String username) {
        User user = findByUsername(username);
        return user.getRole().toString();
    }
}