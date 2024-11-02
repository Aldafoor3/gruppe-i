package com.example.demo.testChat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoom_Repository extends JpaRepository<ChatRoom_Instance,Long> {

}
