package org.example.bookstore.dto;

import lombok.*;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-25(Sat)-2024
 */

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<E> {
    private String statusCode;
    private String statusMessage;
    private Collection<E> data;
    private Collection<Object> errors;

    public ResponseDTO(String statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.data = Collections.emptyList();
        this.errors = Collections.emptyList();
    }

    public ResponseDTO(String statusCode, String statusMessage, Collection<E> data) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.data = data;
        this.errors = Collections.emptyList();
    }

}
