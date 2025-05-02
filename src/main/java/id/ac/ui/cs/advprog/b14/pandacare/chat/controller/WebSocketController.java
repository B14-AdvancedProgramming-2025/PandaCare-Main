package id.ac.ui.cs.advprog.b14.pandacare.chat.controller;

import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatMessage;
import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatRoom;
import id.ac.ui.cs.advprog.b14.pandacare.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class WebSocketController {
    
    private final ChatService chatService;
    
    public WebSocketController(ChatService chatService) {
        this.chatService = chatService;
    }
    
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/messages/{roomId}")
    public ChatMessage sendMessage(@DestinationVariable String roomId, ChatMessage message) {
        // TODO: Implement sending a message through WebSocket
        return null;
    }
    
    @MessageMapping("/chat/create/{pacilianId}/{caregiverId}")
    @SendTo("/topic/rooms")
    public ChatRoom createNewChatRoom(
            @DestinationVariable String pacilianId,
            @DestinationVariable String caregiverId) {
        // TODO: Implement creating a new chat room through WebSocket
        return null;
    }
} 