package siacor.model.utilitarios;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomDateSerializer extends StdSerializer<LocalDateTime> {
    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm", new Locale("es"));

    public CustomDateSerializer() {
        this(null);
    }

    public CustomDateSerializer(Class<LocalDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        if (value != null) {
            String formatted = value.format(FORMATTER);
            // Capitaliza la primera letra del mes (MMM)
            String[] parts = formatted.split("/");
            if (parts.length >= 2) {
                parts[1] = parts[1].substring(0, 1).toUpperCase() + parts[1].substring(1);
                formatted = String.join("/", parts);
            }
            gen.writeString(formatted);
        } else {
            gen.writeNull();
        }
    }
}
