package com.finflow.controller;

import com.finflow.dto.trasactions.PlatformSummaryDTO;
import com.finflow.dto.users.ExpenseBreakdownDTO;
import com.finflow.dto.users.TransactionAnalyticsDTO;
import com.finflow.dto.users.UserMonthlySummaryDTO;
import com.finflow.services.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/users/{userId}/monthly-summary")
    public UserMonthlySummaryDTO getMonthlySummary(
            @PathVariable Long userId) {

        return analyticsService.getMonthlySummary(userId);
    }

    @GetMapping("/users/{userId}/highest-expense")
    public TransactionAnalyticsDTO getHighestExpense(
            @PathVariable Long userId) {

        return analyticsService.getHighestExpense(userId);
    }

    @GetMapping("/users/{userId}/highest-income")
    public TransactionAnalyticsDTO getHighestIncome(
            @PathVariable Long userId) {

        return analyticsService.getHighestIncome(userId);
    }

    @GetMapping("/users/{userId}/expense-breakdown")
    public List<ExpenseBreakdownDTO> getExpenseBreakdown(
            @PathVariable Long userId) {

        return analyticsService.getExpenseBreakdown(userId);
    }

    @GetMapping("/platform-summary")
    public PlatformSummaryDTO getPlatformSummary() {

        return analyticsService.getPlatformSummary();
    }
}
