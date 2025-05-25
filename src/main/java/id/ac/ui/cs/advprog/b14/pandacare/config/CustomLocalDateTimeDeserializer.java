package id.ac.ui.cs.advprog.b14.pandacare.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateString = p.getText();
        try {
            return LocalDateTime.parse(dateString, FORMATTER);
        } catch (DateTimeParseException e) {
            // You might want to log this error or handle it more gracefully
            // For now, rethrowing as IOException to indicate failure
            throw new IOException("Failed to parse date string: [" + dateString + "] with format [dd-MM-yyyy HH:mm]", e);
        }
    }
}
