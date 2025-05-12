# Business Logic Specification

## User
- Can register with username and password
- Role is always set to USER during registration
- Cannot choose their role manually
- Admin can promote users to ADMIN

## Account
- Automatically created upon user registration
- Account types: NORMAL, CASHBACK
- Users may have one account

## Transactions
- User can deposit funds → creates a transaction (`is_deposit = true`)
- User can make purchases → creates a transaction (`is_deposit = false`)
- CASHBACK accounts trigger a second `CASHBACK` transaction (2% reward)
- Transfers between accounts create two `transactions` and one `transactions_in_house` record

## Cashback
- Applies only for CASHBACK accounts
- Automatically triggered for eligible transactions (e.g., purchases)
- Cashback rate is currently fixed (e.g., 2%)

## Admin
- Can view user list and all transactions
- Can audit failed transactions or fraud patterns
- Can promote other users to admin (via internal tool/console)
