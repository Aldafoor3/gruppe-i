package com.example.demo.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<Users,Long> {

    Optional<Users> findByEmailAndPassword(String email, String password);

    Optional<Users> findByEmail(String email);


}
