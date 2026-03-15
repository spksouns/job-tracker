package com.github.spksouns.job_tracker.repository;

import com.github.spksouns.job_tracker.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>
{
    Optional<Company> findByName(String name);
}