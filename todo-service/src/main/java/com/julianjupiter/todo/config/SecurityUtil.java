package com.julianjupiter.todo.config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
    public boolean checkUserId(Long id) {
        var jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwtUser != null && jwtUser.getId().equals(id);
    }
}
