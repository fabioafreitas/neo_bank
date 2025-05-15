# React Frontend Map

## Pages & Components

### Public
- `/register` → `RegisterComponent`
- `/login` → `LoginComponent`

### Authenticated User
- `/dashboard` → `DashboardComponent`
- `/transactions` → `TransactionListComponent`
- `/transfer` → `TransferFormComponent`
- `/cashback` → `CashbackSummaryComponent`

### Admin Only
- `/admin/users` → `UserListComponent`
- `/admin/reports` → `ReportDashboardComponent`

## Shared
- `AuthService`, `TransactionService`, `UserService`
- JWT token interceptor and auth guard