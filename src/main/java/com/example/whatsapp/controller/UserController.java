package com.example.whatsapp.controller;

import com.example.whatsapp.model.dto.AuthenticationReq;
import com.example.whatsapp.model.dto.BaseResponse;
import com.example.whatsapp.model.dto.UserDataDto;
import com.example.whatsapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/send-otp")
    public ResponseEntity<BaseResponse> sendOtp(String phone) {
        return userService.sendOtp(phone);
    }

    @PostMapping("/signin")
    public ResponseEntity<BaseResponse> authenticateUser(@RequestBody AuthenticationReq authenticationReq) {
        return userService.authenticateUser(authenticationReq);
    }

    @PostMapping(path = "/edit/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<BaseResponse> editUser(@PathVariable("id") Long id,
            @ModelAttribute UserDataDto userDataDto) {
        return userService.editUser(id, userDataDto);
    }

}
