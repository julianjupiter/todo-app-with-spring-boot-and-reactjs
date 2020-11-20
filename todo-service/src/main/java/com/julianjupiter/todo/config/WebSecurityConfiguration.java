package com.julianjupiter.todo.config;

import com.julianjupiter.todo.config.jwt.validator.AccessTokenValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final SecurityUtil securityUtil;
    private final AccessTokenValidator accessTokenValidator;
    private final String passwordEncoderId;

    public WebSecurityConfiguration(UserDetailsService userDetailsService,
                                    SecurityUtil securityUtil,
                                    AccessTokenValidator accessTokenValidator,
                                    @Value("${security.password-encoder-id}") String passwordEncoderId) {
        this.userDetailsService = userDetailsService;
        this.securityUtil = securityUtil;
        this.accessTokenValidator = accessTokenValidator;
        this.passwordEncoderId = passwordEncoderId;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(this.passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/v1/auth/**", "/h2-console/**")
                .permitAll()
                .antMatchers("/v1/auth/logout").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/v1/todos/**").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.POST, "/v1/todos/**").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/v1/todos/**").hasRole("USER")
                .antMatchers(HttpMethod.PATCH, "/v1/todos/**").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/v1/todos/**").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/v1/users").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/v1/users").hasAnyRole("ANONYMOUS")
                .antMatchers(HttpMethod.GET, "/v1/users/{id}").access("hasAnyRole('ADMIN') or @securityUtil.checkUserId(#id)")
                .antMatchers(HttpMethod.PUT, "/v1/users/{id}").access("@securityUtil.checkUserId(#id)")
                .antMatchers(HttpMethod.PATCH, "/v1/users/{id}").access("@securityUtil.checkUserId(#id)")
                .antMatchers(HttpMethod.DELETE, "/v1/users/{id}").access("@securityUtil.checkUserId(#id)")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(this.accessTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoderType argon2 = PasswordEncoderType.ARGON2;
        PasswordEncoderType bcrypt = PasswordEncoderType.BCRYPT;
        PasswordEncoderType pbkdf2 = PasswordEncoderType.PBKDF2;
        PasswordEncoderType scrypt = PasswordEncoderType.SCRYPT;

        Map<String, PasswordEncoder> encoders = Map.of(
                argon2.value(), argon2.newPasswordEncoder(),
                bcrypt.value(), bcrypt.newPasswordEncoder(),
                pbkdf2.value(), pbkdf2.newPasswordEncoder(),
                scrypt.value(), scrypt.newPasswordEncoder()
        );

        return new DelegatingPasswordEncoder(this.passwordEncoderId, encoders);
    }

    @Bean
    public AccessTokenAuthenticationFilter accessTokenAuthenticationFilter() {
        return new AccessTokenAuthenticationFilter(accessTokenValidator, userDetailsService);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        var configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of(
                "Access-Control-Allow-Headers",
                "Access-Control-Allow-Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers",
                "Accept",
                "Authorization",
                "Content-Type",
                "Origin"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        configuration.setMaxAge(3600L);
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
