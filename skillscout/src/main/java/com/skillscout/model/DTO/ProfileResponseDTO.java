package com.skillscout.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String Bio;
    private String ProfilePictureURL;
    private List<String> skills;
}
