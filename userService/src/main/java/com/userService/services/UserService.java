package com.userService.services;

import com.userService.dto.UserResponseDTO;
import com.userService.entities.Users;

import java.util.List;

public interface UserService {

    List<UserResponseDTO> getAllUser();

    UserResponseDTO getUser(String userId);


}
