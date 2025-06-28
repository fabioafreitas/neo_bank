# API Specification (Overview)

The ones marked are the implemented methods

## Users

### User Auth

- [x] `POST /api/users/auth/registerClient` → Register new client
- [ ] `POST /api/users/auth/registerMerchant` → Register new merchant
- [x] `POST /api/users/auth/login` → Authenticate user and return JWT
- [x] `POST /api/users/auth/remindUsername` → Receives e-mail and sends username associated to given address if exists in it exists
- [x] `POST /api/users/auth/requestAccessPasswordReset` → Records a access password reset request in DB
- [x] `POST /api/users/auth/requestTransactionPasswordReset` → `CLIENT ROLE` Records a transaction password reset request in DB
- [x] `POST /api/users/auth/resetAccessPassword` → Receives new access password and token of access password reset record
- [x] `POST /api/users/auth/resetTransactionPassword` → `CLIENT ROLE` Receives current access password, new transaction password and token of transaction password reset record

### User Profile

- [ ] `GET /api/users/me/profile`
- [ ] `PUT /api/users/me/profile`

## Accounts

### Account Management

- [x] `GET /api/accounts/me` → `CLIENT ROLE` Get current user's account
- [x] `GET /api/accounts` → `ADMIN ROLE` Get all user accounts

  - Parameters:

    - Required (pagination):

      - `page` (integer): Page number (starting from 0 or 1, as per convention)
      - `size` (integer): Number of items per page
      - `sort` (string): Sorting field and direction, e.g. `createdAt,asc` or `amount,desc`

    - Optional (filters):
      - `status` (string): Filter by transaction status (e.g. `ACTIVE`, `SUSPENDED`, `DEACTIVATED`)
      - `minValue` (decimal): Filter accounts with balance greater than or equal to this amount
      - `maxValue` (decimal): Filter accounts with balance less than or equal to this amount

- [x] `GET /api/accounts/{accountNumber}` → `ADMIN ROLE` Get user account by number
- [x] `PUT /api/accounts/activate/{accountNumber}` → `ADMIN ROLE` Activate user account if isn't deactivated
- [x] `PUT /api/accounts/suspend/{accountNumber}` → `ADMIN ROLE` Suspend user account if isn't deactivated
- [x] `DELETE /api/accounts/deactivate/{accountNumber}` → `ADMIN ROLE` Deactivate user account and delete related user

### Account Budget Management

- [ ] `GET /api/accounts/me/budget` → `CLIENT ROLE` Get current user's budget allocations
- [ ] `POST /api/accounts/me/budget` → `CLIENT ROLE` Create budget allocation for category
- [ ] `PUT /api/accounts/me/budget/{categoryId}` → `CLIENT ROLE` Update budget allocation
- [ ] `DELETE /api/accounts/me/budget/{categoryId}` → `CLIENT ROLE` Remove budget allocation
- [ ] `GET /api/accounts/{accountNumber}/budget` → `ADMIN ROLE` Get account budget allocations

## Transactions

- [ ] `POST /api/transactions/{transactionNumber}` → `CLIENT ROLE` get a transaction by its reference number. If client isn't owner of transaction or hasn't ADMIN role, return unauthorized.
- [ ] `POST /api/transactions/withdraw` → `CLIENT ROLE` Money withdraw (DEBIT operation)
- [ ] `POST /api/transactions/depositRequest` → `CLIENT ROLE` Request a money deposit (CREDIT operation). Request will be send to admin.
- [ ] `POST /api/transactions/purchase` → `CLIENT ROLE` Purchase of a single `merchant_product`. (PURCHASE operation)
- [ ] `POST /api/transactions/transfer` → `CLIENT ROLE` Transfer to another account. Register two transactions, one for source account (TRANSFER_DEBIT) and another for debit account (TRANSFER_CREDIT). Also, adds these transactions relation in `transfer_relation` table.
- [ ] `GET /api/transactions` → `ADMIN ROLE` List transactions based on combination of any of the filters below. If none provided return all:

  - Parameters:

    - Required (pagination):

      - `page` (integer): Page number (starting from 0 or 1, as per convention)
      - `size` (integer): Number of items per page
      - `sort` (string): Sorting field and direction, e.g. `createdAt,asc` or `amount,desc`

    - Optional (filters):
      - `accountNumbers` (list[string]): Filter transactions of a list of account. If not provided, filter all accounts. e.g: [123,234,...,456]
      - `startDate` (datetime): Filter transactions from this date/time (inclusive)
      - `endDate` (datetime): Filter transactions up to this date/time (inclusive)
      - `operationTypes` (string or array): Filter by one or more operation types (e.g. `DEPOSIT`, `WITHDRAW`, `TRANSFER_DEBIT`)
      - `status` (string): Filter by transaction status (e.g. `PENDING`, `APPROVED`, `REJECTED`)
      - `minValue` (decimal): Filter transactions with value greater than or equal to this amount
      - `maxValue` (decimal): Filter transactions with value less than or equal to this amount

