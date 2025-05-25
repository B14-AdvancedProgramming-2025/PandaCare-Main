flowchart TD
subgraph "Client Layer"
WebUI["Web Interface"]
end

    subgraph "Payment & Donation Module"
        PayController["Payment Controllers"]
        PayService["Payment Services"]
        PayRepository["Repositories"]
        PayModel["Domain Models"]
        PayStrategy["Payment Strategies"]
    end

    subgraph "External Integrations"
        PayGateways["Payment Gateways"]
        AuthModule["Authentication Module"]
    end

    subgraph "Data Storage"
        DB[("PostgreSQL Database")]
    end

    WebUI <-->|"REST API Calls"| PayController
    PayController -->|"Uses"| PayService
    PayService -->|"Implements"| PayStrategy
    PayService -->|"Uses"| PayRepository
    PayRepository -->|"Maps"| PayModel
    PayRepository <-->|"Persists"| DB
    PayService <-->|"Authenticates"| AuthModule
    PayStrategy <-->|"Integrates with"| PayGateways

    classDef client fill:#fcf,stroke:#333,stroke-width:1px
    classDef controller fill:#f96,stroke:#333,stroke-width:2px
    classDef service fill:#bbf,stroke:#333,stroke-width:1px
    classDef repository fill:#9be,stroke:#333,stroke-width:1px
    classDef model fill:#bfb,stroke:#333,stroke-width:1px
    classDef external fill:#ff9,stroke:#333,stroke-width:1px
    classDef db fill:#bfb,stroke:#333,stroke-width:1px

    class WebUI client
    class PayController controller
    class PayService service
    class PayRepository repository
    class PayModel,DB model
    class PayGateways,AuthModule external
    class PayStrategy service