package com.gabrielferreira02.expensesManager.service;

import com.gabrielferreira02.expensesManager.dto.ReportResponse;
import com.gabrielferreira02.expensesManager.dto.TransactionRequest;
import com.gabrielferreira02.expensesManager.entity.TransactionEntity;
import com.gabrielferreira02.expensesManager.entity.TransactionType;
import com.gabrielferreira02.expensesManager.entity.UserEntity;
import com.gabrielferreira02.expensesManager.repository.TransactionRepository;
import com.gabrielferreira02.expensesManager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void testFindAll() {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUsername("user");
        user.setPassword("1234");

        TransactionEntity t = new TransactionEntity();
        t.setTransactionValue(200.0);
        t.setType(TransactionType.RECEIVED);
        t.setUser(user);

        when(this.userRepository.existsById(user.getId())).thenReturn(true);
        when(this.transactionRepository.findAllById(user.getId())).thenReturn(List.of(t));
        List<TransactionEntity> list = this.transactionService.findAll(user.getId());

        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    public void testFindAllWithInvalidUser() {
        UUID userId = UUID.randomUUID();

        when(this.userRepository.existsById(userId)).thenReturn(false);
        assertThrows(Error.class, () -> {
            this.transactionService.findAll(userId);
        });
    }

    @Test
    public void testFindAllWithNullUser() {
        UUID userId = null;

        when(this.userRepository.existsById(userId)).thenReturn(false);
        assertThrows(Error.class, () -> {
            this.transactionService.findAll(userId);
        });
    }

    @Test
    public void testCreateTransaction() {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUsername("user");
        user.setPassword("1234");

        TransactionRequest request = new TransactionRequest(user.getId(),200,TransactionType.RECEIVED);

        when(this.userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        TransactionEntity transaction = new TransactionEntity();
        transaction.setTransactionValue(request.getValue());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUser(user);
        transaction.setType(request.getType());

        when(this.transactionRepository.save(any(TransactionEntity.class))).thenReturn(transaction);

        ResponseEntity<?> response = this.transactionService.create(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testErrorCreateTransactionWithNegativeValue() {
        TransactionRequest request = new TransactionRequest(UUID.randomUUID(),-200,TransactionType.RECEIVED);

        assertThrows(Error.class, () -> {
            this.transactionService.create(request);
        });
    }

    @Test
    public void testErrorCreateTransactionWithInvalidUser() {

        TransactionRequest request = new TransactionRequest(UUID.randomUUID(),200,TransactionType.RECEIVED);

        when(this.userRepository.findById(any(UUID.class))).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> {
            this.transactionService.create(request);
        });
    }

    @Test
    public void testErrorCreateTransactionWithNullId() {

        TransactionRequest request = new TransactionRequest(null,200,TransactionType.RECEIVED);

        assertThrows(NoSuchElementException.class, () -> {
            this.transactionService.create(request);
        });
    }

    @Test
    public void testUpdateTransaction() {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUsername("user");
        user.setPassword("1234");

        TransactionEntity t = new TransactionEntity();
        t.setUser(user);
        t.setId(UUID.randomUUID());
        t.setTransactionValue(200);
        t.setType(TransactionType.PAID);

        TransactionRequest request = new TransactionRequest(t.getId(),350, TransactionType.RECEIVED);

        when(this.transactionRepository.findById(request.getId())).thenReturn(Optional.of(t));


        when(this.transactionRepository.save(any(TransactionEntity.class))).thenReturn(t);

        ResponseEntity<?> response = this.transactionService.update(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testUpdateTransactionWithNegativeValue() {

        TransactionRequest request = new TransactionRequest(UUID.randomUUID(),-350, TransactionType.RECEIVED);

        assertThrows(Error.class, () -> {
            this.transactionService.update(request);
        });
    }

    @Test
    public void testErrorUpdateTransactionWithInvalidTransaction() {

        TransactionRequest request = new TransactionRequest(UUID.randomUUID(),200,TransactionType.RECEIVED);

        when(this.transactionRepository.findById(any(UUID.class))).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> {
            this.transactionService.update(request);
        });
    }

    @Test
    public void testDeleteTransaction() {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUsername("user");
        user.setPassword("1234");

        TransactionEntity t = new TransactionEntity();
        t.setUser(user);
        t.setId(UUID.randomUUID());
        t.setTransactionValue(200);
        t.setType(TransactionType.PAID);

        when(this.transactionRepository.existsById(any(UUID.class))).thenReturn(true);

        ResponseEntity<?> response = this.transactionService.delete(t.getId());

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteTransactionWithInvalidId() {

        when(this.transactionRepository.existsById(any(UUID.class))).thenReturn(false);

        ResponseEntity<?> response = this.transactionService.delete(UUID.randomUUID());

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testReport() {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUsername("user");
        user.setPassword("1234");

        TransactionEntity t1 = new TransactionEntity();
        t1.setUser(user);
        t1.setId(UUID.randomUUID());
        t1.setTransactionValue(300);
        t1.setType(TransactionType.RECEIVED);

        TransactionEntity t2 = new TransactionEntity();
        t2.setUser(user);
        t2.setId(UUID.randomUUID());
        t2.setTransactionValue(200);
        t2.setType(TransactionType.PAID);

        when(this.userRepository.existsById(any(UUID.class))).thenReturn(true);
        when(this.transactionRepository.findAllPaidTransactionsById(any(UUID.class))).thenReturn(t2.getTransactionValue());
        when(this.transactionRepository.findAllReceivedTransactionsById(any(UUID.class))).thenReturn(t1.getTransactionValue());

        ReportResponse response = this.transactionService.report(user.getId());

        assertNotNull(response);
        assertEquals(100, response.total());
    }

    @Test
    public void testReportWithInvalidId() {
        when(this.userRepository.existsById(any(UUID.class))).thenReturn(false);

        assertThrows(Error.class, () -> {
            this.transactionService.report(UUID.randomUUID());
        });
    }

}