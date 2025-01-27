package com.skillscout.service.impl;

import com.skillscout.Repository.UserRepository;
import com.skillscout.exception.ResourceNotFoundException;
import com.skillscout.model.DTO.ChangePasswordDTO;
import com.skillscout.model.DTO.UserDTO;
import com.skillscout.model.entity.User;
import com.skillscout.model.mapper.UserMapper;
import com.skillscout.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
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
}