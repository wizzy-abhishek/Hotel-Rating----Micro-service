package com.userService.services.impl;

import com.userService.entities.Hotel;
import com.userService.entities.Users;
import com.userService.exceptions.ResourceException;
import com.userService.repo.UserRepo;
import com.userService.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Value("${GET_HOTEL_BY_USER_ID_API}")
    private String GET_HOTEL_BY_USER_ID_API ;

    private final UserRepo userRepo;
    private final RestClient restClient ;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepo userRepo,
                          RestClient restClient) {
        this.userRepo = userRepo;
        this.restClient = restClient;
    }

    @Override
    public Users saveUser(Users user) {
        String uniqueId = UUID.randomUUID().toString() ;
        user.setUserId(uniqueId);
        return userRepo.save(user);
    }

    @Override
    public List<Users> getAllUser() {
        List<Users> users = userRepo.findAll();

        users.forEach(user -> {
            ParameterizedTypeReference<List<Hotel>> responseType = new ParameterizedTypeReference<>() {};

            List<Hotel> hotelsByUser = restClient.get()
                    .uri(GET_HOTEL_BY_USER_ID_API + user.getUserId())
                    .retrieve()
                    .body(responseType);

            user.setHotelsRated(hotelsByUser);
        });

        return users;
    }

    @Override
    public Users getUser(String userId) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceException("User not found with id " + userId));

        ParameterizedTypeReference<List<Hotel>> responseType = new ParameterizedTypeReference<>() {};
        List<Hotel> hotels = restClient.get()
                .uri(GET_HOTEL_BY_USER_ID_API + user.getUserId())
                .retrieve()
                .body(responseType);

        user.setHotelsRated(hotels);


        return user ;
    }
}
