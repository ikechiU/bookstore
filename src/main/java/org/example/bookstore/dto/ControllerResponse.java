package org.example.bookstore.dto;

import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-25(Sat)-2024
 */

public class ControllerResponse {

    public static ResponseEntity<Object> getResponse() {;
        return ResponseEntity.ok(new ResponseDTO<>("200", "Success"));
    }

    public static ResponseEntity<Object> getResponse(String message) {;
        return ResponseEntity.ok(new ResponseDTO<>("200", message));
    }

    public static ResponseEntity<Object> getResponse(String statusCode, String message) {;
        return ResponseEntity.ok(new ResponseDTO<>(statusCode, message));
    }

    public static ResponseEntity<Object> getResponse(Collection<Object> data) {;
        return ResponseEntity.ok(new ResponseDTO<>("200", "Success", data));
    }

    public static ResponseEntity<Object> getResponse(String message, Collection<Object> data) {;
        return ResponseEntity.ok(new ResponseDTO<>("200", message, data));
    }

    public static ResponseEntity<Object> getResponse(String statusCode, String message, Collection<Object> data) {;
        return ResponseEntity.ok(new ResponseDTO<>(statusCode, message, data));
    }

    public static ResponseEntity<Object> getResponse(Object data) {;
        return ResponseEntity.ok(new ResponseDTO<>("200", "Success", Collections.singleton(data)));
    }

    public static ResponseEntity<Object> getResponse(String message, Object data) {;
        return ResponseEntity.ok(new ResponseDTO<>("200", message, Collections.singleton(data)));
    }

    public static ResponseEntity<Object> getResponse(String statusCode, String message, Object data) {
        return ResponseEntity.ok(new ResponseDTO<>(statusCode, message, Collections.singleton(data)));
    }

}
