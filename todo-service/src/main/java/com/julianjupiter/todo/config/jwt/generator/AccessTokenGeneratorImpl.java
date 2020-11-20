package com.julianjupiter.todo.config.jwt.generator;

import com.julianjupiter.todo.config.jwt.properties.SecretSignatureConfigurationProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@Component
class AccessTokenGeneratorImpl implements AccessTokenGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(AccessTokenGeneratorImpl.class);
    protected final ClaimsGenerator claimsGenerator;
    private final JWSSigner signer;
    private final JWSAlgorithm algorithm;

    AccessTokenGeneratorImpl(ClaimsGenerator claimsGenerator, SecretSignatureConfigurationProperties secretSignatureConfigurationProperties) {
        var secret = secretSignatureConfigurationProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        this.claimsGenerator = claimsGenerator;

        try {
            this.signer = new MACSigner(secret);
        } catch (KeyLengthException exception) {
            throw new RuntimeException("Unable to create a signer", exception);
        }

        this.algorithm = secretSignatureConfigurationProperties.getJwsAlgorithm();
    }

    @Override
    public Optional<String> generate(UserDetails userDetails, Integer expiration) {
        Map<String, Object> claims = claimsGenerator.generate(userDetails, expiration);
        return generate(claims);
    }

    @Override
    public Optional<String> generate(Map<String, Object> claims) {
        try {
            final JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();

            for (final Map.Entry<String, Object> entry : claims.entrySet()) {
                builder.claim(entry.getKey(), entry.getValue());
            }

            var signedJWT = new SignedJWT(new JWSHeader(algorithm), builder.build());
            signedJWT.sign(signer);
            return Optional.of(signedJWT.serialize());
        } catch (JOSEException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("JOSEException while generating token {}", e.getMessage());
            }
        }
        return Optional.empty();
    }
}
