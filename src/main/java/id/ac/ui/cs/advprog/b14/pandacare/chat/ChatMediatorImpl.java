package id.ac.ui.cs.advprog.b14.pandacare.chat;

import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatMessage;
import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatRoom;
import id.ac.ui.cs.advprog.b14.pandacare.chat.storage.ChatMessageRepository;
import id.ac.ui.cs.advprog.b14.pandacare.chat.storage.ChatRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class ChatMediatorImpl implements ChatMediator {
    private final ChatMessageRepository messageRepository;
    private final ChatRoomRepository roomRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatMediatorImpl(ChatMessageRepository messageRepository, ChatRoomRepository roomRepository, SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.roomRepository = roomRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void sendMessage(String roomId, String senderId, String recipientId, String content) {
        ChatRoom room;
        if (roomId != null) {
            room = roomRepository.findById(roomId).orElse(null);
        } else {
            room = roomRepository.findByPacilianIdAndCaregiverId(senderId, recipientId);
            if (room == null) {
                room = roomRepository.findByPacilianIdAndCaregiverId(recipientId, senderId);
            }
            if (room == null) {
                String newRoomId = UUID.randomUUID().toString();
                room = new ChatRoom(newRoomId, senderId, recipientId);
                roomRepository.save(room);
            }
        }
        ChatMessage message = new ChatMessage(senderId, recipientId, content, LocalDateTime.now(), room);
        messageRepository.save(message);
        messagingTemplate.convertAndSend("/queue/messages/" + recipientId, message);
    }
} 