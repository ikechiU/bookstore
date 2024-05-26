package org.example.bookstore.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import org.example.bookstore.enums.Status;
import org.example.bookstore.model.BaseModel;

import java.time.LocalDateTime;

/**
 * @author Ikechi Ucheagwu
 * @createdOn May-24(Fri)-2024
 */

@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseProfile extends BaseModel {
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    private String password;
    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "is_default_password", nullable = false)
    private boolean isDefaultPassword;

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public LocalDateTime getLastLoginDate() {
        return this.lastLoginDate;
    }

    public Status getStatus() {
        return this.status;
    }

    public boolean isDefaultPassword() {
        return this.isDefaultPassword;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setLastLoginDate(final LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    @JsonIgnore
    public void setStatus(final Status status) {
        this.status = status;
    }

    public void setDefaultPassword(final boolean isDefaultPassword) {
        this.isDefaultPassword = isDefaultPassword;
    }

    public BaseProfile(final String username, final String password,  final LocalDateTime lastLoginDate, final Status status, final boolean isDefaultPassword) {
        this.status = Status.INACTIVE;
        this.username = username;
        this.password = password;
        this.lastLoginDate = lastLoginDate;
        this.status = status;
        this.isDefaultPassword = isDefaultPassword;
    }

    public BaseProfile() {
        this.status = Status.INACTIVE;
    }
}

