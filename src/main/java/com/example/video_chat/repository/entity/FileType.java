package com.example.video_chat.repository.entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum FileType {
    MESSAGE("src/main/resources/uploads/message/");
    private final String folderPath;
    FileType(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void createDirectoryIfNotExists() throws IOException {
        Path path = Paths.get(folderPath);
        if (!Files.exists(path)){
            Files.createDirectories(path);
        }
    }
}
