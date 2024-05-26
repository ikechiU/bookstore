package org.example.bookstore.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PermissionDTO implements Serializable {
    private String name;
    private String description;
    private String permissionType;
}
