package com.example.video_chat.domain.entities;

import com.example.video_chat.domain.enums.FileType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "files")
public class FileEntity extends BaseEntity{

    private String name;
    private String url;

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    // Getters and setters



    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // Constructors
    public FileEntity() {}

    public FileEntity(String name, String url, FileType fileEntity) {
        this.name = name;
        this.url = url;
        this.fileType = fileEntity;
    }
}
