package com.loanSystem.Loan.Eligibility.System.service;

import com.loanSystem.Loan.Eligibility.System.dto.LoanRequestDto;
import com.loanSystem.Loan.Eligibility.System.dto.LoanResponseDto;
import com.loanSystem.Loan.Eligibility.System.dto.UserProfileDto;
import com.loanSystem.Loan.Eligibility.System.model.LoanApplication;
import com.loanSystem.Loan.Eligibility.System.model.UserFinancialProfile;
import com.loanSystem.Loan.Eligibility.System.repository.LoanApplicationRepo;
import com.loanSystem.Loan.Eligibility.System.repository.UserFinancialProfileRepo;
import com.loanSystem.Loan.Eligibility.System.rules.RiskCalculator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.loanSystem.Loan.Eligibility.System.model.LoanApplication.LoanStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanService {

    private final LoanApplicationRepo loanApplicationRepo;
    private final UserFinancialProfileRepo userFinancialProfileRepo;
    private final DecisionEngineService decisionEngineService;
    private final RiskCalculator riskCalculator;




    @Transactional
    public LoanResponseDto applyForLoan(LoanRequestDto loanRequestDto){
        log.info("Processing loan application for amount: {}", loanRequestDto.getAmount());

        // Find or create user profile
        UserFinancialProfile userFinancialProfile = findOrCreateUser(loanRequestDto);

        // Evaluate loan using decision engine
        LoanApplication loanApplication = decisionEngineService.evaluate(loanRequestDto);
        loanApplication.setUserFinancialProfile(userFinancialProfile);
        loanApplication.setDecisionDate(LocalDateTime.now());

        // Calculate recommended amount
        Double recommendedAmount = riskCalculator.calculateRecommendedAmount(
                loanRequestDto.getSalary(),loanRequestDto.getExpenses(), loanRequestDto.getCreditScore()
        );

        // Save application
        LoanApplication savedApplication = loanApplicationRepo.save(loanApplication);

        log.info("Loan application saved with ID: {}, Decision: {}",
                savedApplication.getId(), savedApplication.getStatus());

        // Build response
        return buildResponse(savedApplication, recommendedAmount);
    }

    private UserFinancialProfile findOrCreateUser(LoanRequestDto loanRequestDto){
        if (loanRequestDto.getEmail() != null && !loanRequestDto.getEmail().isEmpty()) {
            return userFinancialProfileRepo.findByEmail(loanRequestDto.getEmail())
                    .orElseGet(() -> createUserProfile(loanRequestDto));
        }
        return createUserProfile(loanRequestDto);
    }

    private UserFinancialProfile createUserProfile(LoanRequestDto loanRequestDto){
        UserFinancialProfile userFinancialProfile = new UserFinancialProfile();

        userFinancialProfile.setName(loanRequestDto.getEmail() != null ? loanRequestDto.getEmail().split("@")[0] : "Anonymous");
        userFinancialProfile.setEmail(loanRequestDto.getEmail() != null ? loanRequestDto.getEmail() : "temp@email.com");
        userFinancialProfile.setSalary(loanRequestDto.getSalary());
        userFinancialProfile.setExpenses(loanRequestDto.getExpenses());
        userFinancialProfile.setCreditScore(loanRequestDto.getCreditScore());
        return userFinancialProfileRepo.save(userFinancialProfile);
    }

    public  LoanResponseDto getLoanDecision(Long id){
        LoanApplication loanApplication=loanApplicationRepo.findById(id).orElseThrow(()-> new RuntimeException("Loan application not found with ID: "+ id));

        Double recommendedAmount = riskCalculator.calculateRecommendedAmount(
                loanApplication.getUserFinancialProfile().getSalary(),
                loanApplication.getUserFinancialProfile().getExpenses(),
                loanApplication.getUserFinancialProfile().getCreditScore()
        );

        return buildResponse(loanApplication, recommendedAmount);
    }

    private LoanResponseDto buildResponse(LoanApplication loanApplication,Double recommendedAmount){
        return LoanResponseDto.builder()
                .applicationId(loanApplication.getId())
                .decision(loanApplication.getStatus())
                .riskLevel(loanApplication.getRiskLevel())
                .reason(loanApplication.getDecisionReason())
                .dtiRatio(loanApplication.getDtiRatio())
                .disposableIncome(loanApplication.getDisposableIncome())
                .message(generateUserMessage(loanApplication))
                .recommendedAmount(recommendedAmount)
                .build();
    }
    private String generateUserMessage(LoanApplication loanApplication){
        switch (loanApplication.getStatus()) {
            case APPROVED:
                return "Congratulations! Your loan has been approved.";
            case REJECTED:
                return "We regret to inform you that your loan application has been rejected.";
            case REVIEW:
                return "Your application requires manual review. We'll contact you soon.";
            default:
                return "Your application is being processed.";
        }
    }

    @Transactional
    public void updateUserProfile(Long userId, UserProfileDto userProfileDto){
        UserFinancialProfile userFinancialProfile=userFinancialProfileRepo.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        userFinancialProfile.setName(userProfileDto.getName());
        userFinancialProfile.setEmail(userProfileDto.getEmail());
        userFinancialProfile.setSalary(userProfileDto.getSalary());
        userFinancialProfile.setExpenses(userProfileDto.getExpenses());
        userFinancialProfile.setCreditScore(userProfileDto.getCreditScore());
        userFinancialProfile.setUpdateAt(LocalDateTime.now());

        userFinancialProfileRepo.save(userFinancialProfile);
        log.info("Updated user profile for ID: {}", userId);
    }

}
