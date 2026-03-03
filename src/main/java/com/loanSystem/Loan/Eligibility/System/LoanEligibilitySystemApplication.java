package com.loanSystem.Loan.Eligibility.System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoanEligibilitySystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanEligibilitySystemApplication.class, args);
        System.out.println("Loan Eligibility System Started!");
        System.out.println("API Base URL: http://localhost:8080/api/loan");
	}


}
