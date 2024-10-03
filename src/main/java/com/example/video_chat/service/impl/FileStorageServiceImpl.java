package com.example.video_chat.service.impl;
import com.example.video_chat.exception.FileNotFoundException;
import com.example.video_chat.repository.FileRepository;
import com.example.video_chat.repository.entity.FileEntity;
import com.example.video_chat.repository.entity.FileType;
import com.example.video_chat.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final Path rootLocation = Paths.get("upload");

    @Autowired
    private FileRepository fileRepository;


    @Override
    public FileEntity save(MultipartFile file, FileType fileType) {
        FileEntity fileEntity = new FileEntity();
        try{
            String fileName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = uuid + fileExtension;
            Path filePath = rootLocation.resolve(Paths.get(fileName).normalize().toAbsolutePath());
            Files.copy(file.getInputStream(), filePath);
            fileEntity.setName(newFileName);
            fileEntity.setUrl(filePath.toString());
            return fileRepository.save(fileEntity);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void delete(String url) {
        FileEntity fileEntity = fileRepository.findByUrl(url);
        if (fileEntity != null) {
            fileRepository.delete(fileEntity);
        }
        else
            throw new FileNotFoundException("File not found");
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
