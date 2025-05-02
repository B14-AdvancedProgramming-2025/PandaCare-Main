package id.ac.ui.cs.advprog.b14.pandacare.chat.storage;

import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ChatRoomRepositoryTest {
    
    private ChatRoomRepository repository;
    private ChatRoom room1;
    private ChatRoom room2;
    private String roomId1;
    private String roomId2;
    private String pacilianId;
    private String caregiver1Id;
    private String caregiver2Id;
    
    @BeforeEach
    void setUp() {
        repository = new ChatRoomRepository();
        roomId1 = "room123";
        roomId2 = "room456";
        pacilianId = "pacilian789";
        caregiver1Id = "caregiver111";
        caregiver2Id = "caregiver222";
        
        room1 = new ChatRoom(roomId1, pacilianId, caregiver1Id);
        room2 = new ChatRoom(roomId2, pacilianId, caregiver2Id);
    }
    
    @Test
    void testSaveRoom() {
        repository.save(room1);
        
        ChatRoom foundRoom = repository.findById(roomId1);
        assertEquals(room1, foundRoom);
    }
    
    @Test
    void testFindByIdNonExistent() {
        ChatRoom foundRoom = repository.findById("nonExistentRoom");
        assertNull(foundRoom);
    }
    
    @Test
    void testFindByPacilianId() {
        repository.save(room1);
        repository.save(room2);
        
        List<ChatRoom> rooms = repository.findByPacilianId(pacilianId);
        assertEquals(2, rooms.size());
        assertTrue(rooms.contains(room1));
        assertTrue(rooms.contains(room2));
    }
    
    @Test
    void testFindByCaregiverId() {
        repository.save(room1);
        repository.save(room2);
        
        List<ChatRoom> rooms1 = repository.findByCaregiverId(caregiver1Id);
        assertEquals(1, rooms1.size());
        assertEquals(room1, rooms1.get(0));
        
        List<ChatRoom> rooms2 = repository.findByCaregiverId(caregiver2Id);
        assertEquals(1, rooms2.size());
        assertEquals(room2, rooms2.get(0));
    }
    
    @Test
    void testFindAll() {
        repository.save(room1);
        repository.save(room2);
        
        List<ChatRoom> allRooms = repository.findAll();
        assertEquals(2, allRooms.size());
        assertTrue(allRooms.contains(room1));
        assertTrue(allRooms.contains(room2));
    }
    
    @Test
    void testFindByPacilianIdAndCaregiverId() {
        repository.save(room1);
        repository.save(room2);
        
        ChatRoom foundRoom = repository.findByPacilianIdAndCaregiverId(pacilianId, caregiver1Id);
        assertEquals(room1, foundRoom);
        
        ChatRoom foundRoom2 = repository.findByPacilianIdAndCaregiverId(pacilianId, caregiver2Id);
        assertEquals(room2, foundRoom2);
    }
} 