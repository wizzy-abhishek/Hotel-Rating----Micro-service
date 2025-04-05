package com.rating.controller;

import com.rating.entities.Rating;
import com.rating.entities.RatingWithHotel;
import com.rating.services.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rating")
public class RatingController {

    private final RatingService ratingService ;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public ResponseEntity<Rating> createRating(@RequestBody Rating rating){
        Rating rated = ratingService.createRating(rating);
        return ResponseEntity.status(HttpStatus.CREATED).body(rated);
    }

    @GetMapping
    public ResponseEntity<List<RatingWithHotel>> getAllRating(){
        return ResponseEntity.ok(ratingService.getAllRating());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RatingWithHotel>> getAllRatingByUserId(@PathVariable String userId){
        return ResponseEntity.ok(ratingService.getAllRatingByUserId(userId));
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<RatingWithHotel>> getAllRatingByHotelId(@PathVariable String hotelId){
        return ResponseEntity.ok(ratingService.getAllRatingByHotelId(hotelId));
    }

}
