package com.example.demo.admin;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "firstname")
    @Setter
    private String firstname;
    @Column(name = "lastname")
    @Setter
    private String lastname;
    @Column(name = "email")
    @Setter
    private String email;
    @Column(name = "password")
    @Setter
    private String password;

    @Column(name = "twoFactorCode")
    @Setter
    private int twoFactorCode;

    public Admin(String firstname, String lastname, String email, String password){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public Admin() {

    }


}
