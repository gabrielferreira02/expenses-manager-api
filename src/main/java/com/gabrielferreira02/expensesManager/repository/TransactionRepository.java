package com.gabrielferreira02.expensesManager.repository;

import com.gabrielferreira02.expensesManager.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {
    @Query("SELECT t FROM TransactionEntity t WHERE t.user.id = :userId")
    List<TransactionEntity> findAllById(@Param("userId") UUID userId);
}
