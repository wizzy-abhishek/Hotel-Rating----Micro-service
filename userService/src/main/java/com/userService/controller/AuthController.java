package com.userService.controller;

import com.userService.dto.LoginCreds;
import com.userService.dto.LoginResponseDTO;
import com.userService.dto.SignUp;
import com.userService.services.authentication.AuthLoginService;
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
    private final AuthLoginService authLoginService ;

    public AuthController(AuthService authService, AuthLoginService authLoginService) {
        this.authService = authService;
        this.authLoginService = authLoginService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<LoginResponseDTO> createUser(@RequestBody SignUp users){
        LoginResponseDTO userCreated = authService.signUp(users);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginCreds loginCreds){
        LoginResponseDTO loginToken = authLoginService.login(loginCreds);
        return ResponseEntity.ok(loginToken);
    }
}
