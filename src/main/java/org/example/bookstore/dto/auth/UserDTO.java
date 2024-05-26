package org.example.bookstore.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
