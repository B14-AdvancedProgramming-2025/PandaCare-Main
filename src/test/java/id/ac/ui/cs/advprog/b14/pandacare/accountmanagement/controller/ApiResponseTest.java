package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ApiResponseTest {
    @Test
    void testDefaultConstructor() {
        ApiResponse<String> response = new ApiResponse<>();

        assertNull(response.getMessage());
        assertNull(response.getResult());

        response.setStatusCode(200);
        response.setMessage("Test");
        response.setResult("Test");

        assertEquals(200, response.getStatusCode());
        assertEquals("Test", response.getMessage());
        assertEquals("Test", response.getResult());
    }
}
