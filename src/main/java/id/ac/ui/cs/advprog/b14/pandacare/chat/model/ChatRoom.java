package id.ac.ui.cs.advprog.b14.pandacare.chat.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_rooms")
@Getter
@Setter
@NoArgsConstructor
public class ChatRoom {
    @Id
    private String roomId;
    
    private String pacilianId;
    private String caregiverId;
    
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ChatMessage> messages;
    
    public ChatRoom(String roomId, String pacilianId, String caregiverId) {
        this.roomId = roomId;
        this.pacilianId = pacilianId;
        this.caregiverId = caregiverId;
        this.messages = new ArrayList<>();
    }
    
    public String getRoomId() {
        return roomId;
    }
    
    public void addMessage(ChatMessage message) {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(message);
    }
} 