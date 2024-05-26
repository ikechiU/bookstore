package org.example.bookstore.dto.auth;

import lombok.*;
import org.example.bookstore.enums.Status;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDTO extends UserDTO implements UserDetails, Serializable {
    private String assignedRole;
    private Status status;
    private boolean isDeleted;
    private LocalDateTime lastLoginDate;
    private boolean isDefaultPassword;
    private UUID profileId;
    private String password;
    private Set<SimpleGrantedAuthority> permissions;
    private Set<PermissionDTO> userPermissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.permissions;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

}
