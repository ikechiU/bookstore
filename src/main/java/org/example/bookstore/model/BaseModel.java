package org.example.bookstore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseModel extends Auditable<String> implements Serializable {
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public BaseModel(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public BaseModel() {
    }
}