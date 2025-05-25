flowchart TD
subgraph "Controller Layer"
WalletController["WalletController"]
TopUpController["TopUpController"]
TransactionController["TransactionController"]
end

    subgraph "Service Layer"
        WalletService["WalletService"]
        TopUpService["TopUpService"]
    end

    subgraph "Repository Layer"
        WalletRepo["WalletRepository"]
        TransactionRepo["TransactionRepository"]
    end

    subgraph "Strategy Layer"
        TopUpStrategy["TopUpStrategy"]
        CreditCardStrat["CreditCardStrategy"]
        BankTransferStrat["BankTransferStrategy"]
        EWalletStrat["EWalletStrategy"]
    end

    subgraph "Model Layer"
        Wallet["Wallet"]
        Transaction["Transaction"]
        TransactionReq["TransactionRequest"]
        TopUpReq["TopUpRequest"]
        PaymentReq["PaymentRequest"]
        TransferReq["TransferRequest"]
    end

    WalletController -->|"uses"| WalletService
    TopUpController -->|"uses"| TopUpService
    TransactionController -->|"uses"| WalletService

    WalletService -->|"uses"| WalletRepo
    WalletService -->|"uses"| TransactionRepo
    TopUpService -->|"uses"| WalletRepo
    TopUpService -->|"uses"| TransactionRepo
    TopUpService -->|"uses"| TopUpStrategy

    TopUpStrategy --- CreditCardStrat
    TopUpStrategy --- BankTransferStrat
    TopUpStrategy --- EWalletStrat

    WalletRepo -->|"manages"| Wallet
    TransactionRepo -->|"manages"| Transaction

    TransactionReq --- TopUpReq
    TransactionReq --- PaymentReq
    TransactionReq --- TransferReq

    Wallet --o Transaction

    classDef controller fill:#f96,stroke:#333,stroke-width:1px
    classDef service fill:#bbf,stroke:#333,stroke-width:1px
    classDef repository fill:#9be,stroke:#333,stroke-width:1px
    classDef strategy fill:#fcf,stroke:#333,stroke-width:1px
    classDef model fill:#bfb,stroke:#333,stroke-width:1px

    class WalletController,TopUpController,TransactionController controller
    class WalletService,TopUpService service
    class WalletRepo,TransactionRepo repository
    class TopUpStrategy,CreditCardStrat,BankTransferStrat,EWalletStrat strategy
    class Wallet,Transaction,TransactionReq,TopUpReq,PaymentReq,TransferReq model