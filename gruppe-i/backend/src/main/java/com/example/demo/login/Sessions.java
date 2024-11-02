package com.example.demo.login;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Sessions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "sessionID")
    private String sessionID;

    @Column(name = "isUser")
    private boolean isUser;

    @Column(name = "accID")
    private Long accID;

    public Sessions(String sessionID, boolean isUser, Long accID){
        this.sessionID = sessionID;
        this.isUser = isUser;
        this.accID = accID;
    }

    public Sessions(){

    }
}
