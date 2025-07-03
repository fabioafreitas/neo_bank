--------------------------------------
--  Database Initialization Script  --
--------------------------------------

CREATE TABLE IF NOT EXISTS users (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL CHECK (role IN ('CLIENT', 'ADMIN', 'MERCHANT'))
    );

CREATE TABLE IF NOT EXISTS user_profile (
                                            id UUID PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    phone TEXT,
    address_line1 TEXT,
    address_line2 TEXT,
    city TEXT,
    province TEXT,
    postal_code TEXT,
    country TEXT,
    profile_picture_url TEXT
    );

CREATE TABLE IF NOT EXISTS password_reset_requests (
                                                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    token UUID NOT NULL,
    type TEXT NOT NULL CHECK (type IN ('LOGIN_PASSWORD', 'TRANSACTION_PASSWORD')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP GENERATED ALWAYS AS (created_at + interval '30 minutes') STORED NOT NULL
    );

CREATE TABLE IF NOT EXISTS budget_categories (
                                                 id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL UNIQUE
    );

-- FK (null and without on delete cascade) is on porpouse
-- to allow deletion of users without deleting accounts 
CREATE TABLE IF NOT EXISTS accounts (
                                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE REFERENCES users(id) ON DELETE SET NULL,
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    account_number TEXT NOT NULL UNIQUE,
    transaction_password TEXT NOT NULL,
    status TEXT NOT NULL CHECK (status IN ('ACTIVE', 'SUSPENDED', 'DEACTIVATED')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS account_budget_allocations (
                                                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    budget_category_id UUID NOT NULL REFERENCES budget_categories(id) ON DELETE RESTRICT,
    allocation_value DECIMAL(15, 2) NOT NULL CHECK (allocation_value >= 0),
    UNIQUE (account_id, budget_category_id) -- Ensure unique allocation per account and category
    );

CREATE TABLE IF NOT EXISTS transactions (
                                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_number UUID NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    account_id UUID NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    budget_category_id UUID REFERENCES budget_categories(id) ON DELETE RESTRICT,
    operation_type TEXT NOT NULL CHECK (
                                           operation_type IN (
                                           'TRANSFER_DEBIT', 'TRANSFER_CREDIT',
                                           'DEBIT', 'CREDIT', 'PURCHASE', 'CASHBACK')),
    description TEXT NOT NULL,
    rejection_message TEXT,
    status TEXT NOT NULL CHECK (
                                   status IN ('PENDING', 'APPROVED', 'REJECTED')),
    amount DECIMAL(15, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_rejection_message_only_on_rejected CHECK (
(status = 'REJECTED' AND rejection_message IS NOT NULL)
    OR (status = 'APPROVED' AND rejection_message IS NULL)
    OR (status = 'PENDING' AND rejection_message IS NULL)
    )
    );

CREATE TABLE IF NOT EXISTS transfer_transactions (
                                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    source_transaction_id UUID NOT NULL REFERENCES transactions(id) ON DELETE CASCADE,
    destination_transaction_id UUID NOT NULL REFERENCES transactions(id) ON DELETE CASCADE,
    CONSTRAINT uq_source_transaction UNIQUE (source_transaction_id),
    CONSTRAINT uq_destination_transaction UNIQUE (destination_transaction_id)
    );

CREATE TABLE IF NOT EXISTS transaction_requests (
                                                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_id UUID NOT NULL REFERENCES transactions(id) ON DELETE CASCADE,
    requested_by UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    reviewed_by UUID REFERENCES users(id) ON DELETE SET NULL,
    reviewed_at TIMESTAMP,
    status TEXT NOT NULL DEFAULT 'PENDING' CHECK (
                                                     status IN ('PENDING', 'APPROVED', 'REJECTED'))
    );

CREATE TABLE IF NOT EXISTS merchants (
                                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    store_name TEXT NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );



--- I'll leave for the future the implementation of marketplace features
--- Maybe I'll never return here. But if I do, me from the future: I'd like
--- you to take care of this project. It helped us a lot the darkest hours ;)
CREATE TABLE IF NOT EXISTS product_categories (
                                                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL UNIQUE
    );


CREATE TABLE IF NOT EXISTS products (
                                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    merchant_id UUID NOT NULL REFERENCES merchants(id) ON DELETE CASCADE,
    product_category_id UUID NOT NULL REFERENCES product_categories(id) ON DELETE RESTRICT,
    original_price DECIMAL(15, 2) NOT NULL,
    cashback_rate DECIMAL(5, 4) NOT NULL DEFAULT 0 CHECK (
                                                             cashback_rate >= 0 AND cashback_rate <= 1),
    discount_rate DECIMAL(5, 4) NOT NULL DEFAULT 0 CHECK (
                                                             discount_rate >= 0 AND discount_rate <= 1),
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    main_image_url TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS product_features (
                                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    description TEXT NOT NULL
    );

CREATE TABLE IF NOT EXISTS product_images (
                                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    image_url TEXT NOT NULL,
    ordering INT
    );

CREATE TABLE IF NOT EXISTS purchased_products (
                                                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_id UUID NOT NULL REFERENCES transactions(id) ON DELETE CASCADE,
    merchant_product_id UUID REFERENCES products(id) ON DELETE SET NULL,
    product_category_id UUID NOT NULL REFERENCES product_categories(id) ON DELETE RESTRICT,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    main_image_url TEXT NOT NULL,
    image_urls TEXT[], -- snapshot of additional images at purchase time
    features JSONB NOT NULL, -- snapshot of product features at time of purchase
    purchased_price DECIMAL(15, 2) NOT NULL,
    received_cashback_rate DECIMAL(5, 4) NOT NULL DEFAULT 0 CHECK (
                                                                      received_cashback_rate >= 0 AND received_cashback_rate <= 1),
    applied_discount_rate DECIMAL(5, 4) NOT NULL DEFAULT 0 CHECK (
                                                                     applied_discount_rate >= 0 AND applied_discount_rate <= 1),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS purchased_product_features (
                                                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    purchased_product_id UUID NOT NULL REFERENCES purchased_products(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    description TEXT NOT NULL
    );




----------------------------------------
--              Indexes               --
----------------------------------------

CREATE UNIQUE INDEX IF NOT EXISTS uq_accounts_user_id ON accounts(user_id);
CREATE UNIQUE INDEX IF NOT EXISTS uq_merchants_user_id ON merchants(user_id);

CREATE INDEX IF NOT EXISTS idx_transactions_account_id ON transactions(account_id);
CREATE INDEX IF NOT EXISTS idx_transactions_budget_category_id ON transactions(budget_category_id);
CREATE INDEX IF NOT EXISTS idx_transactions_status ON transactions(status);
CREATE INDEX IF NOT EXISTS idx_transactions_created_at ON transactions(created_at);
CREATE INDEX IF NOT EXISTS idx_allocations_account_id ON account_budget_allocations(account_id);
CREATE INDEX IF NOT EXISTS idx_allocations_budget_category_id ON account_budget_allocations(budget_category_id);
CREATE INDEX IF NOT EXISTS idx_merchant_products_merchant_id ON products(merchant_id);
CREATE INDEX IF NOT EXISTS idx_merchant_products_category_id ON products(product_category_id);
CREATE INDEX IF NOT EXISTS idx_merchant_products_name ON products(name);
CREATE INDEX IF NOT EXISTS idx_purchased_products_transaction_id ON purchased_products(transaction_id);
CREATE INDEX IF NOT EXISTS idx_purchased_products_merchant_product_id ON purchased_products(merchant_product_id);
CREATE INDEX IF NOT EXISTS idx_password_reset_user_id ON password_reset_requests(user_id);
CREATE INDEX IF NOT EXISTS idx_product_features_product_id ON product_features(product_id);
CREATE INDEX IF NOT EXISTS idx_product_images_product_id ON product_images(product_id);
CREATE INDEX IF NOT EXISTS idx_purchased_products_category_id ON purchased_products(product_category_id);
CREATE INDEX IF NOT EXISTS idx_purchased_features_product_id ON purchased_product_features(purchased_product_id);



--------------------------------------
--              Views               --
--------------------------------------

CREATE VIEW rejected_transactions AS SELECT * FROM transactions WHERE status = 'REJECTED';

CREATE VIEW approved_transactions AS SELECT * FROM transactions WHERE status = 'APPROVED';

CREATE VIEW pending_transactions AS SELECT * FROM transactions WHERE status = 'PENDING';

-- Budget Category Summary per Account
-- TODO test view
CREATE OR REPLACE VIEW account_budget_category_summary AS
SELECT
    a.id AS account_id,
    bc.id AS budget_category_id,
    bc.name AS budget_category_name,
    COALESCE(SUM(CASE WHEN t.amount < 0 THEN t.amount ELSE 0 END), 0) AS total_spent,
    COALESCE(SUM(CASE WHEN t.amount > 0 THEN t.amount ELSE 0 END), 0) AS total_received,
    COUNT(t.id) AS transaction_count
FROM accounts a
         LEFT JOIN transactions t ON t.account_id = a.id
         LEFT JOIN budget_categories bc ON t.budget_category_id = bc.id
GROUP BY a.id, bc.id, bc.name;

-- All Budget Allocations and Transactions for an Account
-- TODO test view
CREATE OR REPLACE VIEW account_budget_overview AS
SELECT
    a.id AS account_id,
    bc.id AS budget_category_id,
    bc.name AS budget_category_name,
    aba.allocation_value,
    COALESCE(SUM(t.amount), 0) AS total_transactions
FROM accounts a
         JOIN account_budget_allocations aba ON aba.account_id = a.id
         JOIN budget_categories bc ON aba.budget_category_id = bc.id
         LEFT JOIN transactions t ON t.account_id = a.id AND t.budget_category_id = bc.id
GROUP BY a.id, bc.id, bc.name, aba.allocation_value;

-- Transaction Summary by Budget Category
-- TODO test view
CREATE OR REPLACE VIEW budget_category_transaction_summary AS
SELECT
    bc.id AS budget_category_id,
    bc.name AS budget_category_name,
    COUNT(t.id) AS transaction_count,
    COALESCE(SUM(t.amount), 0) AS total_amount
FROM budget_categories bc
         LEFT JOIN transactions t ON t.budget_category_id = bc.id
GROUP BY bc.id, bc.name;




----------------------------------------
--              Inserts               --
----------------------------------------

INSERT INTO budget_categories (name)
VALUES
    ('Alimentação'),('Saúde'),('Educação'),
    ('Transporte'),('Moradia'),('Lazer'),
    ('Vestuário'),('Serviços'),('Comunicação'),
    ('Investimentos'),('Dívidas'),('Impostos'),
    ('Doações'), ('Outros');

INSERT INTO product_categories (name)
VALUES
    ('Alimentos e Bebidas'),('Higiene Pessoal'),('Limpeza Doméstica'),
    ('Cuidados com Pets'),('Eletrônicos'),('Eletrodomésticos'),
    ('Moda Feminina'),('Moda Masculina'),('Moda Infantil'),
    ('Calçados'),('Acessórios'),('Papelaria'),
    ('Brinquedos'),('Móveis'),('Decoração'),
    ('Cama, Mesa e Banho'),('Material de Construção'),('Ferramentas'),
    ('Autopeças'),('Esporte e Lazer'),('Suplementos e Vitaminas'),
    ('Informática'),('Celulares e Telefonia'),('Perfumaria'),
    ('Beleza e Cosméticos'),('Bebês'),('Jardinagem'),
    ('Livros'),('Instrumentos Musicais'),('Outros');


INSERT INTO public.users
(id, username, "password", "role")
VALUES
    ('22fc95c9-7c95-4513-86b9-3e4b004f9657'::uuid,
     'fabiosysadmin',
     '$2a$10$99r5XeGPLia8eGOqTCWA9uWd8IXMOVN6b0yzmNtbzTzWQpXAG7Mty',
     'ADMIN');

INSERT INTO public.user_profile
(id, first_name, last_name, email, phone, address_line1, address_line2, city, province, postal_code, country, profile_picture_url)
VALUES
    ('22fc95c9-7c95-4513-86b9-3e4b004f9657'::uuid, 'Fábio', 'Alves',
     'fabio.alves.test@email.com', NULL, NULL, NULL,
     NULL, NULL, NULL, NULL, NULL);