package com.warrantybee.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tblUsers")
@Getter
@Setter
public class User extends BaseEntity<User> {
    @Column(name = "firstname", nullable = false, length = 128)
    private String firstname;

    @Column(name = "lastname", nullable = false, length = 128)
    private String lastname;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    @Getter(AccessLevel.NONE)
    private String password;
}
