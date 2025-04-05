package com.userService.entities;


public record Rating (String ratingId ,
                      String userId ,
                      int rating ,
                      String feedback ,
                      Hotel hotel) {
}
