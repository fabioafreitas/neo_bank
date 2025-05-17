# API Specification (Overview)

## Auth
- [x] `POST /api/auth/register` → Register new user (USER role)
- [x] `POST /api/auth/login` → Authenticate and return JWT

## Accounts
- [x] `GET /api/accounts/me` → Get current user’s account
- [x] `GET /api/accounts/{accountNumber}` → Admin only, get user account
- [x] `PUT /api/accounts/{accountNumber}` → Admin only, update user account
- [x] `DELETE /api/accounts/{accountNumber}` → Admin only, deactivate user account

## Transactions
- [x] `POST /api/transactions/withdraw` → Withdraw money
- [x] `POST /api/transactions/deposit` → Deposit money
- [x] `POST /api/transactions/purchase` → Make purchase. If account is cashback and purchase if above threshold, gets cashback
- [x] `POST /api/transactions/transfer` → Transfer to another account
- [x] `GET /api/transactions` → List transactions (filtered by user)
- [ ] `GET /api/transactions/cashback` → List cashback transactions

## Admin
- [ ] `GET /api/admin/users` → List users
- [ ] `GET /api/admin/reports` → Aggregated data
