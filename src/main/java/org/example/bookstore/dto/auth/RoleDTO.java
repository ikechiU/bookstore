package org.example.bookstore.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bookstore.enums.Status;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RoleDTO implements Serializable {
    private String name;
    private String description;
    private Set<PermissionDTO> permissions = new HashSet<>();
    private Status status;
}
