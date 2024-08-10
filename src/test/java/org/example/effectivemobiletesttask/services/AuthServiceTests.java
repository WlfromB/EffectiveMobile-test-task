package org.example.effectivemobiletesttask.services;

import io.jsonwebtoken.Claims;
import org.example.effectivemobiletesttask.dto.jwt.JwtRequest;
import org.example.effectivemobiletesttask.dto.jwt.JwtResponse;
import org.example.effectivemobiletesttask.entities.User;
import org.example.effectivemobiletesttask.security.JwtAuthentication;
import org.example.effectivemobiletesttask.security.PasswordProvider;
import org.example.effectivemobiletesttask.services.auth.AuthServiceImpl;
import org.example.effectivemobiletesttask.services.auth.JwtProvider;
import org.example.effectivemobiletesttask.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class AuthServiceTests {

    @Mock
    private UserService userService;

    @Mock
    private PasswordProvider passwordProvider;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_ValidCredentials_ShouldReturnJwtResponse() throws Exception {
        JwtRequest request = new JwtRequest();
        request.setLoginOrEmail("valid@email.com");
        request.setPassword("password12");
        User user = new User();
        user.setPassword("hashedPassword");

        when(userService.findByEmailOrLogin(request.getLoginOrEmail())).thenReturn(user);
        when(passwordProvider.passwordMatches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtProvider.generateAccessToken(user)).thenReturn("accessToken");
        when(jwtProvider.generateRefreshToken(user)).thenReturn("refreshToken");

        JwtResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }

    @Test
    void login_InvalidPassword_ShouldThrowAccessDeniedException() throws Exception {
        JwtRequest request = new JwtRequest();
        request.setLoginOrEmail("valid@email.com");
        request.setPassword("password12");
        User user = new User();
        user.setPassword("hashedPassword");

        when(userService.findByEmailOrLogin(request.getLoginOrEmail())).thenReturn(user);
        when(passwordProvider.passwordMatches(request.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> authService.login(request));
    }

    @Test
    void getAccessToken_ValidRefreshToken_ShouldReturnJwtResponse() throws Exception {
        String refreshToken = "validRefreshToken";
        Claims claims = mock(Claims.class);
        User user = new User();

        when(jwtProvider.validateRefreshToken(refreshToken)).thenReturn(true);
        when(jwtProvider.getRefreshClaims(refreshToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn("validLogin");
        when(userService.findByEmailOrLogin("validLogin")).thenReturn(user);
        when(jwtProvider.generateAccessToken(user)).thenReturn("newAccessToken");

        JwtResponse response = authService.getAccessToken(refreshToken);

        assertNotNull(response);
        assertEquals("newAccessToken", response.getAccessToken());
        assertNull(response.getRefreshToken());
    }

    @Test
    void getAccessToken_InvalidRefreshToken_ShouldThrowAccessDeniedException() {
        String refreshToken = "invalidRefreshToken";

        when(jwtProvider.validateRefreshToken(refreshToken)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> authService.getAccessToken(refreshToken));
    }

    @Test
    void refresh_ValidRefreshToken_ShouldReturnJwtResponse() throws Exception {
        String refreshToken = "validRefreshToken";
        Claims claims = mock(Claims.class);
        User user = new User();

        when(jwtProvider.validateRefreshToken(refreshToken)).thenReturn(true);
        when(jwtProvider.getRefreshClaims(refreshToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn("validLogin");
        when(userService.findByEmailOrLogin("validLogin")).thenReturn(user);
        when(jwtProvider.generateAccessToken(user)).thenReturn("newAccessToken");
        when(jwtProvider.generateRefreshToken(user)).thenReturn("newRefreshToken");

        JwtResponse response = authService.refresh(refreshToken);

        assertNotNull(response);
        assertEquals("newAccessToken", response.getAccessToken());
        assertEquals("newRefreshToken", response.getRefreshToken());
    }

    @Test
    void refresh_InvalidRefreshToken_ShouldThrowAccessDeniedException() {
        String refreshToken = "invalidRefreshToken";

        when(jwtProvider.validateRefreshToken(refreshToken)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> authService.refresh(refreshToken));
    }

    @Test
    void getAuthInfo_ShouldReturnJwtAuthentication() {
        JwtAuthentication auth = new JwtAuthentication();
        SecurityContextHolder.getContext().setAuthentication(auth);

        JwtAuthentication result = authService.getAuthInfo();

        assertEquals(auth, result);
    }
}
