package id.ac.ui.cs.advprog.b14.pandacare.chat.storage;

import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatRoom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ChatRoomRepositoryTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Test
    void testSaveAndFindById() {
        ChatRoom room = new ChatRoom("room1", "p1", "c1");
        chatRoomRepository.save(room);
        Optional<ChatRoom> found = chatRoomRepository.findById("room1");
        assertThat(found).isPresent();
        assertThat(found.get().getRoomId()).isEqualTo("room1");
    }

    @Test
    void testFindByIdEmpty() {
        Optional<ChatRoom> found = chatRoomRepository.findById("nonexistent");
        assertThat(found).isNotPresent();
    }

    @Test
    void testFindByPacilianId() {
        ChatRoom r1 = new ChatRoom("room1", "p1", "c1");
        ChatRoom r2 = new ChatRoom("room2", "p1", "c2");
        chatRoomRepository.save(r1);
        chatRoomRepository.save(r2);

        List<ChatRoom> found = chatRoomRepository.findByPacilianId("p1");
        assertThat(found).hasSize(2);
        assertThat(found).extracting(ChatRoom::getRoomId)
                        .containsExactlyInAnyOrder("room1", "room2");
    }

    @Test
    void testFindByCaregiverId() {
        ChatRoom r1 = new ChatRoom("room1", "p1", "c1");
        ChatRoom r2 = new ChatRoom("room2", "p2", "c1");
        chatRoomRepository.save(r1);
        chatRoomRepository.save(r2);

        List<ChatRoom> found = chatRoomRepository.findByCaregiverId("c1");
        assertThat(found).hasSize(2);
        assertThat(found).extracting(ChatRoom::getRoomId)
                        .containsExactlyInAnyOrder("room1", "room2");
    }

    @Test
    void testFindByPacilianAndCaregiver() {
        ChatRoom r1 = new ChatRoom("room1", "p1", "c1");
        ChatRoom r2 = new ChatRoom("room2", "p1", "c2");
        chatRoomRepository.save(r1);
        chatRoomRepository.save(r2);

        ChatRoom found = chatRoomRepository.findByPacilianIdAndCaregiverId("p1", "c1");
        assertThat(found.getRoomId()).isEqualTo("room1");
    }
} 