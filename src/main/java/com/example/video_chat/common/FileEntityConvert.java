package com.example.video_chat.common;

import com.example.video_chat.api.FileController;
import com.example.video_chat.domain.entities.FileEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileEntityConvert {
    public static String fileEntityToString(FileEntity fileEntity) {
        return null;
    }

    public static List<String> fileEntityToString(Collection<? extends FileEntity> fileEntity) {
        if(CollectionUtils.isEmpty(fileEntity)) {
            return Collections.emptyList();
        };
        return fileEntity.stream()
                .map(s -> MvcUriComponentsBuilder.fromMethodName(
                        FileController.class,
                        "readFile",
                        s.getUrl()
                ).toUriString())
                .collect(Collectors.toList());
    }
}
