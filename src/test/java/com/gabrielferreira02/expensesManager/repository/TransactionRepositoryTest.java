package com.gabrielferreira02.expensesManager.repository;

import com.gabrielferreira02.expensesManager.entity.TransactionEntity;
import com.gabrielferreira02.expensesManager.entity.TransactionType;
import com.gabrielferreira02.expensesManager.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    private UserEntity savedUser;

    @BeforeEach
    void setup() {
        UserEntity user = new UserEntity();
        user.setUsername("user");
        user.setPassword("1234");
        this.savedUser = userRepository.save(user);

        TransactionEntity t = new TransactionEntity();
        t.setUser(user);
        t.setTransactionValue(200);
        t.setType(TransactionType.RECEIVED);
        transactionRepository.save(t);

        TransactionEntity t1 = new TransactionEntity();
        t1.setUser(user);
        t1.setTransactionValue(150);
        t1.setType(TransactionType.RECEIVED);
        transactionRepository.save(t1);

        TransactionEntity t2 = new TransactionEntity();
        t2.setUser(user);
        t2.setTransactionValue(70);
        t2.setType(TransactionType.PAID);
        transactionRepository.save(t2);
    }

    @Test
    void findAllById() {
        List<TransactionEntity> transactions = transactionRepository.findAllById(this.savedUser.getId());

        assertEquals(3, transactions.size());
        assertEquals(200, transactions.getFirst().getTransactionValue());
        assertEquals(TransactionType.RECEIVED, transactions.getFirst().getType());
    }

    @Test
    void findAllReceivedTransactionsById() {
        Double totalReceived = transactionRepository.findAllReceivedTransactionsById(this.savedUser.getId());

        assertNotNull(totalReceived);
        assertEquals(350, totalReceived);
    }

    @Test
    void findAllPaidTransactionsById() {
        Double totalPaid = transactionRepository.findAllPaidTransactionsById(this.savedUser.getId());

        assertNotNull(totalPaid);
        assertEquals(70, totalPaid);
    }
}