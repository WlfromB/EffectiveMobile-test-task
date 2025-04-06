package org.example.effectivemobiletesttask.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Entity
@Table(name = "comments")
@Data
@EnableJpaAuditing
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 150, nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "issue_id")
    private Issue issue;
    
    public Comment() {}
    
    public Comment(String text) {
        this.text = text;
    }
}
