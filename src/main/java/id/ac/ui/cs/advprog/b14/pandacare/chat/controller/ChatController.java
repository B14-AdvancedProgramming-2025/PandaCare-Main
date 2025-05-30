package id.ac.ui.cs.advprog.b14.pandacare.chat.controller;

import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatMessage;
import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatRoom;
import id.ac.ui.cs.advprog.b14.pandacare.chat.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    private final ChatService chatService;
    
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    
    @GetMapping("/room/{pacilianId}/{caregiverId}")
    public ResponseEntity<Map<String, Object>> getChatRoom(
            @PathVariable String pacilianId,
            @PathVariable String caregiverId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            ChatRoom room = chatService.getChatRoomByPacilianAndCaregiver(pacilianId, caregiverId);
            response.put("success", true);
            response.put("roomId", room.getRoomId());
            response.put("pacilianId", room.getPacilianId());
            response.put("caregiverId", room.getCaregiverId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to get chat room: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/room/{roomId}")
    public ResponseEntity<Map<String, Object>> getChatRoomById(@PathVariable String roomId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<ChatRoom> pacilianRooms = chatService.getChatRoomsByPacilianId(roomId);
            List<ChatRoom> caregiverRooms = chatService.getChatRoomsByCaregiverId(roomId);
            
            ChatRoom room = null;
            if (!pacilianRooms.isEmpty()) {
                room = pacilianRooms.stream()
                    .filter(r -> r.getRoomId().equals(roomId))
                    .findFirst()
                    .orElse(null);
            }
            
            if (room == null && !caregiverRooms.isEmpty()) {
                room = caregiverRooms.stream()
                    .filter(r -> r.getRoomId().equals(roomId))
                    .findFirst()
                    .orElse(null);
            }
            
            if (room != null) {
                response.put("success", true);
                response.put("roomId", room.getRoomId());
                response.put("pacilianId", room.getPacilianId());
                response.put("caregiverId", room.getCaregiverId());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Chat room not found");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to get chat room: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/messages/{roomId}")
    public ResponseEntity<Map<String, Object>> getMessageHistory(@PathVariable String roomId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<ChatMessage> messages = chatService.getMessagesByRoomId(roomId);
            response.put("success", true);
            response.put("messages", messages);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to get messages: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/rooms/pacilian/{pacilianId}")
    public ResponseEntity<Map<String, Object>> getPacilianChatRooms(@PathVariable String pacilianId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<ChatRoom> rooms = chatService.getChatRoomsByPacilianId(pacilianId);
            response.put("success", true);
            response.put("rooms", rooms);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to get chat rooms: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/rooms/caregiver/{caregiverId}")
    public ResponseEntity<Map<String, Object>> getCaregiverChatRooms(@PathVariable String caregiverId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<ChatRoom> rooms = chatService.getChatRoomsByCaregiverId(caregiverId);
            response.put("success", true);
            response.put("rooms", rooms);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to get chat rooms: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 