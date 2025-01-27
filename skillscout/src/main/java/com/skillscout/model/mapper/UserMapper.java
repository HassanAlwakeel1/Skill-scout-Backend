package com.skillscout.model.mapper;

import com.skillscout.model.DTO.UserDTO;
import com.skillscout.model.entity.User;

public interface UserMapper {
     UserDTO userToUserDTO(User user);
}
