package id.ac.ui.cs.advprog.b14.pandacare.chat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChatMediatorTest {
    
    private ChatMediator chatMediator;
    
    @BeforeEach
    void setUp() {
        chatMediator = new ChatMediatorImpl();
    }
    
    @Test
    void testChatMediatorCreation() {
        assertNotNull(chatMediator);
    }
} 