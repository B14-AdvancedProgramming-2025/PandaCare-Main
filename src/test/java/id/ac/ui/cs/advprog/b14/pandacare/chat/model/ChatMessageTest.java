package id.ac.ui.cs.advprog.b14.pandacare.chat.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ChatMessageTest {
    
    private ChatMessage chatMessage;
    private String sender;
    private String recipient;
    private String content;
    private LocalDateTime timestamp;
    
    @BeforeEach
    void setUp() {
        sender = "sender123";
        recipient = "recipient456";
        content = "Hello, how are you?";
        timestamp = LocalDateTime.now();
        chatMessage = new ChatMessage(sender, recipient, content, timestamp);
    }
    
    @Test
    void testChatMessageCreation() {
        assertNotNull(chatMessage);
        assertEquals(sender, chatMessage.getSender());
        assertEquals(recipient, chatMessage.getRecipient());
        assertEquals(content, chatMessage.getContent());
        assertEquals(timestamp, chatMessage.getTimestamp());
    }
    
    @Test
    void testChatMessageSetters() {
        String newSender = "newSender789";
        String newRecipient = "newRecipient012";
        String newContent = "New message content";
        LocalDateTime newTimestamp = LocalDateTime.now().plusHours(1);
        
        chatMessage.setSender(newSender);
        chatMessage.setRecipient(newRecipient);
        chatMessage.setContent(newContent);
        chatMessage.setTimestamp(newTimestamp);
        
        assertEquals(newSender, chatMessage.getSender());
        assertEquals(newRecipient, chatMessage.getRecipient());
        assertEquals(newContent, chatMessage.getContent());
        assertEquals(newTimestamp, chatMessage.getTimestamp());
    }
} 