CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL CHECK (role IN ('CLIENT', 'ADMIN', 'MERCHANT'))
);

CREATE TABLE accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id),
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    account_number TEXT NOT NULL UNIQUE,
    transaction_password TEXT NOT NULL,
    status TEXT NOT NULL CHECK (status IN ('ACTIVE', 'SUSPENDED', 'DEACTIVATED')),
    type TEXT NOT NULL CHECK (type IN ('DEFAULT', 'CASHBACK'))
);
CREATE INDEX idx_accounts_user_id ON accounts(user_id);

-- Categorias básicas pensadas até o momento.
-- Para facilitar a didática, apenas admins podem criar categorias.
-- Pré-população das categorias disponíveis
CREATE TABLE transaction_categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL UNIQUE
);
INSERT INTO
    transaction_categories (name)
VALUES
    ('Alimentação'),
    ('Saúde'),
    ('Educação'),
    ('Transporte'),
    ('Moradia'),
    ('Lazer'),
    ('Vestuário'),
    ('Serviços'),
    ('Comunicação'),
    ('Investimentos'),
    ('Dívidas'),
    ('Impostos'),
    ('Doações');

-- Guarda as transações bancárias realizadas
-- `type` representa o tipo de transação, dentre (WITHDRAW, DEPOSIT, PURCHASE, TRANSFER, CASHBACK)
-- `category_id` representa a categoria escolhida pelo usuário para sua organização financeira
-- se `status=pending`, então está aguardando aprovação do ADMIN do sistema
-- senão se `status=rejected`, informar `failure_message`
-- senão se `status=approved`, transação foi bem sucedida
CREATE TABLE transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_number UUID DEFAULT gen_random_uuid() UNIQUE,
    account_id UUID NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    category_id UUID NOT NULL REFERENCES transaction_categories(id),
    operation_type TEXT NOT NULL,
    description TEXT NOT NULL,
    failure_message TEXT,
    status TEXT NOT NULL CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')),
    amount DECIMAL(15, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_failure_message_only_on_rejected CHECK (
        (status = 'REJECTED' AND failure_message IS NOT NULL)
            OR (status = 'APPROVED' AND failure_message IS NULL)
            OR (status = 'PENDING' AND failure_message IS NULL)
        )
);

CREATE INDEX idx_transactions_account_id ON transactions(account_id);

CREATE VIEW rejected_transactions AS SELECT * FROM transactions WHERE status = 'REJECTED';
CREATE VIEW approved_transactions AS SELECT * FROM transactions WHERE status = 'APPROVED';
CREATE VIEW pending_transactions AS SELECT * FROM transactions WHERE status = 'PENDING';

-- The unique at the FK to `users` table constraints the reference to be 1x1
CREATE TABLE merchants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) UNIQUE,
    store_name TEXT NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE merchant_products (
       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
       merchant_id UUID REFERENCES merchants(id) ON DELETE CASCADE,
       name TEXT NOT NULL,
       description TEXT,
       original_price DECIMAL(15, 2) NOT NULL,
       cashback_value DECIMAL(15, 2) DEFAULT 0,
       has_discount BOOLEAN DEFAULT FALSE,
       discount_value DECIMAL(15, 2),
       image_urls TEXT[], -- array of URLs
       features JSONB -- key/value + description
);

-- Guarda a lógica para reset de senha. Se request for solicitado, Então link com url
-- contendo o `token` deve ser acessado para autenticar a página de atualização de senha
-- Registro só é válido se `is_used=false` e `expires_at` for maior que datetime.now()
-- Se registro for autenticado e senha for trocada com sucesso, substituir `is_used=true`
CREATE TABLE password_reset_requests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    token TEXT NOT NULL UNIQUE,
    type TEXT NOT NULL CHECK (type IN ('LOGIN_PASSWORD', 'TRANSACTION_PASSWORD')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP GENERATED ALWAYS AS (created_at + interval '30 minutes') STORED NOT NULL,
    is_used BOOLEAN DEFAULT FALSE
);

INSERT INTO public.users
    (id, username, "password", "role")
VALUES(
    '2c6b0fba-006a-4512-b546-1ba2b3ea65a3'::uuid,
    'fabioalves',
    '$2a$10$hgykf5Gp9IjiX2vN3s0cLO28jmYqVXVCy/y.Y.tIY.WQFCAQc4EE6',
    'ADMIN'
);