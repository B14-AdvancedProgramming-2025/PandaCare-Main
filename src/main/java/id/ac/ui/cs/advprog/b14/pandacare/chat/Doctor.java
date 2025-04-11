package id.ac.ui.cs.advprog.b14.pandacare.chat;

public class Doctor extends ChatUser {
    
    public Doctor(ChatMediator mediator, String name) {
        super(mediator, name);
    }
    
    @Override
    public void send(String message) {
        System.out.println(this.name + " (Doctor): Sending Message: " + message);
        mediator.sendMessage(message, this);
    }
    
    @Override
    public void receive(String message) {
        System.out.println(this.name + " (Doctor): Received Message: " + message);
    }
} 