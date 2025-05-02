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
        rooms.put(room.getRoomId(), room);
        return room;
    }
    
    public ChatRoom findById(String roomId) {
        return rooms.get(roomId);
    }
    
    public List<ChatRoom> findByPacilianId(String pacilianId) {
        return rooms.values().stream()
                .filter(room -> room.getPacilianId().equals(pacilianId))
                .collect(Collectors.toList());
    }
    
    public List<ChatRoom> findByCaregiverId(String caregiverId) {
        return rooms.values().stream()
                .filter(room -> room.getCaregiverId().equals(caregiverId))
                .collect(Collectors.toList());
    }
    
    public ChatRoom findByPacilianIdAndCaregiverId(String pacilianId, String caregiverId) {
        return rooms.values().stream()
                .filter(room -> room.getPacilianId().equals(pacilianId) && room.getCaregiverId().equals(caregiverId))
                .findFirst()
                .orElse(null);
    }
    
    public List<ChatRoom> findAll() {
        return new ArrayList<>(rooms.values());
    }
} 