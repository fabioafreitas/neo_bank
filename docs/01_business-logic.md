# NeoBank Business Logic Specification

## User Management
- Three types of users: CLIENT, MERCHANT, and ADMIN
- Users register with username, password, and specific role registration endpoints
- Access password and transaction password (for financial operations) system
- Password reset functionality for both access and transaction passwords
- Email-based username recovery system

## Account Management
- Each CLIENT automatically receives an account upon registration
- Account statuses: ACTIVE, SUSPENDED, DEACTIVATED
- Only ADMIN can change account status
- Budget allocation system for better financial management
- Account balance tracking and validation

## Transaction System
- Supported operation types:
  - DEPOSIT (requires admin approval)
  - WITHDRAW (instant with transaction password)
  - TRANSFER (between accounts)
  - PURCHASE (for merchant products)
- Transaction statuses: PENDING, APPROVED, REJECTED
- All transactions are tracked and auditable
- Transfer operations create paired transactions (TRANSFER_DEBIT and TRANSFER_CREDIT)

## Budget Management
- Clients can create and manage budget allocations
- Budget categories with spending limits
- Budget tracking and analysis
- Budget vs actual spending comparison

## Merchant System
- Merchants can register and manage their profile
- Product management system:
  - Create, update, and delete products
  - Set prices and discounts
  - Define cashback rates
- Sales tracking and reporting

## Marketplace
- Clients can browse all merchants and products
- Product filtering and search capabilities
- Product categories
- Price range filtering
- Cashback and discount rate filtering

## Administrative Functions
- Complete user and account management
- Transaction approval system
- Account status management
- Analytics and reporting:
  - Budget allocation usage
  - Transaction summaries
  - Merchant performance metrics
  - Cashback statistics
- System-wide monitoring and control

## Security
- JWT-based authentication
- Role-based access control (CLIENT, MERCHANT, ADMIN)
- Dual password system:
  - Access password for login
  - Transaction password for financial operations
- Transaction validation and verification
