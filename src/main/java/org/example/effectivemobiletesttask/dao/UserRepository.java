package org.example.effectivemobiletesttask.dao;

import org.example.effectivemobiletesttask.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);

    Optional<User> findUserByEmailOrLogin(String email, String login);

    Optional<User> findByEmail(String email);

    Optional<User> findByLogin(String login);
}
