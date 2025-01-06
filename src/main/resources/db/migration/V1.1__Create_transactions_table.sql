CREATE TYPE transaction_type AS ENUM ('received', 'paid');
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE transaction (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    transaction_value FLOAT NOT NULL,
    type transaction_type NOT NULL,
    created_at DATE NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);