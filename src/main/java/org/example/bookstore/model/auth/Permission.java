package org.example.bookstore.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.bookstore.model.BaseModel;

import java.io.Serializable;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@Entity
@Table(name = "permissions")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Permission extends BaseModel implements Serializable {
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    private String permissionType;
}
