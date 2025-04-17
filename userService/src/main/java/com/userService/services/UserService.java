package com.userService.services;

import com.userService.entities.Users;

import java.util.List;

public interface UserService {

    List<Users> getAllUser();

    Users getUser(String userId);


}
