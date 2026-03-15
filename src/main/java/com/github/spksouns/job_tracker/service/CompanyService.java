package com.github.spksouns.job_tracker.service;

import com.github.spksouns.job_tracker.entity.Company;
import com.github.spksouns.job_tracker.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company updateCompany(Long id, Company updated) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found!"));
        company.setName(updated.getName());
        company.setWebsite(updated.getWebsite());
        company.setLocation(updated.getLocation());
        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}