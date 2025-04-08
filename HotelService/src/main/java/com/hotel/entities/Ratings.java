package com.hotel.entities;

public record Ratings(String ratingId ,
                      String userId  ,
                      String hotelId ,
                      int rating  ,
                      String feedback ) {
}
