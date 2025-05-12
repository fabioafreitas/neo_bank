-- Create ENUMs for role and account/transaction types (PostgreSQL only)
CREATE TYPE user_role AS ENUM ('ADMIN', 'USER');
CREATE TYPE account_type AS ENUM ('NORMAL', 'CASHBACK');
CREATE TYPE transaction_type AS ENUM ('NORMAL', 'CASHBACK');

-- USERS table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role user_role NOT NULL
);

-- ACCOUNTS table
CREATE TABLE accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    type account_type NOT NULL
);

-- Index for user lookup
CREATE INDEX idx_accounts_user_id ON accounts(user_id);

-- TRANSACTIONS table
CREATE TABLE transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_number VARCHAR(50) NOT NULL UNIQUE,
    account_id UUID NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    is_deposit BOOLEAN NOT NULL,
    type transaction_type NOT NULL,
    is_success BOOLEAN NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    charge DECIMAL(15, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index for account lookup
CREATE INDEX idx_transactions_account_id ON transactions(account_id);

-- TRANSACTIONS_IN_HOUSE table
CREATE TABLE transactions_in_house (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_id UUID NOT NULL REFERENCES transactions(id) ON DELETE CASCADE,
    sender_account_number TEXT,
    receiver_account_number TEXT,
    CONSTRAINT check_sender_or_receiver CHECK (
        sender_account_number IS NOT NULL OR receiver_account_number IS NOT NULL
    )
);
