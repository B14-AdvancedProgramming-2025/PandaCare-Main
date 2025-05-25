sequenceDiagram
actor User
participant WebUI as Web Interface
participant TransCtrl as TransactionController
participant WalletSvc as WalletService
participant WalletRepo as Wallet Repository
participant TransRepo as Transaction Repository
participant DB as Database

    User->>WebUI: Make payment
    WebUI->>TransCtrl: POST /api/transaction/payment (PaymentRequest)
    TransCtrl->>WalletSvc: makePayment(walletId, amount, description)

    WalletSvc->>WalletRepo: findById(walletId)
    WalletRepo->>DB: SELECT * FROM wallet WHERE id = ?
    DB-->>WalletRepo: Wallet data
    WalletRepo-->>WalletSvc: Wallet object

    WalletSvc->>WalletSvc: Check sufficient balance
    WalletSvc->>WalletSvc: Deduct balance

    WalletSvc->>WalletRepo: save(wallet)
    WalletRepo->>DB: UPDATE wallet SET balance = ?
    DB-->>WalletRepo: Success

    WalletSvc->>TransRepo: save(transaction)
    TransRepo->>DB: INSERT INTO transaction
    DB-->>TransRepo: Success

    WalletSvc-->>TransCtrl: true
    TransCtrl-->>WebUI: 200 OK (success response)
    WebUI-->>User: Payment successful