- [ ] `/api/transactions/me` → `CLIENT ROLE` List transactions of current user. Calls `/api/transactions/` giving only related jwt account number at `accountsNumbers` list. All other filters are accepted.
- [ ] `POST /api/transactions/approve/{transactionNumber}` → `ADMIN ROLE` Approves a pending transaction
- [ ] `POST /api/transactions/reject/{transactionNumber}` → `ADMIN ROLE` Rejects a pending transaction

## Merchants

### Merchant Management

- [ ] `GET /api/merchants/me` → `MERCHANT ROLE` Get current merchant's profile
- [ ] `PUT /api/merchants/me` → `MERCHANT ROLE` Update current merchant's profile
- [ ] `GET /api/merchants` → `ADMIN ROLE` List all merchants
- [ ] `GET /api/merchants/{merchantId}` → `ADMIN ROLE` Get merchant by ID
- [ ] `DELETE /api/merchants/{merchantId}` → `ADMIN ROLE` Deactivate merchant

### Merchant Product Management

- [ ] `GET /api/merchants/me/products` → `MERCHANT ROLE` Get current merchant's products
- [ ] `POST /api/merchants/me/products` → `MERCHANT ROLE` Create new product
- [ ] `GET /api/merchants/me/products/{productId}` → `MERCHANT ROLE` Get specific product
- [ ] `PUT /api/merchants/me/products/{productId}` → `MERCHANT ROLE` Update product
- [ ] `DELETE /api/merchants/me/products/{productId}` → `MERCHANT ROLE` Delete product
- [ ] `GET /api/merchants/{merchantId}/products` → `ADMIN ROLE` Get merchant's products by merchant ID

### Public Product Catalog

- [ ] `GET /api/products` → `CLIENT ROLE` Browse all available products (with pagination/filters)

  - Parameters:
    - Required (pagination):
      - `page` (integer): Page number
      - `size` (integer): Number of items per page
      - `sort` (string): Sorting field and direction, e.g. `name,asc` or `price,desc`
    - Optional (filters):
      - `merchantId` (UUID): Filter by specific merchant
      - `minPrice` (decimal): Minimum price filter
      - `maxPrice` (decimal): Maximum price filter
      - `search` (string): Search in product name/description

- [ ] `GET /api/products/{productId}` → `CLIENT ROLE` Get specific product details

## Management

### User & Account Management

- [ ] `GET /api/management/users` → `ADMIN ROLE` List all users with pagination/filters
- [ ] `GET /api/management/accounts` → `ADMIN ROLE` List all accounts with pagination/filters

### Budget, Account & Transaction Analytics

- [ ] `GET /api/management/analytics/budget/categories` → `ADMIN ROLE` Get budget category transaction summary across all accounts

  - Uses view: `budget_category_transaction_summary`
  - Shows total transactions and amounts per budget category

- [ ] `GET /api/management/analytics/accounts/{accountNumber}/budget` → `ADMIN ROLE` Get complete budget overview for specific account

  - Uses view: `account_budget_overview`
  - Shows allocations vs actual spending per category

- [ ] `GET /api/management/analytics/accounts/{accountNumber}/budget/summary` → `ADMIN ROLE` Get budget category spending summary for specific account

  - Uses view: `account_budget_category_summary`
  - Shows spent/received totals and transaction counts per category

- [ ] `GET /api/management/analytics/transactions/pending` → `ADMIN ROLE` List all pending transaction requests
- [ ] `GET /api/management/analytics/transactions/summary` → `ADMIN ROLE` Get transaction summary statistics
  - Total volume, count by status, by operation type, etc.

### Reports

- [ ] `GET /api/management/reports/budget-allocation-usage` → `ADMIN ROLE` Report showing budget allocation vs usage across all accounts
- [ ] `GET /api/management/reports/cashback-summary` → `ADMIN ROLE` Summary of cashback given to users
- [ ] `GET /api/management/reports/merchant-performance` → `ADMIN ROLE` Merchant sales and product performance metrics
