package com.gabrielferreira02.expensesManager.dto;

import com.gabrielferreira02.expensesManager.entity.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class TransactionRequest {
    @NotNull
    private UUID id;
    @NotNull
    private double value;
    @NotNull
    private TransactionType type;

}
