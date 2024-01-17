package com.ra.controller.user;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.UserChangePassRequestDTO;
import com.ra.model.dto.request.UserRequestDTO;
import com.ra.model.dto.response.UserInfoResponseDTO;
import com.ra.model.dto.response.UserResponseDTO;
import com.ra.model.entity.User;
import com.ra.repository.UserRepository;
import com.ra.security.user_principle.UserDetailService;
import com.ra.service.mail.EmailService;
import com.ra.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class AccountController {
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/account")
    public ResponseEntity<?> getUserLogin(Authentication authentication) {
        Long userId = userDetailService.getUserIdFromAuthentication(authentication);
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            UserInfoResponseDTO userInfoResponseDTO = userService.findByUserId(user.getId());
            if (userInfoResponseDTO != null) {
                return new ResponseEntity<>(userInfoResponseDTO, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/account/change-password")
    public ResponseEntity<?> changePassword(@RequestBody UserChangePassRequestDTO userChangePassRequestDTO, Authentication authentication) {
        Long userId = userDetailService.getUserIdFromAuthentication(authentication);
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            if (!userService.checkPass(user, userChangePassRequestDTO.getPassword())) {
                return new ResponseEntity<>("Password is incorrect !!!", HttpStatus.BAD_REQUEST);
            }
            userService.changePassword(user,userChangePassRequestDTO.getNewPass());
            return new ResponseEntity<>("Password had been changed !!!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Not Found",HttpStatus.NOT_FOUND);
    }

    @PutMapping("/account")
    public ResponseEntity<?> changeInfo(@ModelAttribute UserRequestDTO userRequestDTO,Authentication authentication) throws CustomException {
        Long userId = userDetailService.getUserIdFromAuthentication(authentication);
        User user = userRepository.findById(userId).orElse(null);
        if (user!=null){
            if (userRepository.existsByUserNameContainingIgnoreCase(user.getUserName())){
                throw  new CustomException("User name had been exist !!!");
            }
            if (userRepository.existsByEmailContainingIgnoreCase(user.getEmail())){
                throw  new CustomException("Email had been exist !!!");
            }
            if (userRepository.existsByPhoneContainingIgnoreCase(user.getPhone())){
                throw  new CustomException("Phone number had been exist !!!");
            }
            UserResponseDTO updateUser=userService.save(userId,userRequestDTO);
            return new ResponseEntity<>(updateUser,HttpStatus.OK);
        }
        return new ResponseEntity<>("Not found",HttpStatus.NOT_FOUND);
    }

//    @GetMapping("/sendMail")
//    public ResponseEntity<?> sendMail(){
//        emailService.sendMail();
//        return new ResponseEntity<>("Sent successfully !!!",HttpStatus.OK);
//    }

}
