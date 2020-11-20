package com.julianjupiter.todo.util;

import com.julianjupiter.todo.config.JwtUser;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Applications {

    private Applications() {
    }

    public static List<String> validationErrors(BindingResult bindingResult, MessageSource messageSource) {
        var fieldErrors = bindingResult.getFieldErrors();

        return fieldErrors.stream()
                .map(fieldError -> {
                    var fieldErrorCode = fieldError.getCode();
                    var field = fieldError.getField();
                    var resolveMessageCodes = bindingResult.resolveMessageCodes(fieldErrorCode);
                    return messageSource.getMessage(resolveMessageCodes[0] + "." + field,
                            new Object[]{fieldError.getRejectedValue()},
                            Locale.ENGLISH);
                }).collect(Collectors.toUnmodifiableList());

    }

    public static URI createUri() {
        return ServletUriComponentsBuilder.fromCurrentRequestUri()
                .buildAndExpand()
                .toUri();
    }

    public static URI createUri(String path, Object... uriVariableValues) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path(path)
                .buildAndExpand(uriVariableValues)
                .toUri();
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static String getUsername() {
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            JwtUser user = (JwtUser) principal;
            return user.getUsername();
        }

        return principal.toString();
    }

    public static boolean isAuthenticated() {
        return !(getAuthentication() instanceof AnonymousAuthenticationToken);
    }
}
