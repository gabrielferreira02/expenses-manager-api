package com.gabrielferreira02.expensesManager.service;

import com.gabrielferreira02.expensesManager.dto.TransactionRequest;
import com.gabrielferreira02.expensesManager.entity.CustomUserDetails;
import com.gabrielferreira02.expensesManager.entity.TransactionEntity;
import com.gabrielferreira02.expensesManager.entity.UserEntity;
import com.gabrielferreira02.expensesManager.repository.TransactionRepository;
import com.gabrielferreira02.expensesManager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    public List<TransactionEntity> findAll() {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return transactionRepository.findAllById(user.getId());
    }

    @Transactional
    public ResponseEntity<?> create(TransactionRequest transactionRequest) {
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
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
