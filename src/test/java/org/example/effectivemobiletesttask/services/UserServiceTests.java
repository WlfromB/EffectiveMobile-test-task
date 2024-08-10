package org.example.effectivemobiletesttask.services;

import org.example.effectivemobiletesttask.dao.UserRepository;
import org.example.effectivemobiletesttask.dto.user.UserRequestCreate;
import org.example.effectivemobiletesttask.entities.User;
import org.example.effectivemobiletesttask.security.PasswordProvider;
import org.example.effectivemobiletesttask.services.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.webjars.NotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordProvider passwordProvider;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ValidRequest_ShouldReturnSavedUser() throws Exception {
        UserRequestCreate requestCreate = mock(UserRequestCreate.class);
        User user = new User();
        when(requestCreate.from()).thenReturn(user);
        when(passwordProvider.getPassword(requestCreate.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(requestCreate);

        assertNotNull(result);
        assertEquals("hashedPassword", result.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser_UserAlreadyExists_ShouldThrowIllegalArgumentException() {
        UserRequestCreate requestCreate = new UserRequestCreate();
        requestCreate.setEmail("existingEmail@example.com");
        requestCreate.setLogin("existingLogin");
        when(userRepository.findUserByEmailOrLogin(requestCreate.getEmail(), requestCreate.getLogin()))
                .thenReturn(Optional.of(new User()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(requestCreate));
        assertEquals("User with login existingLogin already exists", exception.getMessage());
    }

    @Test
    void findAll_ValidPageable_ShouldReturnUsersPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> usersPage = new PageImpl<>(Collections.singletonList(new User()));
        when(userRepository.findAll(pageable)).thenReturn(usersPage);

        Page<User> result = userService.findAll(pageable);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    void findAll_EmptyPage_ShouldThrowNotFoundException() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> emptyPage = new PageImpl<>(Collections.emptyList());
        when(userRepository.findAll(pageable)).thenReturn(emptyPage);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.findAll(pageable));
        assertEquals("Page not found", exception.getMessage());
    }

    @Test
    void findById_ValidId_ShouldReturnUser() throws Exception {
        Long userId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.findById(userId);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void findById_InvalidId_ShouldThrowNotFoundException() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.findById(userId));
        assertEquals("User Not Found", exception.getMessage());
    }

    @Test
    void findByLogin_ValidLogin_ShouldReturnUser() throws Exception {
        String login = "validLogin";
        User user = new User();
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));

        User result = userService.findByLogin(login);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).findByLogin(login);
    }

    @Test
    void findByLogin_InvalidLogin_ShouldThrowNotFoundException() {
        String login = "invalidLogin";
        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.findByLogin(login));
        assertEquals("User Not Found", exception.getMessage());
    }

    @Test
    void findByEmail_ValidEmail_ShouldReturnUser() throws Exception {
        String email = "validEmail@example.com";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User result = userService.findByEmail(email);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void findByEmail_InvalidEmail_ShouldThrowNotFoundException() {
        String email = "invalidEmail@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.findByEmail(email));
        assertEquals("User Not Found", exception.getMessage());
    }

    @Test
    void findByEmailOrLogin_ValidEmailOrLogin_ShouldReturnUser() throws Exception {
        String emailOrLogin = "validEmailOrLogin";
        User user = new User();
        when(userRepository.findUserByEmailOrLogin(emailOrLogin, emailOrLogin)).thenReturn(Optional.of(user));

        User result = userService.findByEmailOrLogin(emailOrLogin);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).findUserByEmailOrLogin(emailOrLogin, emailOrLogin);
    }

    @Test
    void findByEmailOrLogin_InvalidEmailOrLogin_ShouldThrowNotFoundException() {
        String emailOrLogin = "invalidEmailOrLogin";
        when(userRepository.findUserByEmailOrLogin(emailOrLogin, emailOrLogin)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.findByEmailOrLogin(emailOrLogin));
        assertEquals("User Not Found", exception.getMessage());
    }
}

