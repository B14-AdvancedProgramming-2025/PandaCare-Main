package id.ac.ui.cs.advprog.b14.pandacare.chat.controller;

import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatMessage;
import id.ac.ui.cs.advprog.b14.pandacare.chat.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class WebSocketControllerTest {

    private WebSocketController webSocketController;

    @Mock
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webSocketController = new WebSocketController(chatService);
    }

    @Test
    void testSendMessage() {
        String roomId = "room123";
        String senderId = "sender456";
        String recipientId = "recipient789";
        String content = "Hello from WebSocket";
        
        ChatMessage inputMessage = new ChatMessage();
        inputMessage.setSender(senderId);
        inputMessage.setRecipient(recipientId);
        inputMessage.setContent(content);
        
        doNothing().when(chatService).sendMessage(anyString(), anyString(), anyString(), anyString());
        
        ChatMessage outputMessage = webSocketController.sendMessage(roomId, inputMessage);
        
        assertNotNull(outputMessage);
        assertEquals(senderId, outputMessage.getSender());
        assertEquals(recipientId, outputMessage.getRecipient());
        assertEquals(content, outputMessage.getContent());
        assertNotNull(outputMessage.getTimestamp());
        
        verify(chatService, times(1)).sendMessage(eq(roomId), eq(senderId), eq(recipientId), eq(content));
    }

    @Test
    void testCreateNewChatRoom() {
        String pacilianId = "pacilian123";
        String caregiverId = "caregiver456";
        
        webSocketController.createNewChatRoom(pacilianId, caregiverId);
        
        verify(chatService, times(1)).getChatRoomByPacilianAndCaregiver(eq(pacilianId), eq(caregiverId));
    }
} 