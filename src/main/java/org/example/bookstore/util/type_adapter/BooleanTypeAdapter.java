package org.example.bookstore.util.type_adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

public class BooleanTypeAdapter implements JsonDeserializer<Boolean> {
    public BooleanTypeAdapter() {
    }

    public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return json.isJsonPrimitive() && json.getAsJsonPrimitive().isBoolean() ? json.getAsBoolean() : false;
    }
}
