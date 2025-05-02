package id.ac.ui.cs.advprog.b14.pandacare.chat.storage;

import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatMessage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ChatMessageRepository {
    private final Map<String, List<ChatMessage>> messagesByRoomId = new HashMap<>();
    
    public void save(String roomId, ChatMessage message) {
        // TODO: Implement saving a message to a room
    }
    
    public List<ChatMessage> findByRoomId(String roomId) {
        // TODO: Implement finding messages by room ID
        return new ArrayList<>();
    }
} 