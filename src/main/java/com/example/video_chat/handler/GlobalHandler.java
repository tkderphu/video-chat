package com.example.video_chat.handler;

import com.example.video_chat.domain.modelviews.response.ApiResponse;
import com.example.video_chat.handler.exception.FileNotFoundException;
import com.example.video_chat.handler.exception.GeneralException;
import com.example.video_chat.domain.modelviews.response.ErrorRespondDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalHandler {
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleFileException(FileNotFoundException ex) {
        ErrorRespondDTO errorRespondDTO = new ErrorRespondDTO();
        errorRespondDTO.setError(ex.getMessage());
        List<String> details = new ArrayList<>();
        details.add("File not found");
        errorRespondDTO.setDetails(details);
        return new ResponseEntity<>(errorRespondDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ApiResponse<?> handleFileException(UsernameNotFoundException ex) {
        return new ApiResponse<>(
                ex.getMessage(),
                400,
                1,
                null
        );
    }


    @ExceptionHandler(GeneralException.class)
    public ApiResponse<?> handleException(GeneralException ex) {
        return new ApiResponse<>(
                ex.getMessage(),
                400,
                1,
                null
        );
    }

}
