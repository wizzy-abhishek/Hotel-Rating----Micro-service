package com.userService.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    @Id
    private String userId ;

    @Column(nullable = false)
    private String name ;

    @Column(nullable = false)
    private String email ;

    private String about ;

    @Transient
    private List<Rating> ratings = new ArrayList<>() ;

}
