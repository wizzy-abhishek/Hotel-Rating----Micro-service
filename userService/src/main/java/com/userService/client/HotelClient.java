package com.userService.client;

import com.userService.dto.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("HOTEL-SERVICE")
public interface HotelClient {

    @GetMapping("/hotel/user/{userId}")
    List<Hotel> getHotelByUserId(@PathVariable String userId);
}
