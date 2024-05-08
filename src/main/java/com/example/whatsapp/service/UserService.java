package com.example.whatsapp.service;

import com.example.whatsapp.model.Media;
import com.example.whatsapp.model.UserData;
import com.example.whatsapp.model.dto.AuthenticationReq;
import com.example.whatsapp.model.dto.BaseResponse;
import com.example.whatsapp.model.dto.UserDataDto;
import com.example.whatsapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final Utils utils;

    public ResponseEntity<BaseResponse> sendOtp(String phone) {
        BaseResponse response = BaseResponse.builder().status(true).description("Success").build();

        try {
            Random r = new Random();
            String code = String.format("%04d", r.nextInt(1001));
            UserData user = userRepository.findFirstByPhone(phone).orElse(UserData.builder()
                    .phone(phone)
                    .build());
            user.setCode(code);
            userRepository.save(user);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            response.setStatus(false);
            response.setDescription("Unable to send OTP");
        }
        if (!response.isStatus()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<BaseResponse> authenticateUser(AuthenticationReq authenticationReq) {
        BaseResponse response = BaseResponse.builder().status(true).description("Success").build();

        try {
            Optional<UserData> userDataOptional = userRepository.findFirstByPhone(authenticationReq.getPhone());
            if (userDataOptional.isPresent()) {
                if (Objects.equals(userDataOptional.get().getCode(), authenticationReq.getCode())) {
                    return ResponseEntity.ok(response);
                } else {
                    response.setStatus(false);
                    response.setDescription("Incorrect code");
                }
            } else {
                response.setStatus(false);
                response.setDescription("User not found");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            response.setStatus(false);
            response.setDescription("Unable to Authenticate");
        }
        if (!response.isStatus()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);

    }

    public ResponseEntity<BaseResponse> editUser(Long id, UserDataDto userDataDto) {
        BaseResponse response = BaseResponse.builder().status(true).description("Success").build();
        try {
            UserData userData = userRepository.findById(id).orElse(null);
            if (Objects.nonNull(userDataDto.getAbout()) && userDataDto.getAbout().length() > 139) {
                response.setStatus(false);
                response.setDescription("About field too long");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (Objects.nonNull(userData)) {
                userData.setPhone(userDataDto.getPhone());
                userData.setFirstName(userDataDto.getFirstName());
                userData.setLastName(userDataDto.getLastName());
                userData.setAbout(userDataDto.getAbout());

                // userData.setProfilePic();
                MultipartFile picture = userDataDto.getProfilePic().orElse(null);
                Media media = utils.saveFile(picture);

                if (picture != null && Objects.isNull(media)) {
                    response.setStatus(false);
                    response.setDescription("Unable to Edit");
                } else {
                    Media prev = userData.getProfilePic();
                    userData.setProfilePic(media);
                    userRepository.save(userData);
                    if (Objects.nonNull(prev)) {
                        utils.removeFile(prev.getLocation());
                    }
                }
            } else {
                response.setStatus(false);
                response.setDescription("User not found");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            response.setStatus(false);
            response.setDescription("Unable to Edit");
        }
        if (!response.isStatus()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }
}
