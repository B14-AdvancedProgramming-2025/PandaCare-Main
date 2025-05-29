package id.ac.ui.cs.advprog.b14.pandacare.rating.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RatingRequestTest {

    @Test
    void testGetterAndSetter() {
        RatingRequest request = new RatingRequest();
        request.setCaregiverId("c1");
        request.setPacilianEmail("p1");
        request.setValue(4);
        request.setComment("Test comment");

        assertEquals("c1", request.getCaregiverId());
        assertEquals("p1", request.getPacilianEmail());
        assertEquals(4, request.getValue());
        assertEquals("Test comment", request.getComment());
    }
}
