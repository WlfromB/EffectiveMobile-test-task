package org.example.effectivemobiletesttask.services.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.effectivemobiletesttask.dao.UserRepository;
import org.example.effectivemobiletesttask.dto.user.UserCreateRequest;
import org.example.effectivemobiletesttask.entities.User;
import org.example.effectivemobiletesttask.security.PasswordProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.function.Supplier;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordProvider passwordProvider;

    private Supplier<NotFoundException> throwUserNotFound(){
        throw new NotFoundException("User not found");
    }
    
    @Override
    @Transactional
    public User createUser(UserCreateRequest requestCreate) throws IllegalArgumentException {
        if(userRepository.findUserByEmailOrLogin(requestCreate.getEmail(), requestCreate.getLogin()).isPresent()) {
            throw new IllegalArgumentException("User with login " + requestCreate.getLogin() + " already exists");
        }
        User user = UserCreateRequest.fromUserCreateRequest(requestCreate);
        user.setPassword(passwordProvider.getPassword(requestCreate.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public Page<User> findAll(Pageable pageable) throws NotFoundException {
        Page<User> users = userRepository.findAll(pageable);
        if (users.isEmpty()) {
            throw new NotFoundException("Page not found");
        }
        return users;
    }

    @Override
    @Transactional
    public User findById(Long id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(throwUserNotFound());
    }

    @Override
    @Transactional
    public User findByLogin(String login) throws NotFoundException {
        return userRepository.findByLogin(login).orElseThrow(throwUserNotFound());
    }

    @Override
    @Transactional
    public User findByEmail(String email) throws NotFoundException {
        return userRepository.findByEmail(email).orElseThrow(throwUserNotFound());
    }

    @Override
    @Transactional
    public User findByEmailOrLogin(String emailOrLogin) throws NotFoundException {
        return userRepository.findUserByEmailOrLogin(emailOrLogin, emailOrLogin).orElseThrow(throwUserNotFound());
    }
}
