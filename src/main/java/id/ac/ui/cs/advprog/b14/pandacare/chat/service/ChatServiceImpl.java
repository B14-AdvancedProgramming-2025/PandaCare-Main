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
        ChatRoom room;
        
        if (roomId != null) {
            room = roomRepository.findById(roomId);
        } else {
            room = roomRepository.findByPacilianIdAndCaregiverId(senderId, recipientId);
            if (room == null) {
                // If room doesn't exist, try with reversed roles
                room = roomRepository.findByPacilianIdAndCaregiverId(recipientId, senderId);
            }
            
            if (room == null) {
                // Create a new room if it doesn't exist
                String newRoomId = UUID.randomUUID().toString();
                
                // Determine who is pacilian and who is caregiver based on some logic
                // For now, we'll assume the sender is the pacilian
                room = new ChatRoom(newRoomId, senderId, recipientId);
                roomRepository.save(room);
            }
            
            roomId = room.getRoomId();
        }
        
        ChatMessage message = new ChatMessage(senderId, recipientId, content, LocalDateTime.now());
        messageRepository.save(roomId, message);
    }
    
    @Override
    public List<ChatMessage> getMessagesByRoomId(String roomId) {
        return messageRepository.findByRoomId(roomId);
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