package com.userService.dto;

import java.util.List;

public record Hotel(String hotelId ,
                    String name ,
                    String location ,
                    String about ,
                    List<Ratings>ratings ) {
}
