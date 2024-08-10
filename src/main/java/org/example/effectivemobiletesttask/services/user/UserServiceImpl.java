package org.example.effectivemobiletesttask.services.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.effectivemobiletesttask.dao.UserRepository;
import org.example.effectivemobiletesttask.dto.user.UserRequestCreate;
import org.example.effectivemobiletesttask.entities.User;
import org.example.effectivemobiletesttask.security.PasswordProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordProvider passwordProvider;
    
    @Override
    @Transactional
    public User createUser(UserRequestCreate requestCreate) throws Exception {
        if(userRepository.findUserByEmailOrLogin(requestCreate.getEmail(), requestCreate.getLogin()).isPresent()) {
            throw new IllegalArgumentException("User with login " + requestCreate.getLogin() + " already exists");
        }
        User user = requestCreate.from();
        user.setPassword(passwordProvider.getPassword(requestCreate.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public Page<User> findAll(Pageable pageable) throws Exception {
        Page<User> users = userRepository.findAll(pageable);
        if (users.isEmpty()) {
            throw new NotFoundException("Page not found");
        }
        return users;
    }

    @Override
    @Transactional
    public User findById(Long id) throws Exception {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));
    }

    @Override
    @Transactional
    public User findByLogin(String login) throws Exception {
        return userRepository.findByLogin(login).orElseThrow(() -> new NotFoundException("User Not Found"));
    }

    @Override
    @Transactional
    public User findByEmail(String email) throws Exception {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User Not Found"));
    }

    @Override
    @Transactional
    public User findByEmailOrLogin(String emailOrLogin) throws Exception {
        return userRepository.findUserByEmailOrLogin(emailOrLogin, emailOrLogin).orElseThrow(() -> new NotFoundException("User Not Found"));
    }
}
