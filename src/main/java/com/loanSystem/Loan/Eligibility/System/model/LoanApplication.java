package com.loanSystem.Loan.Eligibility.System.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "loan_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserFinancialProfile userFinancialProfile;

    @NotNull
    @Min(1000)
    private Double amount;

    @NotNull
    @Min(1)
    @Max(60)
    private Integer termMonths;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;

    private String decisionReason;

    private Double dtiRatio;

    private Double disposableIncome;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt = LocalDateTime.now();

    @Column(name = "decision_date")
    private LocalDateTime decisionDate;

    public enum LoanStatus {
        PENDING, APPROVED, REJECTED, REVIEW
    }

    public enum RiskLevel {
        LOW, MEDIUM, HIGH, VERY_HIGH
    }




}
