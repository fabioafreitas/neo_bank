# 💡 Use Case 1: User Registration and Account Creation

**Actors**: New User  
**Flow**:
- User registers with username/password  
- System creates a default `DEFAULT` account with balance = `0.00`  
- Admin can later upgrade to a `CASHBACK` account  

📌 *Incentive-based onboarding: “Sign up and get 5.00 bonus”*

---

# 💡 Use Case 2: Deposit Funds

**Actors**: Any user  
**Flow**:
- User initiates a deposit of $100  
- A `transactions` entry is created with:  
  - `is_deposit = true`  
  - `is_success = true`  
- User's `accounts.balance` is updated to reflect deposit  

💡 *Can trigger welcome cashback if it’s a new user or part of a promotion*

---

# 💡 Use Case 3: Make a Purchase (Debit Transaction)

**Actors**: User with `CASHBACK` account  
**Flow**:
- User spends $50  
- A `transactions` entry is added (`is_deposit = false`)  
- If account type = `CASHBACK`, a second transaction:  
  - `type = 'CASHBACK'`, `is_deposit = true`, `amount = 1.00` (2% cashback)  
- Both transactions are linked in logic (but not directly in schema)  

💡 *Build loyalty programs by adjusting cashback rates by product or category*

---

# 💡 Use Case 4: Internal Transfers (P2P)

**Actors**: Two users  
**Flow**:
- User A sends $25 to User B  
- System:  
  - Debits User A’s account  
  - Credits User B’s account  
  - Creates two `transactions`  
  - Adds one `transactions_in_house` entry:  
    - `sender_account_number = A123`  
    - `receiver_account_number = B456`  

💡 *You can allow sender/receiver messages, OTP confirmation, or scheduled transfers*

---

# 💡 Use Case 5: Admin View for System Auditing

**Actors**: Admin  
**Flow**:
- Admin logs in and views:  
  - Total users, accounts, cashback paid  
  - All failed transactions  
  - Users with suspicious activity (e.g., chargebacks)  

🔍 *Add filtering and pagination for scalability*

---

# 💡 Use Case 6: Cashback Summary Dashboard

**Actors**: Logged-in user  
**Flow**:
- User views list of their `CASHBACK` transactions  
- Can filter by month/year  
- Backend computes total cashback earned  

📊 *Useful for financial health features: “You earned $12.30 in cashback this year!”*

---

## 🎯 Bonus Ideas

- ❗ *Fraud alert system*: Detect if a user rapidly sends money to themselves using multiple accounts.  
- 📆 *Recurring transfers*: Allow users to set up scheduled internal transactions.  
- 📈 *Admin financial report*: Total volume, number of transactions, average charges.
