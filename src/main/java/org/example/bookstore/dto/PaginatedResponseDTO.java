package org.example.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-25(Sat)-2024
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaginatedResponseDTO {
    private List<?> content = new ArrayList<>();
    private int currentPage;
    private long totalPages;
    private long totalItems;
    private Boolean isFirstPage;
    private Boolean isLastPage;

    public static PaginatedResponseDTO getPagination(Page<?> pagedResult) {
        return PaginatedResponseDTO.builder()
                .content(pagedResult.getContent())
                .currentPage(pagedResult.getNumber() + 1)
                .totalPages(pagedResult.getTotalPages())
                .totalItems(pagedResult.getTotalElements())
                .isFirstPage(pagedResult.isFirst())
                .isLastPage(pagedResult.isLast())
                .build();
    }

    public static PaginatedResponseDTO getPagination(Page<?> pagedResult, List<?> content) {
        return PaginatedResponseDTO.builder()
                .content(content)
                .currentPage(pagedResult.getNumber() + 1)
                .totalPages(pagedResult.getTotalPages())
                .totalItems(pagedResult.getTotalElements())
                .isFirstPage(pagedResult.isFirst())
                .isLastPage(pagedResult.isLast())
                .build();
    }
}
