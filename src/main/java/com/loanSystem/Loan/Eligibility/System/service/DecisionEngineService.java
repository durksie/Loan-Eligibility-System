package com.loanSystem.Loan.Eligibility.System.service;

import com.loanSystem.Loan.Eligibility.System.dto.LoanRequestDto;
import com.loanSystem.Loan.Eligibility.System.model.LoanApplication;
import com.loanSystem.Loan.Eligibility.System.rules.RiskCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DecisionEngineService {
    private static final double MIN_DISPOSABLE_INCOME = 3000;
    private static final double MAX_DTI_RATIO = 60;
    private static final int MIN_CREDIT_SCORE = 650;
    private static final int EXCELLENT_CREDIT_SCORE = 720;
    private static final double SAFE_DTI_RATIO = 40;

    public LoanApplication evaluate(LoanRequestDto loanRequestDto){
        // Calculate financial metrics
        double dti =calculateDTI(loanRequestDto.getExpenses(),loanRequestDto.getSalary());

        double disposableIncome= calculateDisposableIncome(loanRequestDto.getSalary(),loanRequestDto.getExpenses());

        // Determine risk level
        LoanApplication.RiskLevel riskLevel= RiskCalculator.calculateRiskLevel(loanRequestDto.getCreditScore(),dti,disposableIncome);

        // Make decision based on rules
        LoanApplication.LoanStatus descision=makeDecision(loanRequestDto.getCreditScore(),dti,disposableIncome);

        // Generate reason
        String reason=generateReason(descision,loanRequestDto.getCreditScore(),dti,disposableIncome);

        // Create loan application
        LoanApplication application=new LoanApplication();
        application.setAmount(loanRequestDto.getAmount());
        application.setTermMonths(loanRequestDto.getTermMonths());
        application.setStatus(descision);
        application.setRiskLevel(riskLevel);
        application.setDecisionReason(reason);
        application.setDtiRatio(dti);
        application.setDisposableIncome(disposableIncome);

        log.info("Loan decision made: {} for amount: {}, DTI: {}, Credit: {}",
                descision, loanRequestDto.getAmount(), dti, loanRequestDto.getCreditScore());

        return application;
    }

    private double calculateDTI(double expenses,double salary){
        if(salary<=0)return 100;

        return (expenses/salary)*100;
    }

    private double calculateDisposableIncome(double salary,double expenses){
        return salary-expenses;
    }

    private LoanApplication.LoanStatus makeDecision(int creditScore,double dti,double disposableIncome){
        // Rule 1: Credit score too low
        if (creditScore < MIN_CREDIT_SCORE) {
            return LoanApplication.LoanStatus.REJECTED;
        }

        // Rule 2: DTI too high
        if (dti > MAX_DTI_RATIO) {
            return LoanApplication.LoanStatus.REJECTED;
        }

        // Rule 3: Insufficient disposable income
        if (disposableIncome < MIN_DISPOSABLE_INCOME) {
            return LoanApplication.LoanStatus.REJECTED;
        }

        // Rule 4: Excellent profile - Auto approve
        if (creditScore > EXCELLENT_CREDIT_SCORE && dti < SAFE_DTI_RATIO) {
            return LoanApplication.LoanStatus.APPROVED;
        }

        // Rule 5: Good profile but needs review
        if (creditScore >= MIN_CREDIT_SCORE && dti <= MAX_DTI_RATIO) {
            return LoanApplication.LoanStatus.REVIEW;
        }

        return LoanApplication.LoanStatus.REVIEW;
    }

    private String generateReason(LoanApplication.LoanStatus decision,int creditScore,double dti,double disposableIncome){
        switch (decision) {
            case REJECTED:
                if (creditScore < MIN_CREDIT_SCORE) {
                    return String.format("Credit score (%d) is below minimum requirement (%d)",
                            creditScore, MIN_CREDIT_SCORE);
                }
                if (dti > MAX_DTI_RATIO) {
                    return String.format("DTI ratio (%.1f%%) exceeds maximum allowed (%.0f%%)",
                            dti, MAX_DTI_RATIO);
                }
                if (disposableIncome < MIN_DISPOSABLE_INCOME) {
                    return String.format("Disposable income (₹%.0f) is below minimum (₹%.0f)",
                            disposableIncome, MIN_DISPOSABLE_INCOME);
                }
                return "Does not meet loan eligibility criteria";

            case APPROVED:
                return String.format("Approved - Excellent credit score (%d) and healthy DTI ratio (%.1f%%)",
                        creditScore, dti);

            case REVIEW:
                return String.format("Manual review required - Credit: %d, DTI: %.1f%%, Disposable: ₹%.0f",
                        creditScore, dti, disposableIncome);

            default:
                return "Pending evaluation";
        }
    }
}
