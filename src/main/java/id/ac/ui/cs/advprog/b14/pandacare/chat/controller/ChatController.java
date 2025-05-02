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
    
    @GetMapping("/room")
    public String getChatRoomPage(
            @RequestParam("pacilianId") String pacilianId,
            @RequestParam("caregiverId") String caregiverId,
            Model model) {
        // TODO: Implement getting a chat room page
        return "chat/room";
    }
    
    @PostMapping("/send")
    public String sendMessage(
            @RequestParam(value = "roomId", required = false) String roomId,
            @RequestParam("senderId") String senderId,
            @RequestParam("recipientId") String recipientId,
            @RequestParam("content") String content) {
        // TODO: Implement sending a message
        return "redirect:/chat/room";
    }
    
    @GetMapping("/list")
    public String getChatList(
            @RequestParam("userId") String userId,
            @RequestParam("userType") String userType,
            Model model) {
        // TODO: Implement getting a list of chat rooms
        return "chat/list";
    }
} 