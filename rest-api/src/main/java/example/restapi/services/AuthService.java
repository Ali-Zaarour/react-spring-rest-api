package example.restapi.services;

import example.restapi.dto.AppUserDTO;
import example.restapi.payload.LoginCredentials;
import example.restapi.payload.SignupRequest;

import java.util.Map;
import java.util.Optional;

public interface AuthService {
    Optional<AppUserDTO> createUser(SignupRequest signupRequest);
    Optional<Map<String,Object>> login(LoginCredentials loginCredentials);
}
