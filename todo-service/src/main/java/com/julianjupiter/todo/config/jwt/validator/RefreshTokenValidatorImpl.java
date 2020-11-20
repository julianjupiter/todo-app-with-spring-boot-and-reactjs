package com.julianjupiter.todo.config.jwt.validator;

import com.julianjupiter.todo.config.jwt.properties.RefreshTokenConfigurationProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Optional;

@Component
class RefreshTokenValidatorImpl implements RefreshTokenValidator {
    private static final Logger LOG = LoggerFactory.getLogger(RefreshTokenValidatorImpl.class);
    private final JWSVerifier jwsVerifier;

    RefreshTokenValidatorImpl(RefreshTokenConfigurationProperties refreshTokenConfigurationProperties) {
        var secret = refreshTokenConfigurationProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        try {
            this.jwsVerifier = new MACVerifier(secret);
        } catch (JOSEException exception) {
            throw new RuntimeException("Unable to a verifier", exception);
        }
    }

    @Override
    public Optional<String> validate(String token) {
        JWSObject jwsObject = null;

        try {
            jwsObject = JWSObject.parse(token);
            if (jwsObject.verify(jwsVerifier)) {
                return Optional.of(jwsObject.getPayload().toString());
            }
        } catch (ParseException exception) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("ParseException parsing refresh token {} into JWS Object", token);
            }

            return Optional.empty();
        } catch (JOSEException exception) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("JOSEException parsing refresh token {} into JWS Object", token);
            }

            return Optional.empty();
        }

        return Optional.empty();
    }
}
