package id.ac.ui.cs.advprog.b14.pandacare.chat.service;

import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatMessage;
import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatRoom;
import id.ac.ui.cs.advprog.b14.pandacare.chat.storage.ChatMessageRepository;
import id.ac.ui.cs.advprog.b14.pandacare.chat.storage.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ChatServiceImpl implements ChatService {
    
    private final ChatMessageRepository messageRepository;
    private final ChatRoomRepository roomRepository;
    
    public ChatServiceImpl(ChatMessageRepository messageRepository, ChatRoomRepository roomRepository) {
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
    }
    
    @Override
    public void sendMessage(String roomId, String senderId, String recipientId, String content) {
        // TODO: Implement sending a message
    }
    
    @Override
    public List<ChatMessage> getMessagesByRoomId(String roomId) {
        // TODO: Implement getting messages by room ID
        return null;
    }
    
    @Override
    public ChatRoom getChatRoomByPacilianAndCaregiver(String pacilianId, String caregiverId) {
        // TODO: Implement getting a chat room by pacilian and caregiver IDs
        return null;
    }
    
    @Override
    public List<ChatRoom> getChatRoomsByPacilianId(String pacilianId) {
        // TODO: Implement getting chat rooms by pacilian ID
        return null;
    }
    
    @Override
    public List<ChatRoom> getChatRoomsByCaregiverId(String caregiverId) {
        // TODO: Implement getting chat rooms by caregiver ID
        return null;
    }
} 