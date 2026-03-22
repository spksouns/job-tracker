package com.github.spksouns.job_tracker.service;

import com.github.spksouns.job_tracker.entity.Company;
import com.github.spksouns.job_tracker.entity.User;
import com.github.spksouns.job_tracker.repository.CompanyRepository;
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
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    // ─────────────────────────────────────────
    // 1. createCompany
    // ─────────────────────────────────────────
    @Test
    void createCompany_shouldSaveAndReturn() {
        // Arrange
        Company company = new Company();
        company.setName("Google");
        company.setLocation("Remote");

        when(companyRepository.save(company))
                .thenReturn(company);

        // Act
        Company result = companyService.createCompany(company);

        // Assert
        assertNotNull(result);
        assertEquals("Google", result.getName());
        verify(companyRepository, times(1)).save(company);
    }

    // ─────────────────────────────────────────
    // 2. getCompaniesByUser
    // ─────────────────────────────────────────
    @Test
    void getCompaniesByUser_shouldReturnUserCompanies() {
        // Arrange
        Company c1 = new Company();
        c1.setName("Zoho");

        Company c2 = new Company();
        c2.setName("Freshworks");

        when(companyRepository.findByUserId(1L))
                .thenReturn(List.of(c1, c2));

        // Act
        List<Company> result = companyService.getCompaniesByUser(1L);

        // Assert
        assertEquals(2, result.size());
        verify(companyRepository, times(1)).findByUserId(1L);
    }

    // ─────────────────────────────────────────
    // 3. updateCompany — success
    // ─────────────────────────────────────────
    @Test
    void updateCompany_shouldUpdate_whenOwnerMatches() {
        // Arrange
        User owner = new User();
        owner.setId(1L);

        Company existing = new Company();
        existing.setId(5L);
        existing.setName("Old Name");
        existing.setUser(owner);

        Company updated = new Company();
        updated.setName("New Name");
        updated.setWebsite("https://new.com");
        updated.setLocation("Chennai");

        when(companyRepository.findById(5L))
                .thenReturn(Optional.of(existing));
        when(companyRepository.save(existing))
                .thenReturn(existing);

        // Act
        Company result = companyService.updateCompany(5L, updated, owner);

        // Assert
        assertEquals("New Name", result.getName());
        verify(companyRepository, times(1)).save(existing);
    }

    // ─────────────────────────────────────────
    // 4. updateCompany — throws when not owner
    // ─────────────────────────────────────────
    @Test
    void updateCompany_shouldThrow_whenNotOwner() {
        // Arrange
        User owner = new User();
        owner.setId(1L);

        User otherUser = new User();
        otherUser.setId(2L);

        Company existing = new Company();
        existing.setId(5L);
        existing.setUser(owner);

        when(companyRepository.findById(5L))
                .thenReturn(Optional.of(existing));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> companyService.updateCompany(5L, new Company(), otherUser));

        assertEquals("Unauthorized!", ex.getMessage());
    }

    // ─────────────────────────────────────────
    // 5. deleteCompany — throws when not found
    // ─────────────────────────────────────────
    @Test
    void deleteCompany_shouldThrow_whenNotFound() {
        // Arrange
        when(companyRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> companyService.deleteCompany(99L, new User()));

        assertEquals("Company not found!", ex.getMessage());
    }

    // ─────────────────────────────────────────
    // 6. deleteCompany — success
    // ─────────────────────────────────────────
    @Test
    void deleteCompany_shouldDelete_whenOwnerMatches() {
        // Arrange
        User owner = new User();
        owner.setId(1L);

        Company company = new Company();
        company.setId(5L);
        company.setUser(owner);

        when(companyRepository.findById(5L))
                .thenReturn(Optional.of(company));

        // Act
        companyService.deleteCompany(5L, owner);

        // Assert
        verify(companyRepository, times(1)).deleteById(5L);
    }
}