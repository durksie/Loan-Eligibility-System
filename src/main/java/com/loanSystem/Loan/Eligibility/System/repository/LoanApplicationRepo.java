package com.loanSystem.Loan.Eligibility.System.repository;

import com.loanSystem.Loan.Eligibility.System.model.LoanApplication;
import com.loanSystem.Loan.Eligibility.System.model.UserFinancialProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanApplicationRepo extends JpaRepository<LoanApplication,Long> {
    @Query("SELECT l FROM LoanApplication l WHERE l.userFinancialProfile = :user")
    List<LoanApplication> findByUser(@Param("user") UserFinancialProfile user);

    @Query("SELECT l FROM LoanApplication l WHERE l.userFinancialProfile.id = :userId ORDER BY l.appliedAt DESC")
    List<LoanApplication> findRecentByUserId(@Param("userId") Long userId);

    long countByStatus(LoanApplication.LoanStatus status);

}
