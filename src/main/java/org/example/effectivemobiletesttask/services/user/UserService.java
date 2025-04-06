package org.example.effectivemobiletesttask.services.user;

import org.example.effectivemobiletesttask.dto.user.UserCreateRequest;
import org.example.effectivemobiletesttask.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.webjars.NotFoundException;

public interface UserService {
    User createUser(UserCreateRequest user) throws IllegalArgumentException;

    Page<User> findAll(Pageable pageable) throws NotFoundException;

    User findById(Long id) throws NotFoundException;

    User findByLogin(String login) throws NotFoundException;

    User findByEmail(String email) throws NotFoundException;
    
    User findByEmailOrLogin(String emailOrLogin) throws NotFoundException;
}
