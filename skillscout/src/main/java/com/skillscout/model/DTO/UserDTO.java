package com.skillscout.model.DTO;

import com.skillscout.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String bio;
    private String profilePhotoURL;
    private Role role;
}