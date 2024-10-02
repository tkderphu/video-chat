package com.example.video_chat.service;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import com.example.video_chat.model.FileEntity;
import com.example.video_chat.model.FileType;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    FileEntity save(MultipartFile file, FileType fileType);
    void delete(String url);
    Resource readFile(String url);
}
