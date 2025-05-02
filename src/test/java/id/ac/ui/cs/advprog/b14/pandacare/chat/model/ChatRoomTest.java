package id.ac.ui.cs.advprog.b14.pandacare.chat.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ChatRoomTest {
    
    private ChatRoom chatRoom;
    private String roomId;
    private String pacilianId;
    private String caregiverId;
    
    @BeforeEach
    void setUp() {
        roomId = "room123";
        pacilianId = "pacilian456";
        caregiverId = "caregiver789";
        chatRoom = new ChatRoom(roomId, pacilianId, caregiverId);
    }
    
    @Test
    void testChatRoomCreation() {
        assertNotNull(chatRoom);
        assertEquals(roomId, chatRoom.getRoomId());
        assertEquals(pacilianId, chatRoom.getPacilianId());
        assertEquals(caregiverId, chatRoom.getCaregiverId());
        assertNotNull(chatRoom.getMessages());
        assertTrue(chatRoom.getMessages().isEmpty());
    }
    
    @Test
    void testAddMessage() {
        ChatMessage message = new ChatMessage(pacilianId, caregiverId, "Hello", LocalDateTime.now());
        chatRoom.addMessage(message);
        
        assertEquals(1, chatRoom.getMessages().size());
        assertEquals(message, chatRoom.getMessages().get(0));
    }
    
    @Test
    void testAddMultipleMessages() {
        ChatMessage message1 = new ChatMessage(pacilianId, caregiverId, "Hello", LocalDateTime.now());
        ChatMessage message2 = new ChatMessage(caregiverId, pacilianId, "Hi there", LocalDateTime.now().plusMinutes(1));
        
        chatRoom.addMessage(message1);
        chatRoom.addMessage(message2);
        
        assertEquals(2, chatRoom.getMessages().size());
        assertEquals(message1, chatRoom.getMessages().get(0));
        assertEquals(message2, chatRoom.getMessages().get(1));
    }
    
    @Test
    void testSetters() {
        String newPacilianId = "newPacilian123";
        String newCaregiverId = "newCaregiver456";
        ChatMessage message = new ChatMessage(pacilianId, caregiverId, "Test", LocalDateTime.now());
        List<ChatMessage> newMessages = List.of(message);
        
        chatRoom.setPacilianId(newPacilianId);
        chatRoom.setCaregiverId(newCaregiverId);
        chatRoom.setMessages(newMessages);
        
        assertEquals(newPacilianId, chatRoom.getPacilianId());
        assertEquals(newCaregiverId, chatRoom.getCaregiverId());
        assertEquals(newMessages, chatRoom.getMessages());
        assertEquals(1, chatRoom.getMessages().size());
    }
} 