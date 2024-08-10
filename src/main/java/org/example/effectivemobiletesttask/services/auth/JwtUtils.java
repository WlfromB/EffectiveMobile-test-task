package org.example.effectivemobiletesttask.services.auth;


import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.effectivemobiletesttask.security.JwtAuthentication;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtAuthentication = new JwtAuthentication();
        jwtAuthentication.setAuthorities(getAuthorities(claims));
        jwtAuthentication.setLogin(claims.get("sub", String.class));
        return jwtAuthentication;
    }

    private static Set<String> getAuthorities(Claims claims) {
        List<String> authoritiesList = claims.get("authorities", List.class);
        return authoritiesList != null ? new HashSet<>(authoritiesList) : Collections.emptySet();
    }
}
