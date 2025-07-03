# NeoBank Frontend Architecture

## Pages & Components

### Public Routes
- `/` → `LandingComponent`
- `/auth/login` → `LoginComponent`
- `/auth/register` → `RegisterSelectionComponent`
  - `/auth/register/client` → `ClientRegistrationComponent`
  - `/auth/register/merchant` → `MerchantRegistrationComponent`
- `/auth/recover` → `RecoverComponent`
  - `/auth/recover/username` → `UsernameRecoveryComponent`
  - `/auth/recover/password` → `PasswordResetComponent`
  - `/auth/recover/transaction-password` → `TransactionPasswordResetComponent`

### Client Routes
- `/dashboard` → `ClientDashboardComponent`
  - Account overview
  - Quick actions
  - Recent transactions
- `/profile` → `ProfileComponent`
  - Personal information
  - Security settings
- `/budget` → `BudgetManagementComponent`
  - Budget allocations
  - Spending analysis
- `/transactions` → `TransactionsComponent`
  - `/transactions/history` → `TransactionListComponent`
  - `/transactions/new` → `TransactionSelectionComponent`
    - `/transactions/new/transfer` → `TransferFormComponent`
    - `/transactions/new/withdraw` → `WithdrawFormComponent`
    - `/transactions/new/deposit` → `DepositRequestComponent`
- `/marketplace` → `MarketplaceComponent`
  - `/marketplace/products` → `ProductListComponent`
  - `/marketplace/products/:id` → `ProductDetailsComponent`
  - `/marketplace/merchants` → `MerchantListComponent`
  - `/marketplace/merchants/:id` → `MerchantDetailsComponent`

### Merchant Routes
- `/merchant/dashboard` → `MerchantDashboardComponent`
- `/merchant/products` → `ProductManagementComponent`
  - `/merchant/products/new` → `ProductFormComponent`
  - `/merchant/products/:id/edit` → `ProductEditComponent`
- `/merchant/sales` → `SalesAnalyticsComponent`
- `/merchant/profile` → `MerchantProfileComponent`

### Admin Routes
- `/admin/dashboard` → `AdminDashboardComponent`
- `/admin/users` → `UserManagementComponent`
  - User list and search
  - Status management
- `/admin/accounts` → `AccountManagementComponent`
- `/admin/transactions` → `TransactionManagementComponent`
  - Pending approvals
  - Transaction search
- `/admin/analytics` → `AnalyticsDashboardComponent`
  - Budget analytics
  - Transaction analytics
  - Merchant performance
- `/admin/reports` → `ReportGenerationComponent`

## Shared Components
- Navigation
  - `MainNavbar`
  - `SideMenu`
  - `BreadcrumbNav`
- Forms
  - `TransactionPasswordModal`
  - `ConfirmationDialog`
  - `FilterPanel`
- UI Elements
  - `StatusBadge`
  - `AmountDisplay`
  - `LoadingSpinner`
  - `ErrorBoundary`
  - `Notification`

## Services
- `AuthService` - Authentication and user management
- `AccountService` - Account operations
- `TransactionService` - Transaction operations
- `BudgetService` - Budget management
- `MarketplaceService` - Product and merchant operations
- `AnalyticsService` - Data analytics and reporting
- `NotificationService` - Real-time notifications
- `ErrorHandlingService` - Global error handling

## Global Features
- JWT token interceptor
- Role-based route guards
- Real-time updates via WebSocket
- Responsive design system
- Error boundary implementation
- Loading state management
- Form validation system

## UX Design Documentation

The UX design for NeoBank follows a user-centered approach with the following key screens. All interface designs were created with the assistance of UX Pilot AI tool ([View Project](https://uxpilot.ai/a/ui-design?page=aT7LGhVyAVXYdCgGpy7K)), which helped streamline the design process and ensure consistency across all screens.

### Public Area
1. Landing Page [View Design](ux-design/01-landing.png)
   - Clean, modern design highlighting key features
   - Clear calls-to-action for registration and login

2. Authentication 
   - [Login Screen](ux-design/02-login.png)
   - [Registration Flow](ux-design/03-register.png)
   - [Recovery Options](ux-design/04-recover.png)
   - Streamlined login process
   - Role-specific registration flows
   - Secure password recovery system

### Client Dashboard
1. Account Overview
   - [Main Dashboard](ux-design/05-01-account-dashboard.png)
   - [Profile Management](ux-design/05-02-account-profile-information.png)
   - Quick balance view
   - Recent activity summary
   - Profile management interface

2. Budget Management [View Design](ux-design/06-account-budget-management.png)
   - Visual budget allocation
   - Category-wise spending limits
   - Progress tracking

3. Transaction Management
   - [Statistics Overview](ux-design/07-account-transactions-statistics.png)
   - [Money Transfer Interface](ux-design/08-account-money-transfer.png)
   - [Transaction History](ux-design/09-account-all-transaction-details.png)
   - Transaction creation workflows
   - Detailed transaction history
   - Statistical analysis and filters

4. Marketplace
   - [Products Dashboard](ux-design/10-marketplace-dashboard.png)
   - [Product Details View](ux-design/11-marketplace-individual-product.png)
   - Product browsing interface
   - Merchant listings
   - Product detail views with purchase flow

### Design System
- Color Scheme: Professional banking colors with clear hierarchy
- Typography: Clean, readable fonts for financial data
- Components: Consistent, reusable UI elements
- Responsive: Adapts seamlessly to all device sizes
- Accessibility: WCAG 2.1 compliance for all user interfaces
