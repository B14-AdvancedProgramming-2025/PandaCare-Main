classDiagram
class Wallet {
-Long id
-User user
-Double balance
-List~Transaction~ transactions
+getBalance()
+addBalance(amount)
+deductBalance(amount)
}

    class Transaction {
        -Long id
        -Double amount
        -String description
        -TransactionType type
        -LocalDateTime timestamp
        -String provider
        -Wallet wallet
    }

    class TransactionType {
        <<enumeration>>
        TOPUP
        PAYMENT
        TRANSFER
    }

    class TransactionRequest {
        -Long walletId
        -Double amount
        -String description
    }

    class TopUpRequest {
        -Map~String,String~ paymentGatewayDetails
    }

    class PaymentRequest {
        -String merchantId
        -String invoiceNumber
    }

    class TransferRequest {
        -Long recipientWalletId
        -String transferReference
    }

    class TopUpStrategy {
        <<interface>>
        +String getProviderName()
        +boolean processTopUp(wallet, request)
    }

    class CreditCardStrategy {
        +String getProviderName()
        +boolean processTopUp(wallet, request)
    }

    class BankTransferStrategy {
        +String getProviderName()
        +boolean processTopUp(wallet, request)
    }

    Wallet "1" --o "*" Transaction
    TransactionRequest <|-- TopUpRequest
    TransactionRequest <|-- PaymentRequest
    TransactionRequest <|-- TransferRequest
    TopUpStrategy <|.. CreditCardStrategy
    TopUpStrategy <|.. BankTransferStrategy
    Transaction --> TransactionType