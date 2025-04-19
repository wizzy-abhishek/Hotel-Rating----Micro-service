package com.userService.entities;

import com.userService.entities.enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role ;

    private String about ;

}
