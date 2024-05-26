package org.example.bookstore.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public class Auditable<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedBy
    protected T createdBy;
    @CreatedDate
    protected LocalDateTime createdDate;
    @LastModifiedBy
    protected T lastModifiedBy;
    @LastModifiedDate
    protected LocalDateTime lastModifiedDate;

    public Auditable() {
    }

    public Long getId() {
        return this.id;
    }

    public T getCreatedBy() {
        return this.createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public T getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public LocalDateTime getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setCreatedBy(final T createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastModifiedBy(final T lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public void setLastModifiedDate(final LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}

