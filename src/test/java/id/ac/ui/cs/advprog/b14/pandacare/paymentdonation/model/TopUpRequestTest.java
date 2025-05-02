package id.ac.ui.cs.advprog.b14.pandacare.paymentdonation.model;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class TopUpRequestTest {
    @Test
    void testDefaultConstructor() {
        TopUpRequest request = new TopUpRequest(1L, 10000.50, "Ini tes topup aja", null);

        assertEquals(1L, request.getWalletId());
        assertEquals(10000.50, request.getAmount());
        assertEquals("Ini tes topup aja", request.getDescription());
        assertNull(request.getPaymentGatewayDetails());
    }

    @Test
    void testPaymentGatewayDetails() {
        TopUpRequest request = new TopUpRequest(1L, 10000.50, "Ini tes topup aja", null);

        Map<String, String> details = new HashMap<>();
        details.put("provider", "tes provider brow");
        details.put("token", "tes token nih");

        request.setPaymentGatewayDetails(details);

        assertEquals("tes provider brow", request.getPaymentGatewayDetails().get("provider"));
        assertEquals("tes token nih", request.getPaymentGatewayDetails().get("token"));
    }
}