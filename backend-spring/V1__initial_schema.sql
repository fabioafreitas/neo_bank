-- Create ENUMs for role and account/transaction types (PostgreSQL only)
CREATE TYPE user_role AS ENUM ('ADMIN', 'USER');
CREATE TYPE account_type AS ENUM ('NORMAL', 'CASHBACK');
CREATE TYPE account_status AS ENUM ('ACTIVE', 'SUSPENDED', 'DEACTIVATED');
CREATE TYPE transaction_type AS ENUM ('WITHDRAW', 'DEPOSIT', 'PURCHASE', 'TRANSFER', 'CASHBACK');

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
    account_number TEXT NOT NULL UNIQUE,
    status account_status NOT NULL,
    type account_type NOT NULL
);

-- Index for user lookup
CREATE INDEX idx_accounts_user_id ON accounts(user_id);

-- TRANSACTIONS table
CREATE TABLE transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    type transaction_type NOT NULL,
    is_success BOOLEAN NOT NULL,
    sender_account_id UUID,
    receiver_account_id UUID,
    amount DECIMAL(15, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index for account lookup
CREATE INDEX idx_transactions_account_id ON transactions(account_id);
-- Index for transaction type lookup
CREATE INDEX idx_transactions_type ON transactions(type);