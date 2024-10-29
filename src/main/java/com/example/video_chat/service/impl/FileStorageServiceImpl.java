package com.example.video_chat.service.impl;
import com.example.video_chat.handler.exception.FileNotFoundException;
import com.example.video_chat.handler.exception.GeneralException;
import com.example.video_chat.repository.FileRepository;
import com.example.video_chat.domain.entities.FileEntity;
import com.example.video_chat.domain.entities.FileType;
import com.example.video_chat.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final String PATH = "/uploads";

    @Override
    public FileEntity save(MultipartFile file, FileType fileType) {
        if(file == null || file.isEmpty()) {
            throw new GeneralException("File can't null");
        }
        String url = PATH;
        File f;
        if(!(f = new File(url)).exists() && f.mkdir());
        url += "/" + fileType.name();

        if(!(f = new File(url)).exists() && f.mkdir());

        String fileExtension = getExtension(file.getOriginalFilename());

        String fileName = UUID.randomUUID() + fileExtension;

        url += "/" + fileName;

        FileEntity fileEntity = new FileEntity(
                fileName,
                url,
                FileType.MESSAGE
        );

        try {
            Files.copy(file.getInputStream(), Path.of(url));
        } catch (IOException e) {
            throw new GeneralException("File can't create");
        }

        return fileEntity;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    @Override
    public void delete(String url) {
        try {
            Files.deleteIfExists(Path.of(url));
        } catch (IOException e) {
            throw new GeneralException("File can't find in system");
        }
    }

    @Override
    public Resource readFile(String url) {
        try{
            Path filePath = Paths.get(url).normalize().toAbsolutePath();
            if (Files.exists(filePath) || Files.isReadable(filePath)) {
                Resource resource = new UrlResource(filePath.toUri());
                if (resource.exists() || resource.isReadable()) {
                    return resource;
                } else{
                    throw new FileNotFoundException("File not found");
                }
            }
            else{
                throw new FileNotFoundException("File not found");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException();
        }
    }
}
