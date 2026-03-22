package com.github.spksouns.job_tracker.service;

import com.github.spksouns.job_tracker.entity.Job;
import com.github.spksouns.job_tracker.entity.User;
import com.github.spksouns.job_tracker.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public List<Job> getAllJobsByUser(Long userId) {
        return jobRepository.findByUserId(userId);
    }

    public List<Job> getJobsByStatus(Long userId, Job.Status status) {
        return jobRepository.findByUserIdAndStatus(userId, status);
    }

    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    public Job updateJob(Long id, Job updated, User currentUser) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found!"));

        // Ownership check!
        if(!job.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized!");
        }

        job.setRole(updated.getRole());
        job.setSalaryMin(updated.getSalaryMin());
        job.setSalaryMax(updated.getSalaryMax());
        job.setStatus(updated.getStatus());
        job.setWorkMode(updated.getWorkMode());
        job.setPlatform(updated.getPlatform());
        job.setJobUrl(updated.getJobUrl());
        job.setNotes(updated.getNotes());
        return jobRepository.save(job);
    }

    public void deleteJob(Long id, User currentUser) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found!"));

        // Ownership check!
        if(!job.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized!");
        }

        jobRepository.deleteById(id);
    }
}