package com.skillscout.service;

import com.skillscout.model.DTO.ChangePasswordDTO;
import com.skillscout.model.DTO.ProfileResponseDTO;
import com.skillscout.model.DTO.UserDTO;
import com.skillscout.model.DTO.UserProfileDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();
    ResponseEntity<String> changePassword(ChangePasswordDTO changePasswordDTO, Long userId);

    ResponseEntity<UserDTO> getUserById(Long userId);

    ResponseEntity<ProfileResponseDTO> updateUserProfile(UserProfileDTO userProfileDTO, Long userId);

}

