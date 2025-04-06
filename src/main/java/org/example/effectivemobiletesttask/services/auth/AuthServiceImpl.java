package org.example.effectivemobiletesttask.services.auth;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.effectivemobiletesttask.dto.jwt.JwtRequest;
import org.example.effectivemobiletesttask.dto.jwt.JwtResponse;
import org.example.effectivemobiletesttask.entities.User;
import org.example.effectivemobiletesttask.security.JwtAuthentication;
import org.example.effectivemobiletesttask.security.PasswordProvider;
import org.example.effectivemobiletesttask.services.user.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final PasswordProvider passwordProvider;
    private final JwtProvider jwtProvider;


    @Override
    public JwtResponse login(JwtRequest request) throws Exception {
        final User user = userService.findByEmailOrLogin(request.getLoginOrEmail());
        final String userPassword = user.getPassword();
        final String requestPassword = request.getPassword();

        return createResponseWithCheckCondition(user, requestPassword, userPassword);
    }

    @Override
    public JwtResponse getAccessToken(String refreshToken) throws Exception {
        if (validateRefreshTokenCondition(refreshToken)) {
            return createAccessTokenResponse(refreshToken);
        }
        throw new AccessDeniedException("Invalid refresh token");
    }

    @Override
    public JwtResponse refresh(String refreshToken) throws Exception {
        if (validateRefreshTokenCondition(refreshToken)) {
            return createRefreshBothTokenResponse(refreshToken);
        }
        throw new AccessDeniedException("Invalid refresh token");
    }

    @Override
    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

    private boolean passwordMatchesCondition(String requestPassword, String userPassword) {
        return passwordProvider.passwordMatches(requestPassword, userPassword);
    }

    private JwtResponse createLoginResponse(User user) {
        final String accessToken = jwtProvider.generateAccessToken(user);
        final String refreshToken = jwtProvider.generateRefreshToken(user);
        return JwtResponse.responseWithTwoToken(accessToken, refreshToken);
    }

    private JwtResponse createResponseWithCheckCondition(User user, String userPassword, String requestPassword) {
        if (passwordMatchesCondition(userPassword, requestPassword)) {
            return createLoginResponse(user);
        }
        throw new AccessDeniedException("Invalid password");
    }

    private boolean validateRefreshTokenCondition(String refreshToken) {
        return jwtProvider.validateRefreshToken(refreshToken);
    }

    private JwtResponse createAccessTokenResponse(String refreshToken) throws Exception {
        String accessToken = getAccessTokenFromRefreshToken(refreshToken);
        return JwtResponse.responseOnlyAccessToken(accessToken);
    }

    private String getLoginFromRefreshToken(String refreshToken) {
        final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        return claims.getSubject();
    }

    private User getUserFromRefreshToken(String refreshToken) throws Exception {
        final String login = getLoginFromRefreshToken(refreshToken);
        return userService.findByEmailOrLogin(login);
    }

    private String getAccessTokenFromUser(User user) {
        return jwtProvider.generateAccessToken(user);
    }

    private String getRefreshTokenFromUser(User user) {
        return jwtProvider.generateRefreshToken(user);
    }

    private String getAccessTokenFromRefreshToken(String refreshToken) throws Exception {
        final User user = getUserFromRefreshToken(refreshToken);
        return getAccessTokenFromUser(user);
    }

    private JwtResponse createRefreshBothTokenResponse(String refreshToken) throws Exception {
        final User user = getUserFromRefreshToken(refreshToken);
        final String accessToken = getAccessTokenFromUser(user);
        final String newRefreshToken = getRefreshTokenFromUser(user);
        return JwtResponse.responseWithTwoToken(accessToken, newRefreshToken);
    }
}
