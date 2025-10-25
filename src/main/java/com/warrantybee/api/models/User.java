package com.warrantybee.api.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/** Represents an application user */
@Entity
@Table(name = "tblUsers")
@Getter
@Setter
public class User extends BaseEntity<User> {

    /** User's first name */
    @Column(name = "firstname", nullable = false, length = 128)
    private String firstname;

    /** User's last name */
    @Column(name = "lastname", nullable = false, length = 128)
    private String lastname;

    /** User's email address */
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    /** User's password (hidden in getter) */
    @Column(name = "password", nullable = false, length = 255)
    private String password;
}
