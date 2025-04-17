package com.userService.dto;

public record SignUp(String name ,
                     String email ,
                     String password,
                     String role ,
                     String about) {
}
