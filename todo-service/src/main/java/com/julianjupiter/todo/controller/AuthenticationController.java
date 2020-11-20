package com.julianjupiter.todo.controller;

import com.julianjupiter.todo.config.JwtUser;
import com.julianjupiter.todo.config.jwt.AccessRefreshTokenService;
import com.julianjupiter.todo.config.jwt.properties.AccessTokenConfigurationProperties;
import com.julianjupiter.todo.config.jwt.render.AccessRefreshToken;
import com.julianjupiter.todo.config.jwt.validator.RefreshTokenValidator;
import com.julianjupiter.todo.dto.LoginDto;
import com.julianjupiter.todo.exception.InvalidTokenException;
import com.julianjupiter.todo.service.RefreshTokenService;
import com.julianjupiter.todo.util.Applications;
import com.julianjupiter.todo.util.MessageSourceProperties;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/v1/auth")
public class AuthenticationController {
    private final MessageSource messageSource;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final AccessRefreshTokenService accessRefreshTokenService;
    private final RefreshTokenValidator refreshTokenValidator;
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenConfigurationProperties accessTokenConfigurationProperties;

    public AuthenticationController(MessageSource messageSource,
                                    AuthenticationManager authenticationManager,
                                    UserDetailsService userDetailsService,
                                    AccessRefreshTokenService accessRefreshTokenService, RefreshTokenValidator refreshTokenValidator, RefreshTokenService refreshTokenService, AccessTokenConfigurationProperties accessTokenConfigurationProperties) {
        this.messageSource = messageSource;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.accessRefreshTokenService = accessRefreshTokenService;
        this.refreshTokenValidator = refreshTokenValidator;
        this.refreshTokenService = refreshTokenService;
        this.accessTokenConfigurationProperties = accessTokenConfigurationProperties;
    }

    @PostMapping("/login")
    public ResponseEntity<AccessRefreshToken> login(LoginDto loginDto) {
        Authentication authentication;
        try {
            authentication = this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException exception) {
            throw new UsernameNotFoundException(this.messageSource.getMessage(MessageSourceProperties.INVALID_USERNAME_OR_PASSWORD.toString(), null, Locale.ENGLISH), exception);
        }

        return accessRefreshTokenService.generate((UserDetails) authentication.getPrincipal())
                .map(accessRefreshToken -> {
                    var httpCookie = ResponseCookie.from("refresh_token", accessRefreshToken.getRefreshToken())
                            .httpOnly(true)
                            .maxAge(this.accessTokenConfigurationProperties.getExpiration())
                            .path("/")
                            .build();
                    return ResponseEntity.status(HttpStatus.OK)
                            .header(HttpHeaders.SET_COOKIE, httpCookie.toString())
                            .body(accessRefreshToken);
                }).orElseThrow(() -> new RuntimeException("Error in generating access refresh token"));


    }

    @GetMapping("/refresh_token")
    public ResponseEntity<AccessRefreshToken> refreshToken(@CookieValue("refresh_token") String refreshToken) {
        var payload = refreshTokenValidator.validate(refreshToken)
                .map(String::new)
                .orElseThrow(() -> new InvalidTokenException("Expired or invalid token", Applications.createUri()));

        return refreshTokenService.findByKey(payload)
                .map(rt -> {
                    var jwtUser = (JwtUser) this.userDetailsService.loadUserByUsername(rt.getUsername());
                    var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(jwtUser, "", jwtUser.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    return accessRefreshTokenService.generate((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), refreshToken)
                            .map(accessRefreshToken -> ResponseEntity.status(HttpStatus.OK)
                                    .body(accessRefreshToken))
                            .orElseThrow(() -> new RuntimeException("Error in generating refresh access token"));
                }).orElseThrow(() -> new InvalidTokenException("Expired or invalid token", Applications.createUri()));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue("refresh_token") String refreshToken) {
        var payload = refreshTokenValidator.validate(refreshToken)
                .map(String::new)
                .orElseThrow(() -> new InvalidTokenException("Expired or invalid token", Applications.createUri()));

        var updatedRefreshToken = refreshTokenService.findByKey(payload)
                .map(rt -> rt.setRevoked(true))
                .orElseThrow(() -> new InvalidTokenException("Expired or invalid token", Applications.createUri()));

        refreshTokenService.update(updatedRefreshToken);

        var httpCookie = ResponseCookie.from("refresh_token", null)
                .httpOnly(true)
                .maxAge(0)
                .path("/")
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, httpCookie.toString())
                .build();
    }
}
