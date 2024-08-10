package org.example.effectivemobiletesttask.dao;

import org.example.effectivemobiletesttask.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository  extends JpaRepository<Comment, Long> {
    
}
