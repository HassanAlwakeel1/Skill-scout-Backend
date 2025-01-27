package com.skillscout.service;

import com.skillscout.model.DTO.JwtAuthenticationResponse;
import com.skillscout.model.DTO.RefreshTokenRequest;
import com.skillscout.model.DTO.SignInRequest;
import com.skillscout.model.DTO.SignUpRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signin(SignInRequest signinRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    void initiatePasswordReset(String email);
    void resetPassword(String token, String newPassword);
}

