package id.ac.ui.cs.advprog.b14.pandacare.chat.controller;

import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatMessage;
import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatRoom;
import id.ac.ui.cs.advprog.b14.pandacare.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
public class WebSocketController {
    
    private final ChatService chatService;
    
    public WebSocketController(ChatService chatService) {
        this.chatService = chatService;
    }
    
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessage sendMessage(
            @DestinationVariable String roomId, 
            Map<String, String> messageMap,
            SimpMessageHeaderAccessor headerAccessor) {
        
        String sender = messageMap.get("sender");
        String recipient = messageMap.get("recipient");
        String content = messageMap.get("content");
        
        // Extract user details from session if available
        String userId = headerAccessor.getSessionAttributes() != null ? 
                (String) headerAccessor.getSessionAttributes().get("userId") : null;
        
        // Update sender if authenticated
        if (userId != null && !userId.isEmpty()) {
            sender = userId;
        }
        
        // Save the message through service
        chatService.sendMessage(roomId, sender, recipient, content);
        
        // Create and return a message
        return new ChatMessage(sender, recipient, content, LocalDateTime.now());
    }
    
    @SubscribeMapping("/chat/{roomId}")
    public List<ChatMessage> subscribeToRoom(@DestinationVariable String roomId) {
        // When a user subscribes to a room, send them all previous messages
        return chatService.getMessagesByRoomId(roomId);
    }
    
    @MessageMapping("/chat/create/{pacilianId}/{caregiverId}")
    @SendTo("/topic/rooms")
    public ChatRoom createNewChatRoom(
            @DestinationVariable String pacilianId,
            @DestinationVariable String caregiverId) {
        // Get or create a chat room
        return chatService.getChatRoomByPacilianAndCaregiver(pacilianId, caregiverId);
    }
    
    @SubscribeMapping("/user/{userId}")
    public void subscribeToUserQueue(@DestinationVariable String userId, SimpMessageHeaderAccessor headerAccessor) {
        // Store user ID in session for tracking online status
        if (headerAccessor.getSessionAttributes() != null) {
            headerAccessor.getSessionAttributes().put("userId", userId);
        }
    }
} 