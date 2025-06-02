package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.dto;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.UserType;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateProfileDTOTest {
    @Test
    void testDefaultConstructor() {
        List<String> medicalHistory = new ArrayList<>();
        medicalHistory.add("1");
        List<String> workingSchedule = new ArrayList<>();
        workingSchedule.add("1");

        UpdateProfileDTO dto = new UpdateProfileDTO(
                "name",
                "address",
                "1234567890",
                UserType.PACILIAN,
                medicalHistory,
                "specialty",
                workingSchedule,
                "1234567890",
                "test@gmail.com"
        );

        assertNotNull(dto);
    }

}
