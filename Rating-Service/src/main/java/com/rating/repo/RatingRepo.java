package com.rating.repo;

import com.rating.entities.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepo extends MongoRepository<Rating , String> {

    List<Rating> findAllByUserId(String userId);

    List<Rating> findAllByHotelId(String hotelId);

    List<Rating> findByHotelIdAndUserId(String hotelId, String userId);
}
