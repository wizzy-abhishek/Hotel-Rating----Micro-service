package com.userService.services.authentication;

import com.userService.dto.LoginResponseDTO;
import com.userService.dto.SignUp;

public interface AuthService {

    LoginResponseDTO signUp(SignUp signUp);
}
