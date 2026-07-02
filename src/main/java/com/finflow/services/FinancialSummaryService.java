package com.finflow.services;

import com.finflow.dto.storedProsedures.FinancialSummaryDTO;
import com.finflow.repository.FinancialSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinancialSummaryService {

    private final FinancialSummaryRepository financialSummaryRepository;

    public FinancialSummaryDTO getFinancialSummaryPr(Long userId) {
        return financialSummaryRepository.getFinancialSummary(userId);
    }
}
