package com.github.spksouns.job_tracker.service;

import com.github.spksouns.job_tracker.entity.Company;
import com.github.spksouns.job_tracker.entity.User;
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

    public List<Company> getCompaniesByUser(Long userId) {
        return companyRepository.findByUserId(userId);
    }

    public Company updateCompany(Long id, Company updated,
                                 User currentUser) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Company not found!"));

        if (!company.getUser().getId()
                .equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized!");
        }

        company.setName(updated.getName());
        company.setWebsite(updated.getWebsite());
        company.setLocation(updated.getLocation());
        return companyRepository.save(company);
    }

    public void deleteCompany(Long id, User currentUser) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Company not found!"));

        if (!company.getUser().getId()
                .equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized!");
        }

        companyRepository.deleteById(id);
    }
}