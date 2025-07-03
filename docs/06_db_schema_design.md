# NeoBank Database Schema Design

## Core Tables

### users
Primary authentication and role management table.
- `id` (UUID, PK): Unique identifier
- `username` (TEXT): Unique username for authentication
- `password` (TEXT): Hashed password
- `role` (TEXT): User role (CLIENT, ADMIN, MERCHANT)

### user_profile
User personal information.
- `id` (UUID, PK, FK → users): User ID
- `first_name`, `last_name` (TEXT): User's name
- `email` (TEXT): Unique email address
- `phone`, `address_line1`, `address_line2` (TEXT): Contact information
- `city`, `province`, `postal_code`, `country` (TEXT): Address details
- `profile_picture_url` (TEXT): Profile image

### password_reset_requests
Password reset management.
- `id` (UUID, PK): Request identifier
- `user_id` (UUID, FK → users): User requesting reset
- `token` (UUID): Reset token
- `type` (TEXT): LOGIN_PASSWORD or TRANSACTION_PASSWORD
- `created_at`, `expires_at` (TIMESTAMP): Request timing

## Financial Management

### accounts
Core banking account information.
- `id` (UUID, PK): Account identifier
- `user_id` (UUID, FK → users): Account owner
- `balance` (DECIMAL): Current balance
- `account_number` (TEXT): Unique account number
- `transaction_password` (TEXT): Hashed transaction password
- `status` (TEXT): ACTIVE, SUSPENDED, DEACTIVATED
- `created_at`, `updated_at` (TIMESTAMP): Timestamps

### budget_categories
Available budget categories.
- `id` (UUID, PK): Category identifier
- `name` (TEXT): Category name

### account_budget_allocations
User's budget allocation per category.
- `id` (UUID, PK): Allocation identifier
- `account_id` (UUID, FK → accounts)
- `budget_category_id` (UUID, FK → budget_categories)
- `allocation_value` (DECIMAL): Allocated amount

### transactions
Financial transaction records.
- `id` (UUID, PK): Transaction identifier
- `transaction_number` (UUID): Public transaction reference
- `account_id` (UUID, FK → accounts): Account involved
- `budget_category_id` (UUID, FK → budget_categories)
- `operation_type` (TEXT): Type of operation
  - TRANSFER_DEBIT, TRANSFER_CREDIT
  - DEBIT, CREDIT
  - PURCHASE, CASHBACK
- `description` (TEXT): Transaction description
- `rejection_message` (TEXT): Optional rejection reason
- `status` (TEXT): PENDING, APPROVED, REJECTED
- `amount` (DECIMAL): Transaction amount
- `created_at` (TIMESTAMP): Transaction timestamp

### transfer_transactions
Links paired transfer transactions.
- `id` (UUID, PK): Transfer record identifier
- `source_transaction_id` (UUID, FK → transactions)
- `destination_transaction_id` (UUID, FK → transactions)

### transaction_requests
Transaction approval workflow.
- `id` (UUID, PK): Request identifier
- `transaction_id` (UUID, FK → transactions)
- `requested_by` (UUID, FK → users)
- `reviewed_by` (UUID, FK → users)
- `reviewed_at` (TIMESTAMP)
- `status` (TEXT): PENDING, APPROVED, REJECTED

## Marketplace

### merchants
Merchant profile information.
- `id` (UUID, PK): Merchant identifier
- `user_id` (UUID, FK → users): Merchant user account
- `store_name` (TEXT): Business name
- `description` (TEXT): Store description
- `created_at` (TIMESTAMP): Registration date

### product_categories
Available product categories.
- `id` (UUID, PK): Category identifier
- `name` (TEXT): Category name

### products
Product catalog.
- `id` (UUID, PK): Product identifier
- `merchant_id` (UUID, FK → merchants)
- `product_category_id` (UUID, FK → product_categories)
- `original_price` (DECIMAL): Base price
- `cashback_rate` (DECIMAL): Cashback percentage (0-1)
- `discount_rate` (DECIMAL): Discount percentage (0-1)
- `name`, `description` (TEXT): Product details
- `main_image_url` (TEXT): Primary product image
- `created_at` (TIMESTAMP): Product creation date

### product_features
Product characteristics.
- `id` (UUID, PK): Feature identifier
- `product_id` (UUID, FK → products)
- `title`, `description` (TEXT): Feature details

### product_images
Additional product images.
- `id` (UUID, PK): Image identifier
- `product_id` (UUID, FK → products)
- `image_url` (TEXT): Image URL
- `ordering` (INT): Display order

### purchased_products
Purchase transaction records.
- `id` (UUID, PK): Purchase identifier
- `transaction_id` (UUID, FK → transactions)
- `merchant_product_id` (UUID, FK → products)
- `product_category_id` (UUID, FK → product_categories)
- `name`, `description` (TEXT): Product snapshot
- `main_image_url` (TEXT): Main image snapshot
- `image_urls` (TEXT[]): Additional images snapshot
- `features` (JSONB): Features snapshot
- `purchased_price` (DECIMAL): Final price
- `received_cashback_rate` (DECIMAL): Applied cashback
- `applied_discount_rate` (DECIMAL): Applied discount
- `created_at` (TIMESTAMP): Purchase date

### purchased_product_features
Purchased product feature snapshots.
- `id` (UUID, PK): Feature snapshot identifier
- `purchased_product_id` (UUID, FK → purchased_products)
- `title`, `description` (TEXT): Feature details

## Database Views

### Transaction Views
- `rejected_transactions`: All rejected transactions
- `approved_transactions`: All approved transactions
- `pending_transactions`: All pending transactions

### Budget Analysis Views
- `account_budget_category_summary`: Per-account budget category summary
  - Total spent/received per category
  - Transaction count per category

- `account_budget_overview`: Budget allocations vs. actual spending
  - Allocated amounts per category
  - Total transactions per category

- `budget_category_transaction_summary`: System-wide category summary
  - Transaction count per category
  - Total amount per category

## Indexes
- Account lookup: `uq_accounts_user_id`, `uq_merchants_user_id`
- Transaction queries: Multiple indexes on transactions table
- Budget queries: Indexes on allocations and categories
- Product queries: Indexes on products and categories
- Security: Indexes on password reset requests
