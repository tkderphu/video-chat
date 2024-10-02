package com.example.video_chat.controller;
import com.example.video_chat.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {
    @Autowired
    private FileStorageService fileStorageService;
    @GetMapping
    public ResponseEntity<Resource> getFile(@RequestParam(name = "url") String url) throws IOException {
        Resource file = fileStorageService.readFile(url);
        Path filePath;
        try{
            filePath = Paths.get(file.getURI());
        }
        catch (Exception e){
            throw new RuntimeException();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath)).body(file);
    }
}
