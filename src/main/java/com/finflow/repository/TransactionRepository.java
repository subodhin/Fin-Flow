package com.finflow.repository;


import com.finflow.entity.Transaction;
import com.finflow.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<com.finflow.entity.Transaction, Long> {


    List<Transaction> findByUserId(Long userId);


    Page<Transaction> findByUserId(
            Long userId,
            Pageable pageable);

    Page<Transaction> findByUserIdAndType(
            Long userId,
            TransactionType type,
            Pageable pageable);

    Page<Transaction> findByType(TransactionType type, Pageable pageable);

//Derived Query
    @Query("""
        SELECT t
        FROM Transaction t
        WHERE t.amount > :amount
        """)
    Page<Transaction> findTransactionsAboveAmount(
            @Param("amount") BigDecimal amount,
            Pageable pageable);

    @Query(value = """
        SELECT *
        FROM transactions
        WHERE amount > :amount
        """,
            nativeQuery = true)
    Page<Transaction> findTransactionsAboveAmountNative(
            @Param("amount") BigDecimal amount,
            Pageable pageable);
}



