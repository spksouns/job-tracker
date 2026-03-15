package com.github.spksouns.job_tracker.repository;

import com.github.spksouns.job_tracker.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByUserId(Long userId);
    List<Job> findByUserIdAndStatus(Long userId, Job.Status status);
}