package id.ac.ui.cs.advprog.b14.pandacare.chat;

public abstract class ChatUser {
    protected ChatMediator mediator;
    protected String name;
    
    public ChatUser(ChatMediator mediator, String name) {
        this.mediator = mediator;
        this.name = name;
    }
    
    public abstract void send(String message);
    public abstract void receive(String message);
} 