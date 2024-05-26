package org.example.bookstore.config.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.config.properties.AppPropertyConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    private final AppPropertyConfig propertyConfig;

    @Bean
    public KeyStore keyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            Path path = Paths.get(propertyConfig.getSecurityJWTKeyStorePath());
            InputStream resourceAsStream = new FileInputStream(path.toFile());
            keyStore.load(resourceAsStream, propertyConfig.getSecurityJWTKeyStorePassword().toCharArray());
            return keyStore;
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            log.error("Unable to load keystore: {}", propertyConfig.getSecurityJWTKeyStorePath(), e);
        }

        throw new IllegalArgumentException("Unable to load keystore");
    }

    @Bean
    public RSAPrivateKey jwtSigningKey(KeyStore keyStore) {
        try {
            Key key = keyStore.getKey(propertyConfig.getSecurityJWTKeyAlias(), propertyConfig.getSecurityJWTPrivateKeyPassphrase().toCharArray());
            if (key instanceof RSAPrivateKey rsaPrivateKey) {
                return rsaPrivateKey;
            }
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            log.error("Unable to load private key from keystore: {}", propertyConfig.getSecurityJWTKeyStorePath(), e);
        }

        throw new IllegalArgumentException("Unable to load private key");
    }

    @Bean
    public RSAPublicKey jwtValidationKey(KeyStore keyStore) {
        try {
            Certificate certificate = keyStore.getCertificate(propertyConfig.getSecurityJWTKeyAlias());
            PublicKey publicKey = certificate.getPublicKey();

            if (publicKey instanceof RSAPublicKey rsaPublicKey) {
                return rsaPublicKey;
            }
        } catch (KeyStoreException e) {
            log.error("Unable to load private key from keystore: {}", propertyConfig.getSecurityJWTKeyStorePath(), e);
        }

        throw new IllegalArgumentException("Unable to load RSA public key");
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey) {
        return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
    }

}
