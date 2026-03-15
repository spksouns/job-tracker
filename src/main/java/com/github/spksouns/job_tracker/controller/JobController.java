package com.github.spksouns.job_tracker.controller;

import com.github.spksouns.job_tracker.entity.Job;
import com.github.spksouns.job_tracker.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/user/{userId}")
    public List<Job> getAllJobsByUser(@PathVariable Long userId) {
        return jobService.getAllJobsByUser(userId);
    }

    @GetMapping("/user/{userId}/status/{status}")
    public List<Job> getJobsByStatus(
            @PathVariable Long userId,
            @PathVariable Job.Status status) {
        return jobService.getJobsByStatus(userId, status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return jobService.getJobById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Job createJob(@RequestBody Job job) {
        return jobService.createJob(job);
    }

    @PutMapping("/{id}")
    public Job updateJob(@PathVariable Long id, @RequestBody Job job) {
        return jobService.updateJob(id, job);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok().build();
    }
}