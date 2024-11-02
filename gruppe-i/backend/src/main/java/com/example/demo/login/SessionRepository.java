package com.example.demo.login;

import com.example.demo.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Sessions,Long> {


    Sessions findSessionsBySessionID(String sessionId);



}
