package org.example.bookstore.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.bookstore.util.type_adapter.BooleanTypeAdapter;
import org.example.bookstore.util.type_adapter.LocalDateTimeTypeAdapter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

public class AppUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static String getLoggedInUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth == null ? "SYSTEM" : auth.getPrincipal().equals("anonymousUser") ? "anonymousUser" : auth.getName();
    }

    public static ObjectMapper getObjectMapper() {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    public static Pageable getPageable(int page, int size) {
        page = page > 1 ? page - 1 : 0;
        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
    }

    public static Gson getGsonMapper(String dateFormat) {
        return (new GsonBuilder()).registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter(dateFormat)).registerTypeAdapter(Boolean.class, new BooleanTypeAdapter()).create();
    }

    public static Gson getGsonMapper() {
        return (new GsonBuilder()).registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter()).registerTypeAdapter(Boolean.class, new BooleanTypeAdapter()).create();
    }

}