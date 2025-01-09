package com.gabrielferreira02.expensesManager.dto;

import com.gabrielferreira02.expensesManager.entity.TransactionType;

import java.util.UUID;

public record TransactionResponse(UUID id, double value, TransactionType type) {
}
