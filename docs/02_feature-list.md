# NeoBank Feature List

| Feature                              | Description                                                  | Roles            |
|--------------------------------------|--------------------------------------------------------------|------------------|
| Client Registration                  | Creates a new client account with basic profile              | Public           |
| Merchant Registration               | Creates a new merchant account with business profile         | Public           |
| Admin Registration                  | Creates a new admin account (restricted)                     | Admin            |
| Dual Password System                | Separate passwords for access and transactions               | All              |
| Password Reset                      | Reset functionality for both access/transaction passwords     | All              |
| Username Recovery                   | Email-based username recovery system                         | Public           |
| Account Management                  | View and manage account details and status                   | Client/Admin     |
| Budget Allocation                   | Create and manage budget categories and limits               | Client           |
| Account Status Control              | Activate, suspend, or deactivate accounts                    | Admin            |

## Transaction Features
| Feature                              | Description                                                  | Roles            |
|--------------------------------------|--------------------------------------------------------------|------------------|
| Deposit Request                      | Request to add funds (requires admin approval)               | Client           |
| Withdraw                             | Remove funds using transaction password                      | Client           |
| Internal Transfer                    | Transfer between NeoBank accounts                            | Client           |
| Purchase Processing                  | Process purchases from marketplace                           | Client           |
| Transaction Approval                 | Review and approve/reject pending transactions               | Admin            |
| Transaction History                  | Detailed view of all account transactions                    | Client/Admin     |

## Merchant & Marketplace Features
| Feature                              | Description                                                  | Roles            |
|--------------------------------------|--------------------------------------------------------------|------------------|
| Product Management                   | Create, update, and delete products                          | Merchant         |
| Price Management                     | Set prices, discounts, and cashback rates                   | Merchant         |
| Product Catalog                      | Browse and search available products                         | Client           |
| Product Filtering                    | Filter by price, category, cashback rate                    | Client           |
| Merchant Dashboard                   | View and manage merchant profile and products               | Merchant         |

## Administrative Features
| Feature                              | Description                                                  | Roles            |
|--------------------------------------|--------------------------------------------------------------|------------------|
| User Management                      | Complete control over user accounts                          | Admin            |
| Transaction Monitoring               | Monitor and manage all system transactions                   | Admin            |
| Budget Analytics                     | View and analyze budget allocation usage                     | Admin            |
| Account Analytics                    | Track account status and transaction patterns               | Admin            |
| Merchant Analytics                   | Monitor merchant performance and sales                       | Admin            |
| System Reports                       | Generate comprehensive system reports                        | Admin            |

## Security Features
| Feature                              | Description                                                  | Roles            |
|--------------------------------------|--------------------------------------------------------------|------------------|
| JWT Authentication                   | Secure token-based authentication                            | All              |
| Role-Based Access                    | Granular permission control by user role                     | System           |
| Transaction Validation               | Multi-level transaction verification                         | System           |
| Activity Monitoring                  | Track and log system activities                             | Admin            |
