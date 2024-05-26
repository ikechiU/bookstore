package org.example.bookstore.dto.bookstore;

import lombok.*;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-25(Sat)-2024
 */

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CompleteBookResponseDTO extends PartialBookResponseDTO {
    private String content;
}
