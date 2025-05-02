package id.ac.ui.cs.advprog.b14.pandacare.chat.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {
    private String sender;
    private String recipient;
    private String content;
    private LocalDateTime timestamp;
    
    public ChatMessage(String sender, String recipient, String content, LocalDateTime timestamp) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.timestamp = timestamp;
    }
} 