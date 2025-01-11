package com.gabrielferreira02.expensesManager.controller;

import com.gabrielferreira02.expensesManager.dto.ReportResponse;
import com.gabrielferreira02.expensesManager.dto.TransactionRequest;
import com.gabrielferreira02.expensesManager.entity.TransactionEntity;
import com.gabrielferreira02.expensesManager.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("transactions")
@PreAuthorize("hasRole('USER')")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/{id}")
    public List<TransactionEntity> findAll(@PathVariable UUID id) {
        return transactionService.findAll(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid TransactionRequest transaction) {
        return transactionService.create(transaction);
    }

    @PatchMapping
    public ResponseEntity<?> update(@RequestBody @Valid TransactionRequest transaction) {
        return transactionService.update(transaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        return transactionService.delete(id);
    }

    @GetMapping("report/{id}")
    public ReportResponse report(@PathVariable UUID id) {
        return transactionService.report(id);
    }
}
