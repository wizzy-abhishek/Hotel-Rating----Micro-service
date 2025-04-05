package com.rating.services;

import com.rating.entities.Rating;
import com.rating.entities.RatingWithHotel;

import java.util.List;

public interface RatingService {

    Rating createRating(Rating rating);

    List<RatingWithHotel> getAllRating();

    List<RatingWithHotel> getAllRatingByUserId(String userId);

    List<RatingWithHotel> getAllRatingByHotelId(String hotelId);
}

