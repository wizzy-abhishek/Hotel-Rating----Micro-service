package com.userService.services.impl;

import com.userService.entities.Rating;
import com.userService.entities.Users;
import com.userService.exceptions.ResourceException;
import com.userService.repo.UserRepo;
import com.userService.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RestTemplate restTemplate ;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepo userRepo,
                           RestTemplate restTemplate) {
        this.userRepo = userRepo;
        this.restTemplate = restTemplate;
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
        String url = "http://localhost:8083/rating/user/" + userId;

        ResponseEntity<List<Rating>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Rating>>() {});

        List<Rating> ratings = response.getBody();

        logger.info("Ratings fetched: {}", ratings);
        user.setRatings(ratings);
        return user ;
    }
}
