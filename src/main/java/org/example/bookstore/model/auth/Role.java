package org.example.bookstore.model.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.bookstore.model.BaseModel;
import org.example.bookstore.model.auth.Permission;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */


@Entity
@Table(name = "roles")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Role extends BaseModel implements Serializable {
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    @Column(nullable = false)
    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "system_roles_and_permissions_mapping",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")})
    @ToString.Exclude
    private Set<Permission> permissions = new HashSet<>();
}
