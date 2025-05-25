package id.ac.ui.cs.advprog.b14.pandacare.authentication.event;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.Pacilian;
import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.User;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserCreatedEventTest {

    @Test
    void testEventCreation() {
        User user = new Pacilian(
                "test-id",
                "test@example.com",
                "password123",
                "Test User",
                "1234567890123456",
                "Test Address",
                "08123456789",
                Arrays.asList("None")
        );
        Object source = new Object();

        UserCreatedEvent event = new UserCreatedEvent(source, user);

        assertNotNull(event);
        assertEquals(user, event.getUser());
        assertEquals(source, event.getSource());
    }

    @Test
    void testEventWithNullSource() {
        User user = new Pacilian(
                "test-id",
                "test@example.com",
                "password123",
                "Test User",
                "1234567890123456",
                "Test Address",
                "08123456789",
                List.of("None")
        );

        assertThrows(IllegalArgumentException.class, () -> {
            new UserCreatedEvent(null, user);
        });
    }
}