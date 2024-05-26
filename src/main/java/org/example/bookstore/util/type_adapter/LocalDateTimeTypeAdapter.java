package org.example.bookstore.util.type_adapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

public class LocalDateTimeTypeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private final DateTimeFormatter formatter;

    public LocalDateTimeTypeAdapter(String dateFormat) {
        this.formatter = (new DateTimeFormatterBuilder()).appendPattern(dateFormat).optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).optionalEnd().toFormatter();
    }

    public LocalDateTimeTypeAdapter() {
        this.formatter = DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss");
    }

    public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(this.formatter.format(localDateTime));
    }

    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalDateTime.parse(json.getAsString().replace("T", " "), this.formatter);
    }
}

