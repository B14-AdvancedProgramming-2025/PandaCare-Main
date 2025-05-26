package id.ac.ui.cs.advprog.b14.pandacare.chat.storage;

import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatMessage;
import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatRoom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ChatMessageRepositoryTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Test
    void testSaveAndFindByChatRoomRoomId() {
        ChatRoom room = new ChatRoom("room1", "pacilian1", "caregiver1");
        ChatRoom savedRoom = chatRoomRepository.save(room);

        ChatMessage m1 = new ChatMessage("pacilian1", "caregiver1", "Hello", LocalDateTime.now(), savedRoom);
        ChatMessage m2 = new ChatMessage("caregiver1", "pacilian1", "Hi there", LocalDateTime.now().plusMinutes(1), savedRoom);
        chatMessageRepository.save(m1);
        chatMessageRepository.save(m2);

        List<ChatMessage> found = chatMessageRepository.findByChatRoomRoomId("room1");
        assertThat(found).hasSize(2);
        assertThat(found).extracting(ChatMessage::getContent)
                        .containsExactly("Hello", "Hi there");
    }

    @Test
    void testFindByChatRoomRoomIdEmpty() {
        List<ChatMessage> found = chatMessageRepository.findByChatRoomRoomId("nonexistent");
        assertThat(found).isEmpty();
    }
} 