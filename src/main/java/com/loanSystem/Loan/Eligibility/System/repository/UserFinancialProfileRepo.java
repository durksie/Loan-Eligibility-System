package com.loanSystem.Loan.Eligibility.System.repository;

import com.loanSystem.Loan.Eligibility.System.model.UserFinancialProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserFinancialProfileRepo extends JpaRepository<UserFinancialProfile,Long> {
    Optional<UserFinancialProfile> findByEmail(String email);
    boolean existsByEmail(String email);
}
