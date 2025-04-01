package com.userService.services;

import com.userService.entities.Users;

import java.util.List;

public interface UserService {

    Users saveUser(Users users);

    List<Users> getAllUser();

    Users getUser(String userId);


}
