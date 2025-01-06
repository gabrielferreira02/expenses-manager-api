package com.gabrielferreira02.expensesManager.repository;

import com.gabrielferreira02.expensesManager.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    UserEntity findByUsername(String username);
}
