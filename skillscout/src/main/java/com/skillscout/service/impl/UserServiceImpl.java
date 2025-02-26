package com.skillscout.service.impl;

import com.skillscout.Repository.SkillRepository;
import com.skillscout.Repository.UserRepository;
import com.skillscout.exception.ResourceNotFoundException;
import com.skillscout.model.DTO.ChangePasswordDTO;
import com.skillscout.model.DTO.ProfileResponseDTO;
import com.skillscout.model.DTO.UserDTO;
import com.skillscout.model.DTO.UserProfileDTO;
import com.skillscout.model.entity.Skill;
import com.skillscout.model.entity.User;
import com.skillscout.model.mapper.UserMapper;
import com.skillscout.service.CloudinaryImageService;
import com.skillscout.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;
    private CloudinaryImageService cloudinaryImageService;

    private SkillRepository skillRepository;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, CloudinaryImageService cloudinaryImageService, SkillRepository skillRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.cloudinaryImageService = cloudinaryImageService;
        this.skillRepository = skillRepository;
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
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Convert User to UserDTO using ModelMapper
        UserDTO userDTO = userMapper.userToUserDTO(user);

        // Manually set skills and profile picture URL
        userDTO.setSkills(user.getSkills().stream().map(Skill::getName).toList());
        userDTO.setProfilePhotoURL(user.getProfilePictureURL());

        return ResponseEntity.ok(userDTO);
    }


    @Override
    @Transactional
    public ResponseEntity<ProfileResponseDTO> updateUserProfile(UserProfileDTO userProfileDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setFirstName(userProfileDTO.getFirstName());
        user.setLastName(userProfileDTO.getLastName());
        user.setBio(userProfileDTO.getBio());

        // Handle profile picture upload
        MultipartFile photo = userProfileDTO.getProfilePicture();
        if (photo != null && !photo.isEmpty()) {
            try {
                Map uploadImageMap = cloudinaryImageService.upload(photo);
                String photoUrl = (String) uploadImageMap.get("secure_url");
                user.setProfilePictureURL(photoUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload profile picture: " + e.getMessage());
            }
        }

        // Handle skills update
        if (userProfileDTO.getSkills() != null) {
            List<Skill> newSkills = userProfileDTO.getSkills().stream()
                    .map(skillName -> skillRepository.findByName(skillName)
                            .orElseGet(() -> skillRepository.save(new Skill(skillName))))
                    .collect(Collectors.toList()); // Use mutable list

            user.setSkills(newSkills);
        }

        // Save is optional in @Transactional but can be kept for clarity
        userRepository.save(user);

        ProfileResponseDTO updatedUserProfileDTO = userMapper.userToUpdatedProfileDTO(user);
        return ResponseEntity.ok(updatedUserProfileDTO);
    }


}