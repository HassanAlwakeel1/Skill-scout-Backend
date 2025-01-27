package com.skillscout.model.DTO;

import com.skillscout.model.enums.Role;
import lombok.Data;



@Data
public class SignUpRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Role role;
}
