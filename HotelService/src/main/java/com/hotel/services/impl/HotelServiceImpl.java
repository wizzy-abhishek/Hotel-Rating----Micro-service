package com.hotel.services.impl;

import com.hotel.entities.Hotel;
import com.hotel.entities.Ratings;
import com.hotel.exception.ResourceException;
import com.hotel.repo.HotelRepo;
import com.hotel.services.HotelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${GET_RATING_BY_HOTEl_ID}")
    private String GET_RATING_BY_HOTEl_ID;

    @Value("${GET_RATING_BY_USER_ID}")
    private String GET_RATING_BY_USER_ID;

    private final HotelRepo hotelRepo ;
    private final RestClient restClient ;
    private final Logger logger = LoggerFactory.getLogger("Hotel Service Impl");

    public HotelServiceImpl(HotelRepo hotelRepo, RestClient.Builder builder) {
        this.hotelRepo = hotelRepo;
        this.restClient = builder.build();
    }

    @Override
    public Hotel createHotel(Hotel hotel) {
        String hotelId = UUID.randomUUID().toString() ;
        hotel.setHotelId(hotelId);
        return hotelRepo.save(hotel);
    }

    @Override
    public List<Hotel> getAllHotel() {

        List<Hotel> hotels = hotelRepo.findAll();

        return hotels.stream()
                .peek(
                        hotelsRating -> {

                            String url = GET_RATING_BY_HOTEl_ID + hotelsRating.getHotelId() ;

                            ParameterizedTypeReference<List<Ratings>> responseType = new ParameterizedTypeReference<>() {};

                            List<Ratings> ratingOfAHotel = restClient.get()
                                    .uri(url)
                                    .retrieve()
                                    .body(responseType);

                            logger.info("Method name : getAllHotel\n Rating's {}", ratingOfAHotel);

                            hotelsRating.setRatings(ratingOfAHotel);
                        }).toList();
    }

    @Override
    public Hotel getHotelById(String hotelId) {
        Hotel hotel =  hotelRepo.findById(hotelId)
                .orElseThrow(() -> new ResourceException("Hotel with id " + hotelId + " not found"));

        ParameterizedTypeReference<List<Ratings>> responseType = new ParameterizedTypeReference<>() {};

        List<Ratings> ratingOfAHotel = restClient.get()
                .uri(GET_RATING_BY_HOTEl_ID + hotel.getHotelId())
                .retrieve()
                .body(responseType);

        logger.info("Method name : getHotelById\n Rating's {}", ratingOfAHotel);

        hotel.setRatings(ratingOfAHotel);

        return hotel ;
    }

    @Override
    public List<Hotel> getHotelByUserId(String userId) {

        ParameterizedTypeReference<List<Ratings>> responseType = new ParameterizedTypeReference<>() {};
        List<Ratings> ratingsByUser = restClient.get()
                .uri(GET_RATING_BY_USER_ID + userId)
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
}
