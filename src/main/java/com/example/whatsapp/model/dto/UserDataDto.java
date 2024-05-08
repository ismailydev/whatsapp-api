package com.example.whatsapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@AllArgsConstructor
@Data
public class UserDataDto {
    private String phone;
    private String about;
    private String firstName;
    private String lastName;
    private Optional<MultipartFile> profilePic;
}
