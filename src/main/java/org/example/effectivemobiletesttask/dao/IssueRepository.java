package org.example.effectivemobiletesttask.dao;

import org.example.effectivemobiletesttask.entities.Issue;
import org.example.effectivemobiletesttask.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    Page<Issue> findAllByAuthorOrSupplier(User author, User supplier, Pageable pageable);

    Optional<Issue> findByTitle(String title);
}
