package com.skillscout.model.mapper.impl;

import com.skillscout.model.DTO.ProfileResponseDTO;
import com.skillscout.model.DTO.UserDTO;
import com.skillscout.model.entity.User;
import com.skillscout.model.mapper.UserMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

    @Override
    public ProfileResponseDTO userToUpdatedProfileDTO(User user){
        return mapper.map(user,ProfileResponseDTO.class);
    }
}
