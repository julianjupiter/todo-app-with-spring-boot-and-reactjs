package com.julianjupiter.todo.config;

import com.julianjupiter.todo.config.jwt.render.BearerTokenRenderer;
import com.julianjupiter.todo.config.jwt.validator.AccessTokenValidator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

public class AccessTokenAuthenticationFilter extends OncePerRequestFilter {

    private final AccessTokenValidator accessTokenValidator;
    private final UserDetailsService userDetailsService;

    public AccessTokenAuthenticationFilter(AccessTokenValidator accessTokenValidator, UserDetailsService userDetailsService) {
        this.accessTokenValidator = accessTokenValidator;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        var bearerToken = request.getHeader("Authorization");
        this.resolveToken(bearerToken)
                .ifPresentOrElse(authToken -> accessTokenValidator.validate(authToken)
                                .ifPresentOrElse(jwt -> {
                                    try {
                                        var username = jwt.getJWTClaimsSet().getClaim("username");
                                        if (username != null) {
                                            var jwtUser = (JwtUser) this.userDetailsService.loadUserByUsername(username.toString());
                                            var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(jwtUser, "", jwtUser.getAuthorities());
                                            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                            logger.info("authenticated user " + username + ":" + usernamePasswordAuthenticationToken.getAuthorities() + ", setting security context");
                                            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                                        } else {
                                            logger.warn("An error occurred while fetching username from Token");
                                            SecurityContextHolder.clearContext();
                                        }
                                    } catch (ParseException exception) {
                                        logger.error("Invalid claims.");
                                        SecurityContextHolder.clearContext();
                                    }
                                }, () -> {
                                    logger.error("Invalid token.");
                                    SecurityContextHolder.clearContext();
                                }),
                        () -> {
                            logger.warn("Couldn't find bearer string, header will be ignored");
                            SecurityContextHolder.clearContext();
                        });

        filterChain.doFilter(request, response);
    }

    private Optional<String> resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(BearerTokenRenderer.BEARER_TOKEN_TYPE)) {
            return Optional.of(bearerToken.substring(7));
        }

        return Optional.empty();
    }
}
