package com.julianjupiter.todo.config.jwt.generator;

import com.nimbusds.jwt.JWTClaimsSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Component
class ClaimsGeneratorImpl implements ClaimsGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimsGeneratorImpl.class);

    private final JwtIdGenerator jwtIdGenerator;
    private final String applicationName;

    ClaimsGeneratorImpl(JwtIdGenerator jwtIdGenerator, @Value("${spring.application.name}") String applicationName) {
        this.jwtIdGenerator = jwtIdGenerator;
        this.applicationName = applicationName;
    }

    @Override
    public Map<String, Object> generate(UserDetails userDetails, Integer expiration) {
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
        createSubject(builder, userDetails);
        createUsername(builder, userDetails);
        createRoles(builder, userDetails);
        createIssueTime(builder);
        createExpirationTime(builder, expiration);
        createJwtId(builder);
        createIssuer(builder);
        createNotBeforeTime(builder);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Generated claim set: {}", builder.build().toJSONObject());
        }
        return builder.build().getClaims();
    }

    private void createSubject(JWTClaimsSet.Builder builder, UserDetails userDetails) {
        builder.subject(userDetails.getUsername());
    }

    private void createUsername(JWTClaimsSet.Builder builder, UserDetails userDetails) {
        builder.claim("username", userDetails.getUsername());
    }

    private void createRoles(JWTClaimsSet.Builder builder, UserDetails userDetails) {
        var roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toUnmodifiableList());
        builder.claim("roles", roles);
    }

    private void createIssueTime(JWTClaimsSet.Builder builder) {
        builder.issueTime(new Date());
    }

    private void createExpirationTime(JWTClaimsSet.Builder builder, Integer expiration) {
        if (expiration != null) {
            LOG.debug("Setting expiration to {}", expiration);
            builder.expirationTime(Date.from(Instant.now().plus(expiration, ChronoUnit.SECONDS)));
        }
    }

    private void createJwtId(JWTClaimsSet.Builder builder) {
        if (jwtIdGenerator != null) {
            builder.jwtID(jwtIdGenerator.generate());
        }
    }

    private void createIssuer(JWTClaimsSet.Builder builder) {
        if (applicationName != null && !applicationName.isBlank()) {
            builder.issuer(applicationName);
        }
    }

    private void createNotBeforeTime(JWTClaimsSet.Builder builder) {
        builder.notBeforeTime(new Date());
    }
}
