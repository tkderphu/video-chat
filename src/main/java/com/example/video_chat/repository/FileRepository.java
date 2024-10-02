package com.example.video_chat.repository;

import com.example.video_chat.model.FileEntity;
import com.example.video_chat.model.FileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Integer> {
    FileEntity save(MultipartFile file, FileType fileType);
    void delete(String url);
    FileEntity findByUrl(String url);
}
