package com.gabrielferreira02.expensesManager.repository;

import com.gabrielferreira02.expensesManager.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {
    @Query("SELECT t FROM TransactionEntity t WHERE t.user.id = :userId ORDER BY t.createdAt ASC ")
    List<TransactionEntity> findAllById(@Param("userId") UUID userId);
    @Query("SELECT SUM(t.transactionValue) FROM TransactionEntity t " +
            "WHERE t.user.id = :userId AND t.type = 'RECEIVED' " +
            "AND EXTRACT(MONTH FROM t.createdAt) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND EXTRACT(YEAR FROM t.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE)")
    Double findAllReceivedTransactionsById(@Param("userId") UUID userId);
    @Query("SELECT SUM(t.transactionValue) FROM TransactionEntity t " +
            "WHERE t.user.id = :userId AND t.type = 'PAID' " +
            "AND EXTRACT(MONTH FROM t.createdAt) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND EXTRACT(YEAR FROM t.createdAt) = EXTRACT(YEAR FROM CURRENT_DATE)")
    Double findAllPaidTransactionsById(@Param("userId") UUID userId);
}
