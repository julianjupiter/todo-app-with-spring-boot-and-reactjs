package com.julianjupiter.todo.config.jwt;

import com.julianjupiter.todo.config.jwt.event.RefreshTokenGeneratedEvent;
import com.julianjupiter.todo.config.jwt.generator.AccessTokenGenerator;
import com.julianjupiter.todo.config.jwt.generator.RefreshTokenGenerator;
import com.julianjupiter.todo.config.jwt.properties.AccessTokenConfigurationProperties;
import com.julianjupiter.todo.config.jwt.render.AccessRefreshToken;
import com.julianjupiter.todo.config.jwt.render.TokenRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class AccessRefreshTokenServiceImpl implements AccessRefreshTokenService {

    private static final Logger LOG = LoggerFactory.getLogger(AccessRefreshTokenServiceImpl.class);
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final Integer expiration;
    private final TokenRenderer tokenRenderer;
    private final AccessTokenGenerator accessTokenGenerator;
    private final ApplicationEventPublisher applicationEventPublisher;

    AccessRefreshTokenServiceImpl(RefreshTokenGenerator refreshTokenGenerator,
                                         AccessTokenConfigurationProperties accessTokenConfigurationProperties,
                                         TokenRenderer tokenRenderer,
                                         AccessTokenGenerator accessTokenGenerator,
                                         ApplicationEventPublisher applicationEventPublisher) {
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.expiration = accessTokenConfigurationProperties.getExpiration();
        this.tokenRenderer = tokenRenderer;
        this.accessTokenGenerator = accessTokenGenerator;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Generate a new access refresh token
     * @param userDetails
     * @return
     */
    @Override
    public Optional<AccessRefreshToken> generate(UserDetails userDetails) {
        return generate(userDetails, generateRefreshToken(userDetails).orElse(null));
    }

    /**
     * Generate a new access refresh token response
     * @param userDetails
     * @param refreshToken
     * @return
     */
    @Override
    public Optional<AccessRefreshToken> generate(UserDetails userDetails, String refreshToken) {
        Optional<String> optionalAccessToken = accessTokenGenerator.generate(userDetails, expiration);
        if (optionalAccessToken.isEmpty()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Failed to generate access token for user {}", userDetails.getUsername());
            }
            return Optional.empty();
        }

        String accessToken = optionalAccessToken.get();
        return Optional.of(tokenRenderer.render(userDetails, accessToken, refreshToken, expiration));
    }

    /**
     * Generate a new refresh token
     * @param userDetails
     * @return
     */
    @Override
    public Optional<String> generateRefreshToken(UserDetails userDetails) {
        String key = refreshTokenGenerator.createKey();
        Optional<String> refreshTokenOptional = refreshTokenGenerator.generate(key);
        refreshTokenOptional.ifPresent(refreshToken -> applicationEventPublisher.publishEvent(new RefreshTokenGeneratedEvent(this, userDetails, key)));

        return refreshTokenOptional;
    }

}
