package com.github.spksouns.job_tracker.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required!")
    @Column(nullable = false)
    private String name;

    @Email(message = "Invalid email!")
    @NotBlank(message = "Email is required!")
    @Column(nullable = false, unique = true)
    private String email;

    @Size(min = 8, message = "Password min 8 chars!")
    @NotBlank(message = "Password is required!")
    @Column(nullable = false)
    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
