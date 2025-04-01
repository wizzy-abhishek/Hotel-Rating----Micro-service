package com.hotel.services.impl;

import com.hotel.entities.Hotel;
import com.hotel.exception.ResourceException;
import com.hotel.repo.HotelRepo;
import com.hotel.services.HotelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HotelServiceImpl implements HotelService {

    private final HotelRepo hotelRepo ;

    public HotelServiceImpl(HotelRepo hotelRepo) {
        this.hotelRepo = hotelRepo;
    }

    @Override
    public Hotel createHotel(Hotel hotel) {
        String hotelId = UUID.randomUUID().toString() ;
        hotel.setHotelId(hotelId);
        return hotelRepo.save(hotel);
    }

    @Override
    public List<Hotel> getAllHotel() {
        return hotelRepo.findAll();
    }

    @Override
    public Hotel getHotelById(String hotelId) {
        return hotelRepo.findById(hotelId)
                .orElseThrow(() -> new ResourceException("Hotel with id " + hotelId + " not found"));
    }
}
