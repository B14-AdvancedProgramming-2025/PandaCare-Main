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
        List<ChatMessage> messages = messagesByRoomId.computeIfAbsent(roomId, k -> new ArrayList<>());
        messages.add(message);
    }
    
    public List<ChatMessage> findByRoomId(String roomId) {
        return messagesByRoomId.getOrDefault(roomId, new ArrayList<>());
    }
} 