package org.example.bookstore.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@Slf4j
@Component
public class JwtHelper {

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    private final JWT jwtDecoder;

    public JwtHelper(RSAPrivateKey privateKey, RSAPublicKey publicKey, JWT jwtDecoder) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.jwtDecoder = jwtDecoder;
    }

    public String createJwtForClaims(String subject, Map<String, String> claims) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Instant.now().toEpochMilli());
        calendar.add(Calendar.MINUTE, 60);

        JWTCreator.Builder jwtBuilder = JWT.create().withSubject(subject);
        claims.forEach(jwtBuilder::withClaim);

        return jwtBuilder
                .withNotBefore(new Date())
                .withExpiresAt(calendar.getTime())
                .sign(Algorithm.RSA256(publicKey, privateKey));
    }

    public boolean isTokenExpired(String token) {
        DecodedJWT payload = jwtDecoder.decodeJwt(token);
        LocalDateTime expiryDate = convertToLocalDateTimeViaMiliSecond(payload.getExpiresAt());
        return expiryDate.isBefore(LocalDateTime.now());
    }

    private LocalDateTime convertToLocalDateTimeViaMiliSecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
