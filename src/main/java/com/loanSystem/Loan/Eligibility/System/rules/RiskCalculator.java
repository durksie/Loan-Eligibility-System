package com.loanSystem.Loan.Eligibility.System.rules;

import com.loanSystem.Loan.Eligibility.System.model.LoanApplication;
import org.springframework.stereotype.Component;

@Component
public class RiskCalculator {
    public static LoanApplication.RiskLevel calculateRiskLevel(int creditScore,double dti,double disposableIncome){
        int riskScore = 0;

        // Credit Score Risk (0-40 points)
        if (creditScore >= 750) riskScore += 0;
        else if (creditScore >= 700) riskScore += 10;
        else if (creditScore >= 650) riskScore += 20;
        else riskScore += 40;

        // DTI Risk (0-30 points)
        if (dti < 30) riskScore += 0;
        else if (dti < 40) riskScore += 5;
        else if (dti < 50) riskScore += 15;
        else riskScore += 30;

        // Disposable Income Risk (0-30 points)
        if (disposableIncome > 10000) riskScore += 0;
        else if (disposableIncome > 5000) riskScore += 10;
        else if (disposableIncome > 3000) riskScore += 20;
        else riskScore += 30;

        // Determine risk level
        if (riskScore <= 20) return LoanApplication.RiskLevel.LOW;
        else if (riskScore <= 40) return LoanApplication.RiskLevel.MEDIUM;
        else if (riskScore <= 60) return LoanApplication.RiskLevel.HIGH;
        else return LoanApplication.RiskLevel.VERY_HIGH;

    }

    public Double calculateRecommendedAmount(double salary,double expenses,int creditScore){
        double disposable = salary - expenses;
        double baseAmount = disposable * 12; // Annual disposable income

        // Adjust based on credit score
        if (creditScore >= 750) {
            return baseAmount * 1.5;
        } else if (creditScore >= 700) {
            return baseAmount * 1.2;
        } else if (creditScore >= 650) {
            return baseAmount * 0.8;
        } else {
            return baseAmount * 0.5;
        }
    }
}
