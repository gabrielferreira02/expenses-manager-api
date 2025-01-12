package com.gabrielferreira02.expensesManager.service;

import com.gabrielferreira02.expensesManager.dto.ReportResponse;
import com.gabrielferreira02.expensesManager.dto.TransactionRequest;
import com.gabrielferreira02.expensesManager.entity.TransactionEntity;
import com.gabrielferreira02.expensesManager.entity.UserEntity;
import com.gabrielferreira02.expensesManager.repository.TransactionRepository;
import com.gabrielferreira02.expensesManager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    public List<TransactionEntity> findAll(UUID id) {
        if(!userRepository.existsById(id)) {
            throw new Error("User not found");
        }
        return transactionRepository.findAllById(id);
    }

    @Transactional
    public ResponseEntity<?> create(TransactionRequest transactionRequest) {
        if(transactionRequest.getValue() < 0) {
            throw new Error("Field value can't be negative");
        }

        UserEntity user = userRepository.findById(transactionRequest.getId())
                .orElseThrow();

        TransactionEntity transaction = new TransactionEntity();
        transaction.setUser(user);
        transaction.setTransactionValue(transactionRequest.getValue());
        transaction.setType(transactionRequest.getType());

        return ResponseEntity.ok(transactionRepository.save(transaction));
    }

    @Transactional
    public ResponseEntity<?> update(TransactionRequest transactionRequest) {
        if(transactionRequest.getValue() < 0) {
            throw new Error("Field value can't be negative");
        }

        TransactionEntity transaction = transactionRepository.findById(transactionRequest.getId())
                .orElseThrow();

        transaction.setType(transactionRequest.getType());
        transaction.setTransactionValue(transactionRequest.getValue());

        return ResponseEntity.ok(transactionRepository.save(transaction));
    }

    @Transactional
    public ResponseEntity<?> delete(UUID id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    public ReportResponse report(UUID id) {
        if(!userRepository.existsById(id)) {
            throw new Error("User not found");
        }
        Double paid = transactionRepository.findAllPaidTransactionsById(id);
        Double received = transactionRepository.findAllReceivedTransactionsById(id);
        if(received == null) received = 0.0;
        if(paid == null) paid = 0.0;
        Double total = received - paid;
        return new ReportResponse(total);
    }
}
