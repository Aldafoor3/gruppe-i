package com.example.demo.testChat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Message_Instance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    private Long senderId;

    private Long chatRoomId;

    private String msg;

    private String email;

    private String firstName;

    private boolean read = false;

    //private status status

    private Date time;

    public Message_Instance(Long senderId, Long chatRoomId, String msg){
        this.senderId = senderId;
        this.chatRoomId = chatRoomId;
        this.msg = msg;
    }

    public Message_Instance() {

    }
}

