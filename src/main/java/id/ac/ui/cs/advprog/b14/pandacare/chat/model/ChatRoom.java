package id.ac.ui.cs.advprog.b14.pandacare.chat.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoom {
    private String roomId;
    private String pacilianId;
    private String caregiverId;
    private List<ChatMessage> messages;
    
    public ChatRoom(String roomId, String pacilianId, String caregiverId) {
        this.roomId = roomId;
        this.pacilianId = pacilianId;
        this.caregiverId = caregiverId;
        this.messages = new ArrayList<>();
    }
    
    public void addMessage(ChatMessage message) {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(message);
    }
} 