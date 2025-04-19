package com.userService.services.impl;

import com.userService.client.HotelClient;
import com.userService.dto.Hotel;
import com.userService.dto.Ratings;
import com.userService.dto.UserResponseDTO;
import com.userService.entities.Users;
import com.userService.exceptions.ResourceException;
import com.userService.repo.UserRepo;
import com.userService.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Value("${GET_HOTEL_BY_USER_ID_API}")
    private String GET_HOTEL_BY_USER_ID_API ;

    private final UserRepo userRepo;
    private final RestClient restClient ;
    private final DiscoveryClient discoveryClient ;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final HotelClient hotelClient ;

    public UserServiceImpl(UserRepo userRepo,
                           RestClient.Builder builder, DiscoveryClient discoveryClient, HotelClient hotelClient) {
        this.userRepo = userRepo;
        this.restClient = builder.build();
        this.discoveryClient = discoveryClient;
        this.hotelClient = hotelClient;
    }

    @Override
    @CircuitBreaker(name = "getAllUserCircuitBreaker" , fallbackMethod = "getAllUserFallBackCB")
    public List<UserResponseDTO> getAllUser() {
        logger.info("get all user called");
        List<Users> users = userRepo.findAll();
        List<UserResponseDTO> userResponse = new ArrayList<>();
        users.forEach(user -> {

            // <List<Hotel>> responseType = new ParameterizedTypeReference<>() {};

            List<Hotel> hotelsByUser = hotelClient.getHotelByUserId(user.getUserId());

                    /* restClient.get()
                    .uri(GET_HOTEL_BY_USER_ID_API + user.getUserId())
                    .retrieve()
                    .body(responseType);
                    */

            logger.info("Method : getAllUser\n Get_hotel_api_call : {}", hotelsByUser);

            userResponse.add(getUserResponseDTO(user , hotelsByUser));
        });

        return userResponse;
    }

    public List<UserResponseDTO> getAllUserFallBackCB(Throwable throwable) {
        logger.error("get all user not working \n\"CB CALLED \": {}" , throwable.getMessage());
        return List.of();
    }

    @Override
    @CircuitBreaker(name ="getUserCB" , fallbackMethod = "getUserFB")
    public UserResponseDTO getUser(String userId) {

        Users users = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceException("User not found with id " + userId));

        // ParameterizedTypeReference<List<Hotel>> responseType = new ParameterizedTypeReference<>() {};



        List<Hotel> hotels = hotelClient.getHotelByUserId(users.getUserId());

                /* restClient.get()
                .uri(GET_HOTEL_BY_USER_ID_API + user.getUserId())
                .retrieve()
                .body(responseType);
                */

        UserResponseDTO user = getUserResponseDTO(users , hotels);

        logger.info("Method : getUser\n Get_hotel_api_call : {}", hotels);

        return user ;
    }

    public UserResponseDTO getUserFB(String userId , Throwable throwable) {
        logger.error("getUser method not responded while calling for user-id : {} \n came error {}" , userId , throwable.getMessage());
        return new UserResponseDTO("dummy-data",
                throwable.getMessage() ,
                List.of());
    }

    private UserResponseDTO getUserResponseDTO(Users user , List<Hotel> ratings){
        return new UserResponseDTO(user.getName() , user.getAbout(), ratings);
    }
}
