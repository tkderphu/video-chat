package com.example.video_chat.repository;
import com.example.video_chat.repository.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Integer> {
    void delete(FileEntity fileEntity);
    FileEntity findByUrl(String url);
}
