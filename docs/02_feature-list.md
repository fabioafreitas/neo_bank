# Feature List

| Feature                            | Description                                 | Roles     |
|------------------------------------|---------------------------------------------|-----------|
| User Registration                  | Creates a new user with role = USER         | Public    |
| Login & JWT                        | Authenticates and returns JWT token         | Public    |
| Admin Registers Admin              | Restricted to internal/admin-only use       | Admin     |
| Deposit Funds                      | Adds money to user account                  | User      |
| Purchase (Debit)                  | Reduces balance, triggers cashback if eligible | User   |
| Cashback Engine                    | Issues cashback transaction (2% of spend)   | System    |
| Internal Transfers (P2P)          | Sends money between users/accounts          | User      |
| View Transaction History           | Shows all account transactions              | User      |
| Admin Transaction Monitor          | View failed/suspicious transactions         | Admin     |
| Cashback Summary                   | User dashboard for total cashback earned    | User      |
