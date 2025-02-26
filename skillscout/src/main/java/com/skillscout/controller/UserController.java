package com.skillscout.controller;

import com.skillscout.model.DTO.ChangePasswordDTO;
import com.skillscout.model.DTO.ProfileResponseDTO;
import com.skillscout.model.DTO.UserDTO;
import com.skillscout.model.DTO.UserProfileDTO;
import com.skillscout.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<String> changePassword(@PathVariable(name = "id") Long userId,
                                                 @RequestBody ChangePasswordDTO changePasswordDTO){
        return userService.changePassword(changePasswordDTO, userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable(value = "id") Long userId){
        return userService.getUserById(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponseDTO> updateUserProfile(@ModelAttribute UserProfileDTO userProfileDTO,
                                                                @PathVariable(value = "id") Long userId){
        return userService.updateUserProfile(userProfileDTO, userId);
    }
}
