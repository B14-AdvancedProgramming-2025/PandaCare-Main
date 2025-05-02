package id.ac.ui.cs.advprog.b14.pandacare.chat.controller;

import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatMessage;
import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatRoom;
import id.ac.ui.cs.advprog.b14.pandacare.chat.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ChatControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ChatService chatService;

    private ChatController chatController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chatController = new ChatController(chatService);
        mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();
    }

    @Test
    void testGetChatRoomPage() throws Exception {
        String roomId = "room123";
        String pacilianId = "pacilian456";
        String caregiverId = "caregiver789";
        
        ChatRoom mockRoom = new ChatRoom(roomId, pacilianId, caregiverId);
        List<ChatMessage> mockMessages = Arrays.asList(
            new ChatMessage(pacilianId, caregiverId, "Hello", LocalDateTime.now()),
            new ChatMessage(caregiverId, pacilianId, "Hi there", LocalDateTime.now().plusMinutes(1))
        );
        
        when(chatService.getChatRoomByPacilianAndCaregiver(pacilianId, caregiverId)).thenReturn(mockRoom);
        when(chatService.getMessagesByRoomId(roomId)).thenReturn(mockMessages);
        
        mockMvc.perform(get("/chat/room")
                .param("pacilianId", pacilianId)
                .param("caregiverId", caregiverId))
                .andExpect(status().isOk())
                .andExpect(view().name("chat/room"))
                .andExpect(model().attributeExists("room"))
                .andExpect(model().attributeExists("messages"))
                .andExpect(model().attributeExists("pacilianId"))
                .andExpect(model().attributeExists("caregiverId"));
    }

    @Test
    void testSendMessage() throws Exception {
        String roomId = "room123";
        String pacilianId = "pacilian456";
        String caregiverId = "caregiver789";
        String content = "Test message";
        
        mockMvc.perform(post("/chat/send")
                .param("roomId", roomId)
                .param("senderId", pacilianId)
                .param("recipientId", caregiverId)
                .param("content", content)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/chat/room?pacilianId=" + pacilianId + "&caregiverId=" + caregiverId));
    }

    @Test
    void testGetChatList() throws Exception {
        String userId = "user123";
        String userType = "PACILIAN";
        
        List<ChatRoom> mockRooms = Arrays.asList(
            new ChatRoom("room1", userId, "caregiver1"),
            new ChatRoom("room2", userId, "caregiver2")
        );
        
        when(chatService.getChatRoomsByPacilianId(userId)).thenReturn(mockRooms);
        
        mockMvc.perform(get("/chat/list")
                .param("userId", userId)
                .param("userType", userType))
                .andExpect(status().isOk())
                .andExpect(view().name("chat/list"))
                .andExpect(model().attributeExists("chatRooms"))
                .andExpect(model().attributeExists("userId"))
                .andExpect(model().attributeExists("userType"));
    }

    @Test
    void testGetChatListForCaregiver() throws Exception {
        String userId = "caregiver123";
        String userType = "CAREGIVER";
        
        List<ChatRoom> mockRooms = Arrays.asList(
            new ChatRoom("room1", "pacilian1", userId),
            new ChatRoom("room2", "pacilian2", userId)
        );
        
        when(chatService.getChatRoomsByCaregiverId(userId)).thenReturn(mockRooms);
        
        mockMvc.perform(get("/chat/list")
                .param("userId", userId)
                .param("userType", userType))
                .andExpect(status().isOk())
                .andExpect(view().name("chat/list"))
                .andExpect(model().attributeExists("chatRooms"))
                .andExpect(model().attributeExists("userId"))
                .andExpect(model().attributeExists("userType"));
    }
} 