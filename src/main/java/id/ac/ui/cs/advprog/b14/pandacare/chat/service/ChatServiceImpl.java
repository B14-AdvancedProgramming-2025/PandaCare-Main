package id.ac.ui.cs.advprog.b14.pandacare.chat.service;

import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatMessage;
import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatRoom;
import id.ac.ui.cs.advprog.b14.pandacare.chat.storage.ChatMessageRepository;
import id.ac.ui.cs.advprog.b14.pandacare.chat.storage.ChatRoomRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import id.ac.ui.cs.advprog.b14.pandacare.chat.ChatMediator;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {
    
    private final ChatMessageRepository messageRepository;
    private final ChatRoomRepository roomRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMediator chatMediator;
    
    public ChatServiceImpl(
            ChatMessageRepository messageRepository, 
            ChatRoomRepository roomRepository, 
            SimpMessagingTemplate messagingTemplate,
            ChatMediator chatMediator) {
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
        this.messagingTemplate = messagingTemplate;
        this.chatMediator = chatMediator;
    }
    
    @Override
    public void sendMessage(String roomId, String senderId, String recipientId, String content) {
        chatMediator.sendMessage(roomId, senderId, recipientId, content);
    }
    
    @Override
    public List<ChatMessage> getMessagesByRoomId(String roomId) {
        return messageRepository.findByChatRoomRoomId(roomId);
    }
    
    @Override
    public ChatRoom getChatRoomByPacilianAndCaregiver(String pacilianId, String caregiverId) {
        ChatRoom room = roomRepository.findByPacilianIdAndCaregiverId(pacilianId, caregiverId);
        
        if (room == null) {
            // Create a new room if it doesn't exist
            String roomId = UUID.randomUUID().toString();
            room = new ChatRoom(roomId, pacilianId, caregiverId);
            roomRepository.save(room);
        }
        
        return room;
    }
    
    @Override
    public List<ChatRoom> getChatRoomsByPacilianId(String pacilianId) {
        return roomRepository.findByPacilianId(pacilianId);
    }
    
    @Override
    public List<ChatRoom> getChatRoomsByCaregiverId(String caregiverId) {
        return roomRepository.findByCaregiverId(caregiverId);
    }
} 