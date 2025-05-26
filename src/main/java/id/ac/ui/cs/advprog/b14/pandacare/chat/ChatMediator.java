package id.ac.ui.cs.advprog.b14.pandacare.chat;

public interface ChatMediator {
    void sendMessage(String roomId, String senderId, String recipientId, String content);
} 