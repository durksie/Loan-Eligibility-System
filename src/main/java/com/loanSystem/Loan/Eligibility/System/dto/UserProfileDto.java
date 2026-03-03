package com.loanSystem.Loan.Eligibility.System.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserProfileDto {
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotNull
    @Min(0)
    private Double salary;

    @NotNull
    @Min(0)
    private Double expenses;

    @NotNull
    @Min(300)
    @Max(850)
    private Integer creditScore;
}
