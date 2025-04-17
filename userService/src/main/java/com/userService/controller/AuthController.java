package com.userService.controller;

import com.userService.dto.LoginResponseDTO;
import com.userService.dto.SignUp;
import com.userService.services.authentication.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService ;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<LoginResponseDTO> createUser(@RequestBody SignUp users){
        LoginResponseDTO userCreated = authService.signUp(users);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
