package com.github.spksouns.job_tracker.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "companies")
@Data
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Company name is required!")
    @Column(nullable = false)
    private String name;

    private String website;

    private String location;
}