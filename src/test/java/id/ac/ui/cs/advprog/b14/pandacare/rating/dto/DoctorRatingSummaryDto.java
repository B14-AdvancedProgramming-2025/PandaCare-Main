package id.ac.ui.cs.advprog.b14.pandacare.rating.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoctorRatingSummaryDtoTest {

    @Test
    @DisplayName("Constructor should set all fields correctly")
    void constructorAndGetters_ShouldSetFields() {
        // given
        String expectedId = "doc-123";
        String expectedName = "Dr. Test";
        double expectedAvg = 4.75;

        // when
        DoctorRatingSummaryDto dto = new DoctorRatingSummaryDto(expectedId, expectedName, expectedAvg);

        // then
        assertEquals(expectedId, dto.getCaregiverId(), "getCaregiverId should return value passed to constructor");
        assertEquals(expectedName, dto.getCaregiverName(), "getCaregiverName should return value passed to constructor");
        assertEquals(expectedAvg, dto.getAverageRating(), 0.0001, "getAverageRating should return value passed to constructor");
    }

    @Test
    @DisplayName("Setters should update fields correctly")
    void setters_ShouldModifyFields() {
        // given
        DoctorRatingSummaryDto dto = new DoctorRatingSummaryDto("initialId", "Initial Name", 3.0);

        // when
        dto.setCaregiverId("newId");
        dto.setCaregiverName("New Name");
        dto.setAverageRating(5.0);

        // then
        assertEquals("newId", dto.getCaregiverId(), "setCaregiverId should update caregiverId");
        assertEquals("New Name", dto.getCaregiverName(), "setCaregiverName should update caregiverName");
        assertEquals(5.0, dto.getAverageRating(), 0.0001, "setAverageRating should update averageRating");
    }

}
