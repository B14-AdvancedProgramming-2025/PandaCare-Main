package id.ac.ui.cs.advprog.b14.pandacare.chat.storage;

import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatRoom;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ChatRoomRepository {
    private final Map<String, ChatRoom> rooms = new HashMap<>();
    
    public ChatRoom save(ChatRoom room) {
        // TODO: Implement saving a chat room
        return room;
    }
    
    public ChatRoom findById(String roomId) {
        // TODO: Implement finding a room by ID
        return null;
    }
    
    public List<ChatRoom> findByPacilianId(String pacilianId) {
        // TODO: Implement finding rooms by pacilian ID
        return new ArrayList<>();
    }
    
    public List<ChatRoom> findByCaregiverId(String caregiverId) {
        // TODO: Implement finding rooms by caregiver ID
        return new ArrayList<>();
    }
    
    public ChatRoom findByPacilianIdAndCaregiverId(String pacilianId, String caregiverId) {
        // TODO: Implement finding a room by pacilian and caregiver IDs
        return null;
    }
    
    public List<ChatRoom> findAll() {
        // TODO: Implement finding all rooms
        return new ArrayList<>();
    }
} 