package org.example.effectivemobiletesttask.services.user;

import org.example.effectivemobiletesttask.dto.user.UserRequestCreate;
import org.example.effectivemobiletesttask.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User createUser(UserRequestCreate user) throws Exception;

    Page<User> findAll(Pageable pageable) throws Exception;

    User findById(Long id) throws Exception;

    User findByLogin(String login) throws Exception;

    User findByEmail(String email) throws Exception;
    
    User findByEmailOrLogin(String emailOrLogin) throws Exception;
}
