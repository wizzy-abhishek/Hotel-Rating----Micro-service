package com.rating.services.impl;

import com.rating.entities.Hotel;
import com.rating.entities.Rating;
import com.rating.entities.RatingWithHotel;
import com.rating.repo.RatingRepo;
import com.rating.services.RatingService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepo ratingRepo ;
    private final RestClient restClient;

    public RatingServiceImpl(RatingRepo ratingRepo, RestClient restClient) {
        this.ratingRepo = ratingRepo;
        this.restClient = restClient;
    }

    @Override
    public Rating createRating(Rating rating) {
        return ratingRepo.save(rating);
    }

    @Override
    public List<RatingWithHotel> getAllRating() {
        List<Rating> allRatings = ratingRepo.findAll();

        return allRatings.stream()
                .map(rating -> {
                    String GET_HOTEL_API = "http://localhost:8082/hotel/" + rating.getHotelId() ;
                    Hotel hotel = restClient.get()
                            .uri(GET_HOTEL_API)
                            .retrieve()
                            .body(Hotel.class);

                    return new RatingWithHotel(rating.getRatingId() ,
                            rating.getUserId() ,
                            rating.getHotelId() ,
                            rating.getRating(),
                            rating.getFeedback(),
                            hotel);
                }).toList();
    }

    @Override
    public List<RatingWithHotel> getAllRatingByUserId(String userId) {
        List<Rating> userRatings = ratingRepo.findAllByUserId(userId);

        return userRatings.stream()
                .map(rating -> {
                    String GET_HOTEL_API = "http://localhost:8082/hotel/" + rating.getHotelId() ;
                    Hotel hotel = restClient.get()
                            .uri(GET_HOTEL_API)
                            .retrieve()
                            .body(Hotel.class);
                    System.out.println(hotel.hotelId());
                    return new RatingWithHotel(rating.getRatingId() ,
                            rating.getUserId() ,
                            rating.getHotelId() ,
                            rating.getRating(),
                            rating.getFeedback(),
                            hotel);

                }).toList();
    }

    @Override
    public List<RatingWithHotel> getAllRatingByHotelId(String hotelId) {
        List<Rating> hotelRatings = ratingRepo.findAllByHotelId(hotelId);

        return hotelRatings.stream()
                .map(rating -> {
                    String GET_HOTEL_API = "http://localhost:8082/hotel/" + rating.getHotelId() ;
                    Hotel hotel = restClient.get()
                            .uri(GET_HOTEL_API)
                            .retrieve()
                            .body(Hotel.class);

                    return new RatingWithHotel(rating.getRatingId() ,
                            rating.getUserId() ,
                            rating.getHotelId() ,
                            rating.getRating(),
                            rating.getFeedback(),
                            hotel);
                }).toList();
    }
}
