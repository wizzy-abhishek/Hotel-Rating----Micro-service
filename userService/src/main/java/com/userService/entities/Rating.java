package com.userService.entities;

public record Rating (String ratingId ,
                      String userId ,
                      String hotelId,
                      int rating ,
                      String feedback ) {
}
