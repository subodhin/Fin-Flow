package com.finflow.repository;


import com.finflow.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<com.finflow.entity.Transaction, Long> {


    List<Transaction> findByUserId(Long userId);
}
