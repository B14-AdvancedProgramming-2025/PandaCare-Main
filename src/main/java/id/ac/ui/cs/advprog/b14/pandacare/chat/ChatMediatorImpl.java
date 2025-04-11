package id.ac.ui.cs.advprog.b14.pandacare.chat;

import java.util.ArrayList;
import java.util.List;

public class ChatMediatorImpl implements ChatMediator {
    private List<ChatUser> users;
    
    public ChatMediatorImpl() {
        this.users = new ArrayList<>();
    }
    
    @Override
    public void sendMessage(String message, ChatUser sender) {
        // Will be implemented later
    }
    
    @Override
    public void addUser(ChatUser user) {
        // Will be implemented later
    }
} 