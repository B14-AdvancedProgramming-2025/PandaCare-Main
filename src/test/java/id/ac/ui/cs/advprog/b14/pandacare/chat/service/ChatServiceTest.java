package id.ac.ui.cs.advprog.b14.pandacare.chat.service;

import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatMessage;
import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatRoom;
import id.ac.ui.cs.advprog.b14.pandacare.chat.storage.ChatMessageRepository;
import id.ac.ui.cs.advprog.b14.pandacare.chat.storage.ChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ChatServiceTest {

    private ChatServiceImpl chatService;

    @Mock
    private ChatMessageRepository messageRepository;

    @Mock
    private ChatRoomRepository roomRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    private String roomId;
    private String senderId;
    private String recipientId;
    private ChatRoom testRoom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chatService = new ChatServiceImpl(messageRepository, roomRepository, messagingTemplate);
        roomId = "room123";
        senderId = "sender456";
        recipientId = "recipient789";
        testRoom = new ChatRoom(roomId, senderId, recipientId);
    }

    @Test
    void testSendMessageWithExistingRoom() {
        String content = "Hi there";
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(testRoom));
        chatService.sendMessage(roomId, senderId, recipientId, content);
        verify(messageRepository, times(1)).save(any(ChatMessage.class));
        verify(messagingTemplate, times(1)).convertAndSend(eq("/queue/messages/" + recipientId), any(ChatMessage.class));
    }

    @Test
    void testSendMessageCreatesNewRoom() {
        String content = "Hello world";
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());
        when(roomRepository.findByPacilianIdAndCaregiverId(senderId, recipientId)).thenReturn(null);
        when(roomRepository.findByPacilianIdAndCaregiverId(recipientId, senderId)).thenReturn(null);
        when(roomRepository.save(any(ChatRoom.class))).thenReturn(testRoom);

        chatService.sendMessage(null, senderId, recipientId, content);

        verify(roomRepository, times(1)).save(any(ChatRoom.class));
        verify(messageRepository, times(1)).save(any(ChatMessage.class));
        verify(messagingTemplate, times(1)).convertAndSend(eq("/queue/messages/" + recipientId), any(ChatMessage.class));
    }

    @Test
    void testGetMessagesByRoomId() {
        ChatMessage m1 = new ChatMessage(senderId, recipientId, "Msg1", LocalDateTime.now(), testRoom);
        ChatMessage m2 = new ChatMessage(recipientId, senderId, "Msg2", LocalDateTime.now().plusMinutes(1), testRoom);
        List<ChatMessage> mockList = List.of(m1, m2);
        when(messageRepository.findByChatRoomRoomId(roomId)).thenReturn(mockList);
        List<ChatMessage> result = chatService.getMessagesByRoomId(roomId);
        assertEquals(mockList, result);
        verify(messageRepository, times(1)).findByChatRoomRoomId(roomId);
    }

    @Test
    void testGetChatRoomByPacilianAndCaregiverFound() {
        when(roomRepository.findByPacilianIdAndCaregiverId(senderId, recipientId)).thenReturn(testRoom);
        ChatRoom room = chatService.getChatRoomByPacilianAndCaregiver(senderId, recipientId);
        assertEquals(testRoom, room);
        verify(roomRepository, times(1)).findByPacilianIdAndCaregiverId(senderId, recipientId);
    }

    @Test
    void testGetChatRoomByPacilianAndCaregiverNotFound() {
        when(roomRepository.findByPacilianIdAndCaregiverId(senderId, recipientId)).thenReturn(null);
        when(roomRepository.save(any(ChatRoom.class))).thenReturn(testRoom);
        ChatRoom room = chatService.getChatRoomByPacilianAndCaregiver(senderId, recipientId);
        assertNotNull(room);
        verify(roomRepository, times(1)).save(any(ChatRoom.class));
    }

    @Test
    void testGetChatRoomsByPacilianId() {
        when(roomRepository.findByPacilianId(senderId)).thenReturn(List.of(testRoom));
        var rooms = chatService.getChatRoomsByPacilianId(senderId);
        assertEquals(1, rooms.size());
        assertEquals(testRoom, rooms.get(0));
        verify(roomRepository, times(1)).findByPacilianId(senderId);
    }

    @Test
    void testGetChatRoomsByCaregiverId() {
        when(roomRepository.findByCaregiverId(recipientId)).thenReturn(List.of(testRoom));
        var rooms = chatService.getChatRoomsByCaregiverId(recipientId);
        assertEquals(1, rooms.size());
        assertEquals(testRoom, rooms.get(0));
        verify(roomRepository, times(1)).findByCaregiverId(recipientId);
    }
} 