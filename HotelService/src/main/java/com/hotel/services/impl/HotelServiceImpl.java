package com.hotel.services.impl;

import com.hotel.entities.Hotel;
import com.hotel.entities.Ratings;
import com.hotel.exception.ResourceException;
import com.hotel.repo.HotelRepo;
import com.hotel.services.HotelService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {

   /* @Value("${GET_RATING_BY_HOTEl_ID}")
    private String GET_RATING_BY_HOTEl_ID;

    @Value("${GET_RATING_BY_USER_ID}")
    private String GET_RATING_BY_USER_ID;*/

    private final HotelRepo hotelRepo ;
    private final RestClient restClient ;
    private final Logger logger = LoggerFactory.getLogger("Hotel Service Impl");
    private final DiscoveryClient discoveryClient ;

    public HotelServiceImpl(HotelRepo hotelRepo, RestClient.Builder builder, DiscoveryClient discoveryClient) {
        this.hotelRepo = hotelRepo;
        this.restClient = builder.build();
        this.discoveryClient = discoveryClient;
    }

    @Override
    @RateLimiter(name = "writeOpsRateLimiter" , fallbackMethod = "writeOpsFallBack")
    public Hotel createHotel(Hotel hotel) {
        String hotelId = UUID.randomUUID().toString() ;
        hotel.setHotelId(hotelId);
        return hotelRepo.save(hotel);
    }

    public Hotel writeOpsFallBack(Hotel hotel , Throwable throwable){
        logger.error("Error in writing ops, rate limiter came in picture ");
        return new Hotel();
    }

    @Override
    @CircuitBreaker(name = "getAllHotelCB" ,
            fallbackMethod = "getAllHotelFB")
    public List<Hotel> getAllHotel(){

        List<Hotel> hotels = hotelRepo.findAll();
        ServiceInstance serviceInstance = discoveryClient.getInstances("RATING-SERVICE").get(0);
        return hotels.stream()
                .peek(
                        hotelsRating -> {

                            String url = serviceInstance.getUri() + "/rating/hotel/" + hotelsRating.getHotelId() ;

                            ParameterizedTypeReference<List<Ratings>> responseType = new ParameterizedTypeReference<>() {};

                            List<Ratings> ratingOfAHotel = restClient.get()
                                    .uri(url)
                                    .retrieve()
                                    .body(responseType);

                            logger.info("Method name : getAllHotel\n Rating's {}", ratingOfAHotel);

                            hotelsRating.setRatings(ratingOfAHotel);
                        }).toList();
    }

    public List<Hotel> getAllHotelFB(Throwable throwable){
        logger.error("Circuit breaker called for getAllHotel \n Cause : {}" , throwable.getMessage());
        return List.of();
    }

    @Override
    public Hotel getHotelById(String hotelId) {
        Hotel hotel =  hotelRepo.findById(hotelId)
                .orElseThrow(() -> new ResourceException("Hotel with id " + hotelId + " not found"));

        ParameterizedTypeReference<List<Ratings>> responseType = new ParameterizedTypeReference<>() {};
        ServiceInstance serviceInstance = discoveryClient.getInstances("RATING-SERVICE").get(0);
        List<Ratings> ratingOfAHotel = restClient.get()
                .uri( serviceInstance.getUri() + "/rating/hotel/" + hotel.getHotelId())
                .retrieve()
                .body(responseType);

        logger.info("Method name : getHotelById\n Rating's {}", ratingOfAHotel);

        hotel.setRatings(ratingOfAHotel);

        return hotel ;
    }

    @Override
    @CircuitBreaker(name = "getHotelByUserIdCB"
            , fallbackMethod = "getHotelByUserIdFB")
    public List<Hotel> getHotelByUserId(String userId) {

        ParameterizedTypeReference<List<Ratings>> responseType = new ParameterizedTypeReference<>() {};
        ServiceInstance serviceInstance = discoveryClient.getInstances("RATING-SERVICE").get(0);
        List<Ratings> ratingsByUser = restClient.get()
                .uri(serviceInstance.getUri() + "/rating/user/" + userId)
                .retrieve()
                .body(responseType);

        List<String> hotelsId = ratingsByUser.stream()
                .map(Ratings::hotelId)
                .toList();

        logger.info("Method name : getHotelByUserId\n HotelId's {}", hotelsId);

        List<Hotel> hotels = hotelRepo.findAllById(hotelsId);

        Map<String, List<Ratings>> ratingsMap = ratingsByUser.stream()
                .collect(Collectors.groupingBy(Ratings::hotelId));

        hotels.forEach(hotel -> {
            List<Ratings> hotelRatings = ratingsMap.getOrDefault(hotel.getHotelId(), new ArrayList<>());
            hotel.setRatings(hotelRatings);
        });

        return hotels ;
    }

    public List<Hotel> getHotelByUserIdFB(String userId , Throwable throwable){
        logger.error("Circuit breaker called while calling hotel of user-id : {} \nError {}" , userId , throwable.getMessage());
        return List.of();
    }
}
