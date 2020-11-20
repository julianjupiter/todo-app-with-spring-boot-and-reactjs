package com.julianjupiter.todo.config.jwt.validator;

import com.julianjupiter.todo.config.jwt.properties.AccessTokenConfigurationProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Optional;

@Component
class AccessTokenValidatorImpl implements AccessTokenValidator {
    private static final Logger LOG = LoggerFactory.getLogger(AccessTokenValidatorImpl.class);

    private final String secret;

    AccessTokenValidatorImpl(AccessTokenConfigurationProperties accessTokenConfigurationProperties) {
        this.secret = accessTokenConfigurationProperties.getSecret();
    }

    @Override
    public Optional<JWT> validate(String token) {
        try {
            JWT jwt = JWTParser.parse(token);
            return validate((SignedJWT) jwt);
        } catch (final ParseException exception) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Failed to parse JWT: {}", exception.getMessage());
            }
        }

        return Optional.empty();
    }

    private Optional<JWT> validate(SignedJWT jwt) {
        try {
            final var verifier = new MACVerifier(this.secret);
            var verified = jwt.verify(verifier);
            if (!verified) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("JWT Signature verification failed: {}", jwt.getParsedString());
                }
            } else {
                return Optional.of(jwt);
            }
        } catch (JOSEException exception) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Verification failed", exception);
            }
        }

        return Optional.empty();
    }
}
