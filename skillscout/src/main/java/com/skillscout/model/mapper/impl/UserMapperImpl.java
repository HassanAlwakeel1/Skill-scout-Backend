package com.skillscout.model.mapper.impl;

import com.skillscout.model.DTO.ProfileResponseDTO;
import com.skillscout.model.DTO.UserDTO;
import com.skillscout.model.entity.Skill;
import com.skillscout.model.entity.User;
import com.skillscout.model.mapper.UserMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapperImpl implements UserMapper {
    private ModelMapper mapper;
    public UserMapperImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public UserDTO userToUserDTO(User user){
        return mapper.map(user,UserDTO.class);
    }

    // In UserMapper class

    @Override
    public ProfileResponseDTO userToUpdatedProfileDTO(User user) {
        ProfileResponseDTO dto = new ProfileResponseDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setBio(user.getBio());
        dto.setProfilePictureURL(user.getProfilePictureURL());

        // Convert Skill entities to their names
        List<String> skillNames = user.getSkills().stream()
                .map(Skill::getName)
                .collect(Collectors.toList());
        dto.setSkills(skillNames);

        return dto;
    }
}
