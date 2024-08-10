package org.example.effectivemobiletesttask.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String login;
    
    @Column(nullable = false)
    private String password;
    
    @Column(unique = true,nullable = false)
    private String email;    
    
    @OneToMany
    private Set<Row> rows = new HashSet<>();
    
    
}
