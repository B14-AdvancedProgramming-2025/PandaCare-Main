package id.ac.ui.cs.advprog.b14.pandacare.chat.storage;

import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ChatMessageRepositoryTest {
    
    private ChatMessageRepository repository;
    private ChatMessage message1;
    private ChatMessage message2;
    private String roomId1;
    private String roomId2;
    
    @BeforeEach
    void setUp() {
        repository = new ChatMessageRepository();
        roomId1 = "room123";
        roomId2 = "room456";
        
        String sender1 = "sender1";
        String recipient1 = "recipient1";
        message1 = new ChatMessage(sender1, recipient1, "Hello", LocalDateTime.now());
        
        String sender2 = "sender2";
        String recipient2 = "recipient2";
        message2 = new ChatMessage(sender2, recipient2, "Hi there", LocalDateTime.now().plusMinutes(1));
    }
    
    @Test
    void testSaveMessage() {
        repository.save(roomId1, message1);
        
        List<ChatMessage> messages = repository.findByRoomId(roomId1);
        assertEquals(1, messages.size());
        assertEquals(message1, messages.get(0));
    }
    
    @Test
    void testSaveMultipleMessagesInSameRoom() {
        repository.save(roomId1, message1);
        repository.save(roomId1, message2);
        
        List<ChatMessage> messages = repository.findByRoomId(roomId1);
        assertEquals(2, messages.size());
        assertEquals(message1, messages.get(0));
        assertEquals(message2, messages.get(1));
    }
    
    @Test
    void testSaveMessageInDifferentRooms() {
        repository.save(roomId1, message1);
        repository.save(roomId2, message2);
        
        List<ChatMessage> room1Messages = repository.findByRoomId(roomId1);
        assertEquals(1, room1Messages.size());
        assertEquals(message1, room1Messages.get(0));
        
        List<ChatMessage> room2Messages = repository.findByRoomId(roomId2);
        assertEquals(1, room2Messages.size());
        assertEquals(message2, room2Messages.get(0));
    }
    
    @Test
    void testFindByRoomIdNonExistent() {
        List<ChatMessage> messages = repository.findByRoomId("nonExistentRoom");
        assertNotNull(messages);
        assertTrue(messages.isEmpty());
    }
} 