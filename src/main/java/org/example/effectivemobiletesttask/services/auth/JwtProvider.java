package org.example.effectivemobiletesttask.services.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.example.effectivemobiletesttask.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

@Slf4j
@Component
public class JwtProvider {
    private static final String defaultValue = "default";
    private static final int EXPIRATION_ACCESS_TOKEN_TIME_IN_MINUTES = 5;
    private static final int EXPIRATION_REFRESH_TOKEN_TIME_IN_DAYS = 30;
    private static final LocalDateTime dateTime = LocalDateTime.now();

    @Value("${jwt.access-secret}")
    private String jwtAccessSecretKey;

    @Value("${jwt.refresh-secret}")
    private String jwtRefreshSecretKey;

    public String generateAccessToken(@NonNull User user) {
        final Date accessExpiration = getAccessExpirationDate();
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(accessExpiration)
                .signWith(createKey(jwtAccessSecretKey), SignatureAlgorithm.HS256)
                .claim("roles", formRoles(user))
                .compact();
    }

    public String generateRefreshToken(@NonNull User user) {
        final Date refreshExpiration = getRefreshExpirationDate();
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(refreshExpiration)
                .signWith(createKey(jwtRefreshSecretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    private Instant getAccessExpirationInstant() {
        return dateTime.plusMinutes(EXPIRATION_ACCESS_TOKEN_TIME_IN_MINUTES)
                .atZone(ZoneId.systemDefault()).toInstant();
    }

    private Date getAccessExpirationDate() {
        final Instant accessExprirationInstant = getAccessExpirationInstant();
        return Date.from(accessExprirationInstant);
    }

    private Instant getRefreshExpirationInstant() {
        return dateTime.plusDays(EXPIRATION_REFRESH_TOKEN_TIME_IN_DAYS)
                .atZone(ZoneId.systemDefault()).toInstant();
    }

    private Date getRefreshExpirationDate() {
        final Instant refreshExprirationInstant = getRefreshExpirationInstant();
        return Date.from(refreshExprirationInstant);
    }

    private boolean validateToken(@NonNull String token, @NonNull String secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(createBytes(secret))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired {}", expEx.getMessage());
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt {}", unsEx.getMessage());
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt {}", mjEx.getMessage());
        } catch (SignatureException sEx) {
            log.error("Invalid signature {}", sEx.getMessage());
        } catch (Exception e) {
            log.error("invalid token {}", e.getMessage());
        }
        return false;
    }

    public boolean validateAccessToken(@NonNull String token) {
        return validateToken(token, jwtAccessSecretKey);
    }

    public boolean validateRefreshToken(@NonNull String token) {
        return validateToken(token, jwtRefreshSecretKey);
    }

    public Claims getClaims(@NonNull String token,
                            @NonNull String secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(createBytes(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, jwtAccessSecretKey);
    }

    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, jwtRefreshSecretKey);
    }

    private Key createKey(String secret) {
        byte[] keyBytes = createBytes(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private byte[] createBytes(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Arrays.copyOf(keyBytes, 32);
    }

    //Сейчас не стоит задачи как то ограничивать доступ в зависимости от разрешений у пользователя.(ТЗ)
    private Set<String> formRoles(User user) {
        return Set.of(defaultValue);
    }
}
