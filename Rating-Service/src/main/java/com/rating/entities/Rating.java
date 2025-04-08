package com.rating.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "user_rating")
public class Rating {

    @Id
    private String ratingId ;

    private String userId  ;

    private String hotelId ;

    private int rating  ;

    private String feedback ;
}
