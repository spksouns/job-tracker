package com.github.spksouns.job_tracker.controller;

import com.github.spksouns.job_tracker.config.SecurityUtils;
import com.github.spksouns.job_tracker.entity.User;
import com.github.spksouns.job_tracker.repository.UserRepository;
import jakarta.validation.Valid;
import com.github.spksouns.job_tracker.entity.Company;
import com.github.spksouns.job_tracker.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Security;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityUtils securityUtils;

    @GetMapping
    public List<Company> getMyCompanies() {
        User user = securityUtils.getCurrentUser();
        return companyService.getCompaniesByUser(user.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(
            @PathVariable Long id) {
        return companyService.getCompanyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Company createCompany(
            @RequestBody @Valid Company company) {
        User user = securityUtils.getCurrentUser();
        company.setUser(user);
        return companyService.createCompany(company);
    }

    @PutMapping("/{id}")
    public Company updateCompany(
            @PathVariable Long id,
            @RequestBody @Valid Company company) {
        User user = securityUtils.getCurrentUser();
        return companyService.updateCompany(id, company, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(
            @PathVariable Long id) {
        User user = securityUtils.getCurrentUser();
        companyService.deleteCompany(id, user);
        return ResponseEntity.ok().build();
    }
}