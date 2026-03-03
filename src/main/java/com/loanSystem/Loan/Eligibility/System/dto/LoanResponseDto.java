package com.loanSystem.Loan.Eligibility.System.dto;

import com.loanSystem.Loan.Eligibility.System.model.LoanApplication;
import lombok.Builder;
import lombok.Data;


@Data
//Automatically implements the Builder design pattern for your class.
@Builder
public class LoanResponseDto {
    private Long applicationId;
    private LoanApplication.LoanStatus decision;
    private LoanApplication.RiskLevel riskLevel;
    private String reason;
    private Double dtiRatio;
    private Double disposableIncome;
    private String message;
    private Double recommendedAmount;
}
