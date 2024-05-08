package com.example.whatsapp.controller;

import com.example.whatsapp.model.dto.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class MaxFileSizeException extends RuntimeException {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<BaseResponse> handleMaxSizeException() {
        BaseResponse response = BaseResponse.builder().description("File too large").status(false).build();
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
    }
}
