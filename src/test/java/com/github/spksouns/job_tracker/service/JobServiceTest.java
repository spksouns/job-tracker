package com.github.spksouns.job_tracker.service;

import com.github.spksouns.job_tracker.entity.Job;
import com.github.spksouns.job_tracker.entity.User;
import com.github.spksouns.job_tracker.repository.JobRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    // ─────────────────────────────────────────
    // 1. getAllJobsByUser
    // ─────────────────────────────────────────
    @Test
    void getAllJobsByUser_shouldReturnJobList() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Job job1 = new Job();
        job1.setRole("SDE");
        job1.setUser(user);

        Job job2 = new Job();
        job2.setRole("Backend Engineer");
        job2.setUser(user);

        when(jobRepository.findByUserId(1L))
                .thenReturn(List.of(job1, job2));

        // Act
        List<Job> result = jobService.getAllJobsByUser(1L);

        // Assert
        assertEquals(2, result.size());
        verify(jobRepository, times(1)).findByUserId(1L);
    }

    // ─────────────────────────────────────────
    // 2. createJob
    // ─────────────────────────────────────────
    @Test
    void createJob_shouldSaveAndReturnJob() {
        // Arrange
        Job job = new Job();
        job.setRole("Java Developer");
        job.setStatus(Job.Status.APPLIED);

        when(jobRepository.save(job)).thenReturn(job);

        // Act
        Job result = jobService.createJob(job);

        // Assert
        assertNotNull(result);
        assertEquals("Java Developer", result.getRole());
        verify(jobRepository, times(1)).save(job);
    }

    // ─────────────────────────────────────────
    // 3. updateJob — success
    // ─────────────────────────────────────────
    @Test
    void updateJob_shouldUpdate_whenOwnerMatches() {
        // Arrange
        User owner = new User();
        owner.setId(1L);

        Job existingJob = new Job();
        existingJob.setId(10L);
        existingJob.setRole("Old Role");
        existingJob.setUser(owner);

        Job updatedJob = new Job();
        updatedJob.setRole("New Role");
        updatedJob.setStatus(Job.Status.INTERVIEW);

        when(jobRepository.findById(10L))
                .thenReturn(Optional.of(existingJob));
        when(jobRepository.save(existingJob))
                .thenReturn(existingJob);

        // Act
        Job result = jobService.updateJob(10L, updatedJob, owner);

        // Assert
        assertEquals("New Role", result.getRole());
        verify(jobRepository, times(1)).save(existingJob);
    }

    // ─────────────────────────────────────────
    // 4. updateJob — throws when job not found
    // ─────────────────────────────────────────
    @Test
    void updateJob_shouldThrow_whenJobNotFound() {
        // Arrange
        User user = new User();
        user.setId(1L);

        when(jobRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> jobService.updateJob(99L, new Job(), user));

        assertEquals("Job not found!", ex.getMessage());
    }

    // ─────────────────────────────────────────
    // 5. updateJob — throws when not owner
    // ─────────────────────────────────────────
    @Test
    void updateJob_shouldThrow_whenNotOwner() {
        // Arrange
        User owner = new User();
        owner.setId(1L);

        User otherUser = new User();
        otherUser.setId(2L);   // ← different user!

        Job existingJob = new Job();
        existingJob.setId(10L);
        existingJob.setUser(owner);   // job belongs to owner

        when(jobRepository.findById(10L))
                .thenReturn(Optional.of(existingJob));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> jobService.updateJob(10L, new Job(), otherUser));

        assertEquals("Unauthorized!", ex.getMessage());
    }

    // ─────────────────────────────────────────
    // 6. deleteJob — success
    // ─────────────────────────────────────────
    @Test
    void deleteJob_shouldDelete_whenOwnerMatches() {
        // Arrange
        User owner = new User();
        owner.setId(1L);

        Job job = new Job();
        job.setId(10L);
        job.setUser(owner);

        when(jobRepository.findById(10L))
                .thenReturn(Optional.of(job));

        // Act
        jobService.deleteJob(10L, owner);

        // Assert
        verify(jobRepository, times(1)).deleteById(10L);
    }

    // ─────────────────────────────────────────
    // 7. deleteJob — throws when not owner
    // ─────────────────────────────────────────
    @Test
    void deleteJob_shouldThrow_whenNotOwner() {
        // Arrange
        User owner = new User();
        owner.setId(1L);

        User otherUser = new User();
        otherUser.setId(2L);

        Job job = new Job();
        job.setId(10L);
        job.setUser(owner);

        when(jobRepository.findById(10L))
                .thenReturn(Optional.of(job));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> jobService.deleteJob(10L, otherUser));

        assertEquals("Unauthorized!", ex.getMessage());
        verify(jobRepository, never()).deleteById(any());
    }
}