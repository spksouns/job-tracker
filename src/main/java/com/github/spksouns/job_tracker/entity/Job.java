package com.github.spksouns.job_tracker.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private String role;

    private Integer salaryMin;
    private Integer salaryMax;

    @Enumerated(EnumType.STRING)
    private WorkMode workMode;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String platform;
    private String jobUrl;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private LocalDate appliedDate;

    private LocalDateTime lastUpdated;

    @PrePersist
    public void prePersist() {
        appliedDate = LocalDate.now();
        lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        lastUpdated = LocalDateTime.now();
    }

    public enum WorkMode {
        REMOTE, ONSITE, HYBRID
    }

    public enum Status {
        APPLIED, SHORTLISTED, INTERVIEW, REJECTED, OFFERED
    }
}