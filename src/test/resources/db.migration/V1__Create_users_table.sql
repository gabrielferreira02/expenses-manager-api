CREATE TABLE users (
    id CHAR(36) PRIMARY KEY DEFAULT RANDOM_UUID(),
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);