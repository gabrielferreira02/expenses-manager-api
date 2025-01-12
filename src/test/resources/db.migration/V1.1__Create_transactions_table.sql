CREATE TABLE transaction (
    id CHAR(36) PRIMARY KEY DEFAULT RANDOM_UUID(),
    user_id CHAR(36) NOT NULL,
    transaction_value FLOAT NOT NULL,
    type VARCHAR(20) NOT NULL,
    created_at DATE NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);