package com.julianjupiter.todo.config.jwt.generator;

import com.julianjupiter.todo.config.jwt.properties.RefreshTokenConfigurationProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
class RefreshTokenGeneratorImpl implements RefreshTokenGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(RefreshTokenGeneratorImpl.class);
    private final JWSAlgorithm algorithm;
    private final JWSSigner signer;

    RefreshTokenGeneratorImpl(RefreshTokenConfigurationProperties refreshTokenConfigurationProperties) {
        byte[] secret = refreshTokenConfigurationProperties.getSecret().getBytes(UTF_8);
        this.algorithm = refreshTokenConfigurationProperties.getJwsAlgorithm();

        try {
            this.signer = new MACSigner(secret);
        } catch (JOSEException exception) {
            throw new RuntimeException("Unable to create a signer", exception);
        }
    }

    @Override
    public String createKey() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Optional<String> generate(String token) {
        try {
            JWSObject jwsObject = new JWSObject(new JWSHeader(algorithm), new Payload(token));
            jwsObject.sign(signer);
            return Optional.of(jwsObject.serialize());
        } catch (JOSEException exception) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("JOSEException signing a JWS Object", exception);
            }
        }

        return Optional.empty();
    }
}
