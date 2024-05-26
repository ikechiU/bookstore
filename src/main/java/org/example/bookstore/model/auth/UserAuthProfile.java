package org.example.bookstore.model.auth;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@Entity
@Table(name = "user_auth_profile", indexes = @Index(columnList = "username"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthProfile extends BaseProfile implements Serializable {

    @OneToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

    private String assignedRole;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_auth_profiles_and_permissions_mapping",
            joinColumns =
            @JoinColumn(name = "user_auth_profile_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<Permission> permissions = new HashSet<>();

}

