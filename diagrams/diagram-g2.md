# PandaCare System Architecture - Risk Analysis and Future Architecture (G2)

## Risk Analysis Summary

After analyzing our current modular monolith architecture, we've identified several potential risks that could emerge as PandaCare grows in popularity and usage:

1. **Scalability Limitations**

   - Single Spring Boot application may struggle with high user load
   - Monolithic database could become a performance bottleneck
   - Real-time chat features require different scaling patterns than other services

2. **Reliability Concerns**

   - Single point of failure in the monolithic architecture
   - Maintenance downtime affects all system features
   - No effective isolation between critical and non-critical components

3. **Development & Deployment Challenges**

   - Growing codebase complexity despite modularization
   - Deployment of the entire application for single feature updates
   - Testing becomes increasingly complex with interdependent modules

4. **Resource Efficiency Issues**
   - Inefficient resource allocation (all modules scale together)
   - Uneven resource utilization (some modules require more processing power)
   - Limited ability to optimize specific modules independently

## Updated Context Diagram

```mermaid
flowchart TD
    Guest["Guest User"]
    Pacil["Pacilian (Student)"]
    Care["Caregiver (Doctor)"]

    API["API Gateway"]
    Auth["Authentication Service"]
    Consul["Consultation Service"]
    Chat["Chat Service"]
    Legacy["Legacy Services"]
    Cache["Cache Layer"]

    DB[("Primary Database")]
    ChatDB[("Chat Database")]

    Guest -->|Register/Login| API
    Pacil -->|All Interactions| API
    Care -->|All Interactions| API

    API -->|Auth Requests| Auth
    API -->|Consultation Requests| Consul
    API -->|Chat Messages| Chat
    API -->|Other Requests| Legacy

    Auth <-->|Authentication Data| DB
    Consul <-->|Scheduling/Consultation Data| DB
    Chat <-->|Message Storage| ChatDB
    Legacy <-->|Other Data| DB

    Auth <-->|Cached Tokens/Sessions| Cache
    Consul <-->|Cached Doctor Profiles| Cache
    Chat <-->|Active Chat Sessions| Cache

    classDef gateway fill:#f96,stroke:#333,stroke-width:2px
    classDef service fill:#bbf,stroke:#333,stroke-width:1px
    classDef db fill:#bfb,stroke:#333,stroke-width:1px
    classDef user fill:#fcf,stroke:#333,stroke-width:1px
    classDef cache fill:#ff9,stroke:#333,stroke-width:1px

    class API gateway
    class Auth,Consul,Chat,Legacy service
    class DB,ChatDB db
    class Guest,Pacil,Care user
    class Cache cache
```

## Updated Container Diagram

```mermaid
flowchart TD
    subgraph "Client Layer"
        Browser["Web Browser"]
        MobileApp["Mobile App \n(Future)"]
    end

    subgraph "API Gateway Layer"
        Gateway["API Gateway"]
        LoadBalancer["Load Balancer"]
        Auth["Authentication &\nAuthorization"]
    end

    subgraph "Service Layer"
        subgraph "Extracted Microservices"
            ChatService["Chat Service"]
            ConsultService["Consultation Service"]
        end

        subgraph "Legacy Monolith"
            Spring["Spring Boot Application"]
            AcctMgmt["Account Management"]
            PayDon["Payment Module"]
            Rating["Rating Module"]
            Article["Article Module"]
        end
    end

    subgraph "Data Layer"
        PrimaryDB[("Primary PostgreSQL")]
        ChatDB[("Chat NoSQL Database")]
        Cache[("Redis Cache")]
    end

    Browser <-->|"HTTPS/WebSocket"| Gateway
    MobileApp <-->|"HTTPS/WebSocket"| Gateway

    Gateway --- LoadBalancer
    Gateway --- Auth

    LoadBalancer -->|"Route Requests"| ChatService
    LoadBalancer -->|"Route Requests"| ConsultService
    LoadBalancer -->|"Route Requests"| Spring

    ChatService <-->|"Store Messages"| ChatDB
    ChatService <-->|"Cache Active Chats"| Cache

    ConsultService <-->|"Schedule Data"| PrimaryDB
    ConsultService <-->|"Cache Doctor Profiles"| Cache

    Spring <-->|"Store/Retrieve Data"| PrimaryDB
    Spring <-->|"Cache Common Data"| Cache

    classDef client fill:#fcf,stroke:#333,stroke-width:1px
    classDef gateway fill:#f96,stroke:#333,stroke-width:2px
    classDef service fill:#bbf,stroke:#333,stroke-width:1px
    classDef monolith fill:#9be,stroke:#333,stroke-width:1px
    classDef db fill:#bfb,stroke:#333,stroke-width:1px
    classDef cache fill:#ff9,stroke:#333,stroke-width:1px

    class Browser,MobileApp client
    class Gateway,LoadBalancer,Auth gateway
    class ChatService,ConsultService service
    class Spring,AcctMgmt,PayDon,Rating,Article monolith
    class PrimaryDB,ChatDB db
    class Cache cache
```
