package com.example.video_chat.repository;
import com.example.video_chat.domain.entities.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    void delete(FileEntity fileEntity);
    FileEntity findByUrl(String url);
}
