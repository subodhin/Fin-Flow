package com.finflow.repository;

import com.finflow.dto.storedProsedures.FinancialSummaryDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.math.BigDecimal;

@Repository
public class FinancialSummaryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public FinancialSummaryDTO getFinancialSummary(Long userId) {

        // Create stored procedure
        StoredProcedureQuery procedure =
                entityManager.createStoredProcedureQuery(
                        "get_user_financial_summary");

        // Register IN parameter
        procedure.registerStoredProcedureParameter(
                "p_user_id",
                Long.class,
                ParameterMode.IN
        );

        // Register OUT parameters
        procedure.registerStoredProcedureParameter(
                "p_income",
                BigDecimal.class,
                ParameterMode.OUT
        );

        procedure.registerStoredProcedureParameter(
                "p_expense",
                BigDecimal.class,
                ParameterMode.OUT
        );

        procedure.registerStoredProcedureParameter(
                "p_balance",
                BigDecimal.class,
                ParameterMode.OUT
        );

        // Set input value
        procedure.setParameter("p_user_id", userId);

        // Execute stored procedure
        procedure.execute();

        // Read OUT parameters
        BigDecimal income =
                (BigDecimal) procedure.getOutputParameterValue("p_income");

        BigDecimal expense =
                (BigDecimal) procedure.getOutputParameterValue("p_expense");

        BigDecimal balance =
                (BigDecimal) procedure.getOutputParameterValue("p_balance");

        // Return DTO
        return FinancialSummaryDTO.builder()
                .totalIncome(income)
                .totalExpense(expense)
                .balance(balance)
                .build();
    }
}
