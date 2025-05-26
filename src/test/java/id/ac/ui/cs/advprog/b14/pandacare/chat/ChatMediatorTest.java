package id.ac.ui.cs.advprog.b14.pandacare.chat;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import id.ac.ui.cs.advprog.b14.pandacare.chat.storage.ChatMessageRepository;
import id.ac.ui.cs.advprog.b14.pandacare.chat.storage.ChatRoomRepository;

class ChatMediatorTest {
    
    private ChatMediator chatMediator;
    
    @Mock
    private ChatMessageRepository messageRepository;

    @Mock
    private ChatRoomRepository roomRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chatMediator = new ChatMediatorImpl(messageRepository, roomRepository, messagingTemplate);
    }
    
    @Test
    void testChatMediatorCreation() {
        assertNotNull(chatMediator);
    }
} 