package com.userService.services.authentication;

import com.userService.dto.LoginResponseDTO;
import com.userService.dto.SignUp;
import com.userService.entities.Users;
import com.userService.entities.auth.UserPrinciple;
import com.userService.entities.enums.Roles;
import com.userService.jwt.JwtService;
import com.userService.repo.UserRepo;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements UserDetailsService, AuthService{

    private final UserRepo userRepo ;
    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final JwtService jwtService ;
    private final PasswordEncoder passwordEncoder ;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserPrinciple(user);
    }

    @Transactional(rollbackFor = {Exception.class , RuntimeException.class})
    @RateLimiter(name = "writeOpsRateLimiter" , fallbackMethod = "writeOpsFallBack")
    public LoginResponseDTO signUp(SignUp signUp){

        Users user = userRepo.findByEmailIgnoreCase(signUp.email())
                .orElse(null);

        if (user != null){
            throw new BadCredentialsException("Email already present " + signUp.email());
        }
        String userId = UUID.randomUUID().toString();
        Users toBeCreatedUser = new Users(userId ,
                signUp.name(),
                signUp.email() ,
                signUp.password() ,
                Roles.valueOf(signUp.role()) ,
                signUp.about(),
                null);

        toBeCreatedUser.setPassword(passwordEncoder.encode(toBeCreatedUser.getPassword()));
        Users savingUser = userRepo.save(toBeCreatedUser);

        String token = jwtService.generateAccessToken(savingUser);

        return new LoginResponseDTO(token);
    }

    public LoginResponseDTO writeOpsFallBack(SignUp user , Throwable throwable){
        logger.error("Error in writing ops, rate limiter came in picture ");
        return new LoginResponseDTO(throwable.getMessage());
    }


}
