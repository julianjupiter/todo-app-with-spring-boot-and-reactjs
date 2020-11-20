package com.julianjupiter.todo.config.jwt.properties;

import com.nimbusds.jose.JWSAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.token.jwt.generator.access-token")
public class AccessTokenConfigurationProperties {
    private String secret;
    private JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;
    private Integer expiration = 3600;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public JWSAlgorithm getJwsAlgorithm() {
        return jwsAlgorithm;
    }

    public void setJwsAlgorithm(JWSAlgorithm jwsAlgorithm) {
        this.jwsAlgorithm = jwsAlgorithm;
    }

    public Integer getExpiration() {
        return expiration;
    }

    public void setExpiration(Integer expiration) {
        if (expiration != null) {
            this.expiration = expiration;
        }
    }

}
