package id.ac.ui.cs.advprog.b14.pandacare.chat.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    private String sender;
    private String recipient;
    private String content;
    
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime timestamp;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;
    
    public ChatMessage(String sender, String recipient, String content, LocalDateTime timestamp) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.timestamp = timestamp;
    }
    
    public ChatMessage(String sender, String recipient, String content, LocalDateTime timestamp, ChatRoom chatRoom) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.timestamp = timestamp;
        this.chatRoom = chatRoom;
    }
} 