package com.userService.services.authentication;

import com.userService.dto.LoginCreds;
import com.userService.dto.LoginResponseDTO;

public interface AuthLoginService {

    LoginResponseDTO login(LoginCreds loginCreds);
}
