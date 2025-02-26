package com.skillscout.service.impl;

import com.skillscout.Repository.UserRepository;
import com.skillscout.exception.ResourceNotFoundException;
import com.skillscout.model.DTO.ChangePasswordDTO;
import com.skillscout.model.DTO.ProfileResponseDTO;
import com.skillscout.model.DTO.UserDTO;
import com.skillscout.model.DTO.UserProfileDTO;
import com.skillscout.model.entity.User;
import com.skillscout.model.mapper.UserMapper;
import com.skillscout.service.CloudinaryImageService;
import com.skillscout.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;
    private CloudinaryImageService cloudinaryImageService;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, CloudinaryImageService cloudinaryImageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.cloudinaryImageService = cloudinaryImageService;
    }

    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                return userRepository.findByEmail(email)
                        .orElseThrow(()-> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public ResponseEntity<String> changePassword(ChangePasswordDTO changePasswordDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
        String userPassword = user.getPassword();
        String oldPassword = changePasswordDTO.getOldPassword();
        String newPassword = changePasswordDTO.getNewPassword();
        String newPasswordConfirmation = changePasswordDTO.getNewPasswordConfirmation();
        if (newPassword.equals(newPasswordConfirmation) && passwordEncoder.matches(oldPassword, userPassword)){
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return ResponseEntity.ok("Password changed successfully!");
        }else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad credentials");
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
        UserDTO userDTO = userMapper.userToUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @Override
    public ResponseEntity<ProfileResponseDTO> updateUserProfile(UserProfileDTO userProfileDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",userId));

        user.setFirstName(userProfileDTO.getFirstName());
        user.setLastName(userProfileDTO.getLastName());
        user.setBio(userProfileDTO.getBio());
        MultipartFile photo = userProfileDTO.getProfilePicture();
        if (photo != null && !photo.isEmpty()) {
            Map uploadImageMap = cloudinaryImageService.upload(photo);
            String photoUrl = (String)uploadImageMap.get("secure_url");
            user.setProfilePictureURL(photoUrl);
        }
        userRepository.save(user);
        ProfileResponseDTO updatedUserProfileDTO = userMapper.userToUpdatedProfileDTO(user);
        return ResponseEntity.ok(updatedUserProfileDTO);
    }
}