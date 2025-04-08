package com.userService.entities;

import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

public record Hotel(String hotelId ,
                    String name ,
                    String location ,
                    String about ,
                    List<Ratings>ratings ) {
}
