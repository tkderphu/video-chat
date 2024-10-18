package com.example.video_chat.service;
import com.example.video_chat.domain.entities.FileEntity;
import com.example.video_chat.domain.entities.FileType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    FileEntity save(MultipartFile file, FileType fileType);
    void delete(String url);
    Resource readFile(String url);
}
