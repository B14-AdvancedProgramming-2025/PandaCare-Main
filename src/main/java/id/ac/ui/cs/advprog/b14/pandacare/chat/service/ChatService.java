package id.ac.ui.cs.advprog.b14.pandacare.chat.service;

import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatMessage;
import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatRoom;

import java.util.List;

public interface ChatService {
    
    void sendMessage(String roomId, String senderId, String recipientId, String content);
    
    List<ChatMessage> getMessagesByRoomId(String roomId);
    
    ChatRoom getChatRoomByPacilianAndCaregiver(String pacilianId, String caregiverId);
    
    List<ChatRoom> getChatRoomsByPacilianId(String pacilianId);
    
    List<ChatRoom> getChatRoomsByCaregiverId(String caregiverId);
    
} 