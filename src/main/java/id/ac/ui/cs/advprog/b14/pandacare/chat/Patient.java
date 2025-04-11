package id.ac.ui.cs.advprog.b14.pandacare.chat;

public class Patient extends ChatUser {
    
    public Patient(ChatMediator mediator, String name) {
        super(mediator, name);
    }
    
    @Override
    public void send(String message) {
        System.out.println(this.name + " (Patient): Sending Message: " + message);
        mediator.sendMessage(message, this);
    }
    
    @Override
    public void receive(String message) {
        System.out.println(this.name + " (Patient): Received Message: " + message);
    }
} 