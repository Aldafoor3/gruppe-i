package com.example.demo.testChat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChatRoom_Instance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    private String chatName;

    private int[] participantList;


    public boolean isUserIdpresent(int userId){
        for (int i = 0; i < participantList.length;i++){
            if (participantList[i] == userId) return true;
        }
        return false;
    }
}
