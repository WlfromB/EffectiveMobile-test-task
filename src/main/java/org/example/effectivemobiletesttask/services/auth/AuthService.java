package org.example.effectivemobiletesttask.services.auth;

import org.example.effectivemobiletesttask.dto.jwt.*;
import org.example.effectivemobiletesttask.security.JwtAuthentication;

public interface AuthService {
    JwtResponse login(JwtRequest request) throws Exception;

    JwtResponse getAccessToken(String refreshToken) throws Exception;

    JwtResponse refresh(String refreshToken) throws Exception;

    JwtAuthentication getAuthInfo();
}
