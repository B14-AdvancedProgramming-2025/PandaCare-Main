package id.ac.ui.cs.advprog.b14.pandacare.chat.controller;

import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatMessage;
import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatRoom;
import id.ac.ui.cs.advprog.b14.pandacare.chat.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController {
    
    private final ChatService chatService;
    
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    
    @GetMapping("")
    public String getIndex() {
        return "chat/index";
    }
    
    @GetMapping("/room")
    public String getChatRoomPage(
            @RequestParam("pacilianId") String pacilianId,
            @RequestParam("caregiverId") String caregiverId,
            Model model) {
        // Get or create a chat room for these users
        ChatRoom room = chatService.getChatRoomByPacilianAndCaregiver(pacilianId, caregiverId);
        
        // Get all messages for this room
        List<ChatMessage> messages = chatService.getMessagesByRoomId(room.getRoomId());
        
        // Add data to the model
        model.addAttribute("room", room);
        model.addAttribute("messages", messages);
        model.addAttribute("pacilianId", pacilianId);
        model.addAttribute("caregiverId", caregiverId);
        
        return "chat/room";
    }
    
    @PostMapping("/send")
    public String sendMessage(
            @RequestParam(value = "roomId", required = false) String roomId,
            @RequestParam("senderId") String senderId,
            @RequestParam("recipientId") String recipientId,
            @RequestParam("content") String content) {
        // Send the message
        chatService.sendMessage(roomId, senderId, recipientId, content);
        
        // Redirect back to the chat room
        return "redirect:/chat/room?pacilianId=" + senderId + "&caregiverId=" + recipientId;
    }
    
    @GetMapping("/list")
    public String getChatList(
            @RequestParam("userId") String userId,
            @RequestParam("userType") String userType,
            Model model) {
        List<ChatRoom> chatRooms;
        
        // Get chat rooms based on user type
        if ("PACILIAN".equals(userType)) {
            chatRooms = chatService.getChatRoomsByPacilianId(userId);
        } else if ("CAREGIVER".equals(userType)) {
            chatRooms = chatService.getChatRoomsByCaregiverId(userId);
        } else {
            chatRooms = List.of(); // Empty list for unknown user types
        }
        
        // Add data to the model
        model.addAttribute("chatRooms", chatRooms);
        model.addAttribute("userId", userId);
        model.addAttribute("userType", userType);
        
        return "chat/list";
    }
} 