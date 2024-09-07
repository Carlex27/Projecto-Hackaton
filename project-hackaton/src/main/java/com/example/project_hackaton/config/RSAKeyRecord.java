package com.example.project_hackaton.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Record class for RSA keys
 * Contains the public and private RSA keys
 * Used for security configuration
 * @param rsaPublicKey
 * @param rsaPrivateKey
 */
@ConfigurationProperties(prefix = "jwt")
public record RSAKeyRecord(
        RSAPublicKey rsaPublicKey,
        RSAPrivateKey rsaPrivateKey
) {

}
