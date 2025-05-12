# API Specification (Overview)

## Auth
- `POST /api/auth/register` → Register new user (USER role)
- `POST /api/auth/login` → Authenticate and return JWT

## Accounts
- `GET /api/accounts/me` → Get current user’s account
- `GET /api/accounts/{id}` → Admin only

## Transactions
- `POST /api/transactions/deposit` → Deposit money
- `POST /api/transactions/purchase` → Make purchase
- `POST /api/transactions/transfer` → Transfer to another account
- `GET /api/transactions` → List transactions (filtered by user)
- `GET /api/transactions/cashback` → List cashback transactions

## Admin
- `GET /api/admin/users` → List users
- `GET /api/admin/reports` → Aggregated data
