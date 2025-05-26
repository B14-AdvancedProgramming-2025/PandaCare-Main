# PandaCare Chat Module Architecture - Individual Contribution (G4)

## Container Diagram for Chat Module

```mermaid
flowchart TD
    subgraph "Client Side"
        UI["Chat Interface (Next.js Component)"]
        WSClient["WebSocket Client"]
    end

    subgraph "PandaCare Backend"
        API["REST API Endpoints (Spring Controllers)"]
        WSEndpoint["WebSocket Endpoint (STOMP/SockJS)"]

        subgraph "Chat Module"
            ChatCtrl["Chat Controller"]
            WSCtrl["WebSocket Controller"]
            ChatSvc["Chat Service"]
            ChatRepo["Chat Repositories"]
            ChatMediator["Chat Mediator"]
        end
    end

    DB[("Supabase PostgreSQL - Chat Messages - Chat Rooms")]

    UI <-->|"REST API calls"| API
    WSClient <-->|"WebSocket Connection"| WSEndpoint

    API --> ChatCtrl
    WSEndpoint --> WSCtrl

    ChatCtrl --> ChatSvc
    WSCtrl --> ChatSvc
    ChatCtrl --> ChatMediator

    ChatSvc --> ChatRepo
    ChatRepo <-->|"CRUD Operations"| DB

    classDef client fill:#fcf,stroke:#333,stroke-width:1px
    classDef api fill:#f96,stroke:#333,stroke-width:2px
    classDef module fill:#bbf,stroke:#333,stroke-width:1px
    classDef db fill:#bfb,stroke:#333,stroke-width:1px

    class UI,WSClient client
    class API,WSEndpoint api
    class ChatCtrl,WSCtrl,ChatSvc,ChatRepo,ChatMediator module
    class DB db
```

## Component Diagram for Chat Module

```mermaid
graph TD
    ChatCtrl["Chat Controller"]
    WSCtrl["WebSocket Controller"]
    ChatSvc["Chat Service"]
    ChatRepo["Chat Repository"]
    ChatMediator["Chat Mediator"]
    DB[("Database")]

    ChatCtrl -->|"sendMessage(), getMessages()"| ChatSvc
    WSCtrl -->|"sendMessage(), getChatRooms()"| ChatSvc
    ChatCtrl -->|"registerUser(), sendMessage()"| ChatMediator
    ChatSvc -->|"save(), findBy*()"| ChatRepo
    ChatRepo -->|"CRUD operations"| DB

    classDef controller fill:#f96,stroke:#333,stroke-width:1px
    classDef service fill:#bbf,stroke:#333,stroke-width:1px
    classDef repo fill:#fcf,stroke:#333,stroke-width:1px
    classDef mediator fill:#9be,stroke:#333,stroke-width:1px
    classDef db fill:#bfb,stroke:#333,stroke-width:1px

    class ChatCtrl,WSCtrl controller
    class ChatSvc service
    class ChatRepo repo
    class ChatMediator mediator
    class DB db

    %% Note for Chat Mediator
    subgraph note ["Mediator Pattern Note"]
        Note["Implements Mediator Pattern to facilitate<br>communication between users"]
    end

    note -.-> ChatMediator
```

## Class Diagram - Chat Module Models

```mermaid
classDiagram
    class ChatMessage {
        -String id
        -String sender
        -String recipient
        -String content
        -LocalDateTime timestamp
        -ChatRoom chatRoom
        +ChatMessage(sender, recipient, content, timestamp)
        +ChatMessage(sender, recipient, content, timestamp, chatRoom)
    }

    class ChatRoom {
        -String roomId
        -String pacilianId
        -String caregiverId
        -List~ChatMessage~ messages
        +ChatRoom(roomId, pacilianId, caregiverId)
        +addMessage(message)
        +getRoomId()
    }

    ChatRoom "1" *-- "many" ChatMessage : contains
```

## Class Diagram - Chat Module Services

```mermaid
classDiagram
    class ChatService {
        <<interface>>
        +sendMessage(roomId, senderId, recipientId, content)
        +getMessagesByRoomId(roomId)
        +getChatRoomByPacilianAndCaregiver(pacilianId, caregiverId)
        +getChatRoomsByPacilianId(pacilianId)
        +getChatRoomsByCaregiverId(caregiverId)
    }

    class ChatServiceImpl {
        -ChatMessageRepository messageRepository
        -ChatRoomRepository roomRepository
        +sendMessage(roomId, senderId, recipientId, content)
        +getMessagesByRoomId(roomId)
        +getChatRoomByPacilianAndCaregiver(pacilianId, caregiverId)
        +getChatRoomsByPacilianId(pacilianId)
        +getChatRoomsByCaregiverId(caregiverId)
        -generateRoomId(pacilianId, caregiverId)
    }

    class ChatMessageRepository {
        <<interface>>
        +findByChatRoom_RoomId(roomId)
        +save(message)
    }

    class ChatRoomRepository {
        <<interface>>
        +findByRoomId(roomId)
        +findByPacilianIdAndCaregiverId(pacilianId, caregiverId)
        +findByPacilianId(pacilianId)
        +findByCaregiverId(caregiverId)
        +save(room)
    }

    ChatService <|.. ChatServiceImpl : implements
    ChatServiceImpl --> ChatMessageRepository : uses
    ChatServiceImpl --> ChatRoomRepository : uses
```

## Sequence Diagram - Sending a Chat Message

```mermaid
sequenceDiagram
    participant P as Pacilian
    participant UI as Chat UI
    participant WS as WebSocket
    participant WSCTRL as WebSocketController
    participant SVC as ChatService
    participant REPO as ChatRepository
    participant DB as Database

    P->>UI: Types and sends message
    UI->>WS: sendMessage(roomId, sender, recipient, content)
    WS->>WSCTRL: Message via STOMP
    WSCTRL->>SVC: sendMessage(roomId, sender, recipient, content)
    SVC->>SVC: Create ChatMessage object
    SVC->>REPO: save(chatMessage)
    REPO->>DB: Insert message
    DB-->>REPO: Confirm save
    REPO-->>SVC: Return saved message
    SVC-->>WSCTRL: Return confirmation
    WSCTRL->>WS: Broadcast to recipients
    WS-->>UI: Display new message
    UI-->>P: Message appears in chat
```
