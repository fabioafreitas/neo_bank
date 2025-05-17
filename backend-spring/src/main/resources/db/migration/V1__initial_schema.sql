-- USERS table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL
);

-- ACCOUNTS table
CREATE TABLE accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id),
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    account_number TEXT NOT NULL,
    status TEXT NOT NULL,
    type TEXT NOT NULL
);

-- Index for user lookup
CREATE INDEX idx_accounts_user_id ON accounts(user_id);

-- TRANSACTIONS table
CREATE TABLE transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_number UUID DEFAULT gen_random_uuid() UNIQUE,
    account_id UUID NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    type TEXT NOT NULL,
    description TEXT NOT NULL,
    failure_message TEXT,
    is_success BOOLEAN NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_failure_message_only_on_failure CHECK (
        (is_success = false AND failure_message IS NOT NULL)
        OR (is_success = true AND failure_message IS NULL)
    )
);

-- Index for account lookup
CREATE INDEX idx_transactions_account_id ON transactions(account_id);