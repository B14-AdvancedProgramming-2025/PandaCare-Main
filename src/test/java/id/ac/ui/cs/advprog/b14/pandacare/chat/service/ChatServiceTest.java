package id.ac.ui.cs.advprog.b14.pandacare.chat.service;

import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatMessage;
import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatRoom;
import id.ac.ui.cs.advprog.b14.pandacare.chat.storage.ChatMessageRepository;
import id.ac.ui.cs.advprog.b14.pandacare.chat.storage.ChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ChatServiceTest {
    
    private ChatService chatService;
    
    @Mock
    private ChatMessageRepository messageRepository;
    
    @Mock
    private ChatRoomRepository roomRepository;
    
    private ChatRoom testRoom;
    private String roomId;
    private String pacilianId;
    private String caregiverId;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chatService = new ChatServiceImpl(messageRepository, roomRepository);
        
        roomId = "room123";
        pacilianId = "pacilian456";
        caregiverId = "caregiver789";
        testRoom = new ChatRoom(roomId, pacilianId, caregiverId);
    }
    
    @Test
    void testSendMessage() {
        String content = "Hello, how are you?";
        when(roomRepository.findById(roomId)).thenReturn(testRoom);
        
        chatService.sendMessage(roomId, pacilianId, caregiverId, content);
        
        verify(messageRepository, times(1)).save(eq(roomId), any(ChatMessage.class));
    }
    
    @Test
    void testSendMessageRoomNotFound() {
        String content = "Hello, how are you?";
        when(roomRepository.findById(roomId)).thenReturn(null);
        when(roomRepository.findByPacilianIdAndCaregiverId(pacilianId, caregiverId)).thenReturn(null);
        when(roomRepository.save(any(ChatRoom.class))).thenReturn(testRoom);
        
        chatService.sendMessage(null, pacilianId, caregiverId, content);
        
        verify(roomRepository, times(1)).save(any(ChatRoom.class));
        verify(messageRepository, times(1)).save(anyString(), any(ChatMessage.class));
    }
    
    @Test
    void testGetMessagesByRoomId() {
        List<ChatMessage> mockMessages = new ArrayList<>();
        mockMessages.add(new ChatMessage(pacilianId, caregiverId, "Hello", LocalDateTime.now()));
        mockMessages.add(new ChatMessage(caregiverId, pacilianId, "Hi there", LocalDateTime.now().plusMinutes(1)));
        
        when(messageRepository.findByRoomId(roomId)).thenReturn(mockMessages);
        
        List<ChatMessage> messages = chatService.getMessagesByRoomId(roomId);
        
        assertEquals(2, messages.size());
        verify(messageRepository, times(1)).findByRoomId(roomId);
    }
    
    @Test
    void testGetChatRoomByPacilianAndCaregiver() {
        when(roomRepository.findByPacilianIdAndCaregiverId(pacilianId, caregiverId)).thenReturn(testRoom);
        
        ChatRoom room = chatService.getChatRoomByPacilianAndCaregiver(pacilianId, caregiverId);
        
        assertEquals(testRoom, room);
        verify(roomRepository, times(1)).findByPacilianIdAndCaregiverId(pacilianId, caregiverId);
    }
    
    @Test
    void testGetChatRoomByPacilianAndCaregiverNotFound() {
        when(roomRepository.findByPacilianIdAndCaregiverId(pacilianId, caregiverId)).thenReturn(null);
        when(roomRepository.save(any(ChatRoom.class))).thenReturn(testRoom);
        
        ChatRoom room = chatService.getChatRoomByPacilianAndCaregiver(pacilianId, caregiverId);
        
        assertNotNull(room);
        verify(roomRepository, times(1)).findByPacilianIdAndCaregiverId(pacilianId, caregiverId);
        verify(roomRepository, times(1)).save(any(ChatRoom.class));
    }
    
    @Test
    void testGetChatRoomsByPacilianId() {
        List<ChatRoom> mockRooms = List.of(testRoom);
        when(roomRepository.findByPacilianId(pacilianId)).thenReturn(mockRooms);
        
        List<ChatRoom> rooms = chatService.getChatRoomsByPacilianId(pacilianId);
        
        assertEquals(1, rooms.size());
        assertEquals(testRoom, rooms.get(0));
        verify(roomRepository, times(1)).findByPacilianId(pacilianId);
    }
    
    @Test
    void testGetChatRoomsByCaregiverId() {
        List<ChatRoom> mockRooms = List.of(testRoom);
        when(roomRepository.findByCaregiverId(caregiverId)).thenReturn(mockRooms);
        
        List<ChatRoom> rooms = chatService.getChatRoomsByCaregiverId(caregiverId);
        
        assertEquals(1, rooms.size());
        assertEquals(testRoom, rooms.get(0));
        verify(roomRepository, times(1)).findByCaregiverId(caregiverId);
    }
} 