package com.example.video_chat.ControllerAdvice;

import com.example.video_chat.exception.FileNotFoundException;
import com.example.video_chat.model.ErrorRespondDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ControllerAdvisor extends DefaultHandler {
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<Object> handleFileException(FileNotFoundException ex, WebRequest request ) {
        ErrorRespondDTO errorRespondDTO = new ErrorRespondDTO();
        errorRespondDTO.setError(ex.getMessage());
        List<String> details = new ArrayList<>();
        details.add("File not found");
        errorRespondDTO.setDetails(details);
        return new ResponseEntity<Object>(errorRespondDTO, HttpStatus.NOT_FOUND);
    }
}
