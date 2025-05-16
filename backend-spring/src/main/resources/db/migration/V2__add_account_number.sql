ALTER TABLE accounts
ADD COLUMN account_number TEXT NOT NULL DEFAULT gen_random_uuid()::text UNIQUE;

ALTER TABLE transactions
DROP COLUMN IF EXISTS is_deposit,
DROP COLUMN IF EXISTS charge;

CREATE TABLE transfers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_id UUID NOT NULL REFERENCES transactions(id) ON DELETE CASCADE,
    sender_account_id UUID NOT NULL REFERENCES accounts(id),
    receiver_account_id UUID NOT NULL REFERENCES accounts(id),
    sender_account_number TEXT,
    receiver_account_number TEXT,
    CONSTRAINT check_sender_receiver_different CHECK (
        sender_account_id IS DISTINCT FROM receiver_account_id
    )
);

