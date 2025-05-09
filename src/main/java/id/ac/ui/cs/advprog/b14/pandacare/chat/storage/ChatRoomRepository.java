package id.ac.ui.cs.advprog.b14.pandacare.chat.storage;

import id.ac.ui.cs.advprog.b14.pandacare.chat.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    
    List<ChatRoom> findByPacilianId(String pacilianId);
    
    List<ChatRoom> findByCaregiverId(String caregiverId);
    
    ChatRoom findByPacilianIdAndCaregiverId(String pacilianId, String caregiverId);
} 