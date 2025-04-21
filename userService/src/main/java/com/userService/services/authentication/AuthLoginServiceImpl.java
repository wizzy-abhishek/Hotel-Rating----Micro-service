package com.userService.services.authentication;

import com.userService.dto.LoginCreds;
import com.userService.dto.LoginResponseDTO;
import com.userService.entities.Users;
import com.userService.entities.auth.UserPrinciple;
import com.userService.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthLoginServiceImpl implements AuthLoginService {

    private final AuthenticationManager authenticationManager ;
    private final JwtService jwtService ;

    public AuthLoginServiceImpl(AuthenticationManager authenticationManager,
                                JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponseDTO login(LoginCreds loginCreds){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginCreds.email() , loginCreds.password())
        );

        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        Users users = userPrinciple.getUsers();

        String token = jwtService.generateAccessToken(users);

        return new LoginResponseDTO(token);

    }
}
