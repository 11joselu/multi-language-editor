package org.talayos.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.talayos.api.entities.ZipEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ZipEntity> handleIOException(HttpServletRequest req, Exception ex) {
        log.info("Error reading zip file. Maybe request is not a ZIP file");

        ZipEntity zipEntity = new ZipEntity();
        zipEntity.setMessage("Error reading Zip file");
        zipEntity.setData(new ArrayList<>());
        zipEntity.setStatusCode(HttpStatus.BAD_REQUEST.value());


        ResponseEntity<ZipEntity> responseEntity = new ResponseEntity<>(zipEntity,
                HttpStatus.BAD_REQUEST);

        return responseEntity;
    }

    @ExceptionHandler(FileUploadBase.InvalidContentTypeException.class)
    public ResponseEntity<ZipEntity> invalidContentType(HttpServletRequest req, Exception ex) {
        log.info("Reading ZIP file, content is not a properties files");

        ZipEntity zipEntity = new ZipEntity();
        zipEntity.setMessage("Content files not supported");
        zipEntity.setData(new ArrayList<>());
        zipEntity.setStatusCode(HttpStatus.BAD_REQUEST.value());

        ResponseEntity<ZipEntity> responseEntity = new ResponseEntity<>(zipEntity,
                HttpStatus.BAD_REQUEST);

        return responseEntity;
    }

    @ExceptionHandler(FileUploadBase.FileUploadIOException.class)
    public ResponseEntity<ZipEntity> invalidFileUploadException(HttpServletRequest req) {
        log.info("ZIP File is Empty");

        ZipEntity zipEntity = new ZipEntity();
        zipEntity.setMessage("Please select a file to upload");
        zipEntity.setData(new ArrayList<>());
        zipEntity.setStatusCode(HttpStatus.BAD_REQUEST.value());

        ResponseEntity<ZipEntity> responseEntity = new ResponseEntity<>(zipEntity,
                HttpStatus.BAD_REQUEST);

        return responseEntity;
    }
}
