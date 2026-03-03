package com.loanSystem.Loan.Eligibility.System.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanRequestDto {
    @NotNull
    @Min(10000)
    private Double salary;

    @NotNull
    @Min(0)
    private Double expenses;

    @NotNull
    @Min(300)
    @Max(850)
    private Integer creditScore;

    @NotNull
    @Min(1000)
    private Double amount;

    @Min(1)
    @Max(60)
    private Integer termMonths = 12; // Default 12 months

    private String email; // Optional: to associate with existing user
}
