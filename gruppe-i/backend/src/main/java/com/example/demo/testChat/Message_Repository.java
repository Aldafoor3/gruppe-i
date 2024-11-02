package com.example.demo.testChat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Message_Repository extends JpaRepository<Message_Instance,Long> {


    List<Message_Instance> findByChatRoomIdOrderByMessageId(Long chatRoomId);
}
