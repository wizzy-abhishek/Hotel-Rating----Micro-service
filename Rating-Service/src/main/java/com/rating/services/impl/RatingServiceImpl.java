package com.rating.services.impl;

import com.rating.entities.Rating;
import com.rating.repo.RatingRepo;
import com.rating.services.RatingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepo ratingRepo ;

    public RatingServiceImpl(RatingRepo ratingRepo) {
        this.ratingRepo = ratingRepo;
    }

    @Override
    public Rating createRating(Rating rating) {
        return ratingRepo.save(rating);
    }

    @Override
    public List<Rating> getAllRating() {
        return ratingRepo.findAll() ;
    }

    @Override
    public List<Rating> getAllRatingByUserId(String userId) {
        return ratingRepo.findAllByUserId(userId);
    }

    @Override
    public List<Rating> getAllRatingByHotelId(String hotelId) {
        return ratingRepo.findAllByHotelId(hotelId);
    }
}
