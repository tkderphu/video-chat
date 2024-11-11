package com.example.video_chat.controller;

import com.example.video_chat.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/v1/files")
@CrossOrigin("*")
public class FileController {
    @Autowired
    private FileStorageService fileStorageService;

//     http://localhost:8080/api/v1/files?url=C%3A%5CUsers%5Cthanh%5COneDrive%5CPictures%5C2024-9-12%2010-57-36.png
    @GetMapping
    public ResponseEntity<Resource> readFile(@RequestParam(name = "url") String url) {
        try {
            // Đọc file từ service
            Resource file = fileStorageService.readFile(url);
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }
            String contentType = Files.probeContentType(file.getFile().toPath());
            String fileName = file.getFilename();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(file);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error reading file", e);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
