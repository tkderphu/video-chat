package com.example.video_chat.repository;

import com.example.video_chat.domain.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
