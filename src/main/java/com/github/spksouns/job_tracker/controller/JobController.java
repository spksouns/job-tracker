package com.github.spksouns.job_tracker.controller;

import com.github.spksouns.job_tracker.config.SecurityUtils;
import com.github.spksouns.job_tracker.entity.Job;
import com.github.spksouns.job_tracker.entity.User;
import com.github.spksouns.job_tracker.repository.UserRepository;
import com.github.spksouns.job_tracker.service.JobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityUtils securityUtils;


    @GetMapping
    public List<Job> getMyJobs() {
        User user = securityUtils.getCurrentUser();
        return jobService.getAllJobsByUser(user.getId());
    }

    @GetMapping("/status/{status}")
    public List<Job> getJobsByStatus(
            @PathVariable Job.Status status) {
        User user = securityUtils.getCurrentUser();
        return jobService.getJobsByStatus(user.getId(), status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(
            @PathVariable Long id) {
        return jobService.getJobById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Job createJob(@RequestBody @Valid Job job) {
        User user = securityUtils.getCurrentUser();
        job.setUser(user);
        return jobService.createJob(job);
    }

    @PutMapping("/{id}")
    public Job updateJob(
            @PathVariable Long id,
            @RequestBody @Valid Job job) {
        User user = securityUtils.getCurrentUser();
        return jobService.updateJob(id, job, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long id) {
        User user = securityUtils.getCurrentUser();
        jobService.deleteJob(id, user);
        return ResponseEntity.ok().build();
    }
}