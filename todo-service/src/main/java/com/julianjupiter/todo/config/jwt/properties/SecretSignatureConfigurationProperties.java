package com.julianjupiter.todo.config.jwt.properties;

import com.nimbusds.jose.JWSAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.token.jwt.signatures.secret.generator")
public class SecretSignatureConfigurationProperties {
    private String secret;
    private JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;

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
}
