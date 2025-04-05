package com.rating.entities;

public record RatingWithHotel(String ratingId ,
                              String userId  ,
                              String hotelId ,
                              int rating  ,
                              String feedback ,
                              Hotel hotel) {
}
