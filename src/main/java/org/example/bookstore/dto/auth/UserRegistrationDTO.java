package org.example.bookstore.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@Data
@Builder
public class UserRegistrationDTO {
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Valid email required")
    private String email;
    @NotBlank(message = "First name is mandatory")
    private String firstName;
    @NotBlank(message = "Last name mandatory")
    private String lastName;
    @NotBlank(message = "Password is mandatory")
    private String password;
    private String phoneNumber;
}
