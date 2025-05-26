package id.ac.ui.cs.advprog.b14.pandacare.chat.controller;

import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatMessage;
import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatRoom;
import id.ac.ui.cs.advprog.b14.pandacare.chat.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class WebSocketControllerTest {

    @Mock
    private ChatService chatService;

    private WebSocketController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new WebSocketController(chatService);
    }

    @Test
    void testSendMessage() {
        String roomId = "room1";
        String sender = "sender1";
        String recipient = "recipient1";
        String content = "Hello";
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("sender", sender);
        messageMap.put("recipient", recipient);
        messageMap.put("content", content);
        SimpMessageHeaderAccessor headerAccessor = mock(SimpMessageHeaderAccessor.class);
        when(headerAccessor.getSessionAttributes()).thenReturn(null);
        ChatMessage response = controller.sendMessage(roomId, messageMap, headerAccessor);
        assertNotNull(response);
        assertEquals(sender, response.getSender());
        assertEquals(recipient, response.getRecipient());
        assertEquals(content, response.getContent());
        assertNotNull(response.getTimestamp());
        verify(chatService, times(1)).sendMessage(eq(roomId), eq(sender), eq(recipient), eq(content));
    }

    @Test
    void testSubscribeToRoom() {
        String roomId = "room2";
        ChatMessage m1 = new ChatMessage("a", "b", "m", LocalDateTime.now());
        List<ChatMessage> messages = Collections.singletonList(m1);
        when(chatService.getMessagesByRoomId(roomId)).thenReturn(messages);
        List<ChatMessage> result = controller.subscribeToRoom(roomId);
        assertEquals(messages, result);
        verify(chatService, times(1)).getMessagesByRoomId(roomId);
    }

    @Test
    void testCreateNewChatRoom() {
        String pacilianId = "p1";
        String caregiverId = "c1";
        ChatRoom room = new ChatRoom("r1", pacilianId, caregiverId);
        when(chatService.getChatRoomByPacilianAndCaregiver(pacilianId, caregiverId)).thenReturn(room);
        ChatRoom result = controller.createNewChatRoom(pacilianId, caregiverId);
        assertEquals(room, result);
        verify(chatService, times(1)).getChatRoomByPacilianAndCaregiver(pacilianId, caregiverId);
    }

    @Test
    void testSubscribeToUserQueue() {
        String userId = "user1";
        Map<String, Object> sessionAttrs = new HashMap<>();
        SimpMessageHeaderAccessor headerAccessor = mock(SimpMessageHeaderAccessor.class);
        when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttrs);
        controller.subscribeToUserQueue(userId, headerAccessor);
        assertTrue(sessionAttrs.containsKey("userId"));
        assertEquals(userId, sessionAttrs.get("userId"));
    }
} 