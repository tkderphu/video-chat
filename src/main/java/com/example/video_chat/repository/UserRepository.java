package com.example.video_chat.repository;

import com.example.video_chat.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmailIgnoreCase(String email);


    Page<User> findAllByEmailIsNotContainingIgnoreCase(
            String currentUsername,
            Pageable pageable
    );
}
