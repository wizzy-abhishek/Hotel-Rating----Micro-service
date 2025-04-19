package com.userService.dto;

import java.util.List;

public record UserResponseDTO(String name,
                              String about ,
                              List<Hotel> hotelsRated) {
}
