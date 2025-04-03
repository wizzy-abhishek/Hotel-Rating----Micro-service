package com.userService.services.impl;

import com.userService.entities.Users;
import com.userService.exceptions.ResourceException;
import com.userService.repo.UserRepo;
import com.userService.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Users saveUser(Users user) {
        String uniqueId = UUID.randomUUID().toString() ;
        user.setUserId(uniqueId);
        return userRepo.save(user);
    }

    @Override
    public List<Users> getAllUser() {
        return userRepo.findAll();
    }

    @Override
    public Users getUser(String userId) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceException("User not found with id " + userId));
        // http://localhost:8083/rating/user/{userId}

        return user ;
    }
}
