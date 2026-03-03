package com.loanSystem.Loan.Eligibility.System.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "user_financial_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFinancialProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Email
    @Column(unique = true)
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

    @Column(name = "created_at")
    private LocalDateTime createdAt=LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updateAt=LocalDateTime.now();


}
