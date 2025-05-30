package id.ac.ui.cs.advprog.b14.pandacare.authentication.event;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserCreatedEvent extends ApplicationEvent {
    private final User user;

    public UserCreatedEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}