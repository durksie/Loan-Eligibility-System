package com.loanSystem.Loan.Eligibility.System.controller;

import com.loanSystem.Loan.Eligibility.System.dto.LoanRequestDto;
import com.loanSystem.Loan.Eligibility.System.dto.LoanResponseDto;
import com.loanSystem.Loan.Eligibility.System.dto.UserProfileDto;
import com.loanSystem.Loan.Eligibility.System.model.UserFinancialProfile;
import com.loanSystem.Loan.Eligibility.System.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loan")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;
    // Create user profile
    @PostMapping("/user")
    public ResponseEntity<UserFinancialProfile> createUserProfile(
            @Valid @RequestBody UserProfileDto userProfileDto) {

        UserFinancialProfile createdUser = loanService.createUserProfile(userProfileDto);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    //apply for a loan
    @PostMapping("/apply")
    public ResponseEntity<LoanResponseDto>applyForLoan(@Valid @RequestBody LoanRequestDto loanRequestDto){
        LoanResponseDto loanResponseDto= loanService.applyForLoan(loanRequestDto);
        return new ResponseEntity<>(loanResponseDto, HttpStatus.CREATED);
    }
    //get loan decision
    @GetMapping("/{userId}")
    public ResponseEntity<LoanResponseDto>getLoanDecision(@PathVariable Long userId){
        LoanResponseDto loanResponseDto=loanService.getLoanDecision(userId);
        return ResponseEntity.ok(loanResponseDto);
    }
    //update a user profile
    @PutMapping("/user/{userId}")
    public ResponseEntity<String>updateUserProfile(@PathVariable Long userId, @Valid @RequestBody UserProfileDto userProfileDto){
        loanService.updateUserProfile(userId,userProfileDto);

        return ResponseEntity.ok("User Profile updated successfully");
    }

    //To check the health
    @GetMapping("/health")
    public ResponseEntity<String>healthCheck(){
        return ResponseEntity.ok("Loan Eligibility system is running!");
    }

}
