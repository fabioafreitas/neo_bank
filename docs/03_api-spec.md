# API Specification (Overview)

The ones marked are the implemented methods

## Auth

- [x] `POST /api/auth/registerClient` → Register new client
- [ ] `POST /api/auth/registerMerchant` → Register new merchant
- [x] `POST /api/auth/login` → Authenticate user and return JWT
- [x] `POST /api/auth/remindUsername` → Receives e-mail and sends username associated to given address if exists in it exists
- [x] `POST /api/auth/requestAccessPasswordReset` → Records a access password reset request in DB
- [x] `POST /api/auth/requestTransactionPasswordReset` → `CLIENT ROLE` Records a transaction password reset request in DB
- [x] `POST /api/auth/resetAccessPassword` → Receives new access password and token of access password reset record
- [x] `POST /api/auth/resetTransactionPassword` → `CLIENT ROLE` Receives current access password, new transaction password and token of transaction password reset record

## User Profile

- [ ] `GET /api/users/me/profile`
- [ ] `PUT /api/users/me/profile`

## Accounts

- [ ] `GET /api/accounts/me` → `CLIENT ROLE` Get current user’s account
- [ ] `GET /api/accounts` → `ADMIN ROLE` Get all user accounts
- [ ] `GET /api/accounts/{accountNumber}` → `ADMIN ROLE` Get user account by number
- [ ] `DELETE /api/accounts/{accountNumber}` → `ADMIN ROLE` Deactivate user account

## Transactions

- [ ] `POST /api/transactions/withdraw` → Withdraw money
- [ ] `POST /api/transactions/deposit` → Deposit money
- [ ] `POST /api/transactions/purchase` → Make purchase. If account is cashback and purchase if above threshold, gets cashback
- [ ] `POST /api/transactions/transfer` → Transfer to another account
- [ ] `GET /api/transactions` → List transactions (filtered by user) (TODO - add filters with start/end date, transaction types, transaction categories)
- [ ] `GET /api/transactions/cashback` → List cashback transactions

GET /api/transactions → list by filters (requires start/end date)

POST /api/transactions/withdraw

POST /api/transactions/deposit (admin approval required)

POST /api/transactions/purchase

POST /api/transactions/transfer

POST /api/transactions/submit → creates pending transaction

POST /api/transactions/approve/{id} (admin only)

POST /api/transactions/reject/{id} (admin only)

GET /api/transactions/cashback

## Admin

- [ ] `GET /api/admin/users` → List users
- [ ] `GET /api/admin/reports` → Aggregated data
