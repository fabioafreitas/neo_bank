# API Specification (Overview)

## Auth

- [x] `POST /api/auth/register` → Register new user (USER role)
- [x] `POST /api/auth/login` → Authenticate and return JWT
- [ ] `POST /api/auth/recover-username` → Sends an e-mail message containing the related username with this e-mail address if exists. I doesn't return anything, as a security feature, to prevent this route to be used as brute force to check is e-mails are stored.
- [ ] `POST /api/auth/request-password-reset` → Generates an registry in the password_reset_requests table
- [ ] `POST /api/auth/reset-password` → receives the data from a password_reset_requests row, to auth if the request is valid and of type `LOGIN_PASSWORD`. If so, performs the password reset by sending to server the new password of the user.

## User Profile

- [ ] `GET /api/users/me/profile`
- [ ] `PUT /api/users/me/profile`

## Accounts

- [x] `GET /api/accounts/me` → Get current user’s account
- [x] `GET /api/accounts/{accountNumber}` → Admin only, get user account
- [x] `PUT /api/accounts/{accountNumber}` → Admin only, update user account
- [x] `DELETE /api/accounts/{accountNumber}` → Admin only, deactivate user account
- [ ] `PUT /api/accounts/change-transaction-password` → receives the data from a password_reset_requests row, to auth if the request is valid and of type `TRANSACTION_PASSWORD`. If so, performs the password reset by sending to server the new password of the user.
- [ ] `PUT /api/accounts/change-login-password` → changes password while user is authenticated, request the old password, new password and confirm new password.

## Transactions

- [x] `POST /api/transactions/withdraw` → Withdraw money
- [x] `POST /api/transactions/deposit` → Deposit money
- [x] `POST /api/transactions/purchase` → Make purchase. If account is cashback and purchase if above threshold, gets cashback
- [x] `POST /api/transactions/transfer` → Transfer to another account
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
