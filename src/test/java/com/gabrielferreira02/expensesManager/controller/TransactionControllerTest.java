package com.gabrielferreira02.expensesManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielferreira02.expensesManager.dto.ReportResponse;
import com.gabrielferreira02.expensesManager.dto.TransactionRequest;
import com.gabrielferreira02.expensesManager.entity.TransactionEntity;
import com.gabrielferreira02.expensesManager.entity.TransactionType;
import com.gabrielferreira02.expensesManager.entity.UserEntity;
import com.gabrielferreira02.expensesManager.repository.TransactionRepository;
import com.gabrielferreira02.expensesManager.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    @WithMockUser(roles = "USER")
    @Test
    public void testFindAllWithAuthorizedAccess() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUsername("user");
        user.setPassword("1234");

        TransactionEntity t = new TransactionEntity();
        t.setUser(user);
        t.setTransactionValue(200);
        t.setType(TransactionType.PAID);

        List<TransactionEntity> transactions = List.of(t);
        when(transactionService.findAll(user.getId())).thenReturn(transactions);

        mockMvc.perform(get("/transactions/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(transactions.size()));

    }

    @Test
    public void testFindAllWithUnauthorizedAccess() throws Exception {

        mockMvc.perform(get("/transactions/{id}", UUID.randomUUID()))
                .andExpect(status().isForbidden());

    }

    @WithMockUser(roles = "USER")
    @Test
    public void testCreateWithAuthorizedAccess() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUsername("user");
        user.setPassword("1234");

        TransactionRequest t = new TransactionRequest(user.getId(), 200, TransactionType.RECEIVED);

        TransactionEntity transaction = new TransactionEntity();
        transaction.setUser(user);
        transaction.setTransactionValue(t.getValue());
        transaction.setType(t.getType());
        transaction.setCreatedAt(LocalDateTime.now());

        String body = objectMapper.writeValueAsString(t);

        ResponseEntity<TransactionEntity> response = ResponseEntity.ok(transaction);
        when(transactionService.create(any(TransactionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionValue").value(transaction.getTransactionValue()))
                .andExpect(jsonPath("$.type").value(transaction.getType().toString()));

    }

    @Test
    public void testCreateWithUnauthorizedAccess() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUsername("user");
        user.setPassword("1234");

        TransactionRequest t = new TransactionRequest(user.getId(), 200, TransactionType.RECEIVED);

        String body = objectMapper.writeValueAsString(t);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = "USER")
    @Test
    public void testUpdateWithAuthorizedAccess() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUsername("user");
        user.setPassword("1234");

        UUID id = UUID.randomUUID();

        TransactionRequest t = new TransactionRequest(id, 200, TransactionType.RECEIVED);

        TransactionEntity transaction = new TransactionEntity();
        transaction.setId(id);
        transaction.setUser(user);
        transaction.setTransactionValue(t.getValue());
        transaction.setType(t.getType());
        transaction.setCreatedAt(LocalDateTime.now());

        String body = objectMapper.writeValueAsString(t);

        ResponseEntity<TransactionEntity> response = ResponseEntity.ok(transaction);
        when(transactionService.update(any(TransactionRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionValue").value(transaction.getTransactionValue()))
                .andExpect(jsonPath("$.type").value(transaction.getType().toString()));

    }

    @Test
    public void testUpdateWithUnauthorizedAccess() throws Exception {
        UUID id = UUID.randomUUID();

        TransactionRequest t = new TransactionRequest(id, 200, TransactionType.RECEIVED);

        String body = objectMapper.writeValueAsString(t);

        mockMvc.perform(patch("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                )
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = "USER")
    @Test
    public void testDeleteWithAuthorizedAccess() throws Exception {
        UUID id = UUID.randomUUID();

        when(this.transactionRepository.existsById(any(UUID.class))).thenReturn(true);

        mockMvc.perform(delete("/transactions/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteWithUnauthorizedAccess() throws Exception {

        mockMvc.perform(delete("/transactions/{id}", UUID.randomUUID()))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(roles = "USER")
    @Test
    public void testReportWithAuthorizedAccess() throws Exception {
        UUID id = UUID.randomUUID();

        when(this.transactionService.report(any(UUID.class))).thenReturn(new ReportResponse(300.0));

        mockMvc.perform(get("/transactions/report/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(300));
    }

    @Test
    public void testReportWithUnauthorizedAccess() throws Exception {

        mockMvc.perform(get("/transactions/report/{id}", UUID.randomUUID()))
                .andExpect(status().isForbidden());
    }

}