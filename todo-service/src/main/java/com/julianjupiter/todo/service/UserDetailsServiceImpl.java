package com.julianjupiter.todo.service;

import com.julianjupiter.todo.config.JwtUser;
import com.julianjupiter.todo.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user ->new JwtUser(
                        user.getId(),
                        user.getUsername(),
                        user.getPassword(),
                        user.isAccountNonExpired(),
                        user.isAccountNonLocked(),
                        user.isCredentialsNonExpired(),
                        user.isEnabled(),
                        user.getRoles().stream()
                                .map(userRole -> new SimpleGrantedAuthority(userRole.getCode().name()))
                                .collect(Collectors.toUnmodifiableSet())))
                .orElseThrow(() -> new UsernameNotFoundException(""));
    }
}
