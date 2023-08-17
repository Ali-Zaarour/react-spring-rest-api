package example.restapi.controllers;

import example.restapi.dto.AppUserDTO;
import example.restapi.payload.LoginCredentials;
import example.restapi.payload.SignupRequest;
import example.restapi.services.AuthService;
import example.restapi.utils.Constants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/uni-api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AppUserDTO> signup(@Valid @RequestBody SignupRequest signupRequest){
        var createdUser = authService.createUser(signupRequest);
        return createdUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .header(Constants.ERROR_ATTRIBUTE_X_ERROR_MESSAGE,"User already exists.")
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<AppUserDTO> login(@Valid @RequestBody LoginCredentials loginCredentials){
        var loginToken = authService.login(loginCredentials);
        return loginToken.map(loginData -> ResponseEntity.status(HttpStatus.OK)
                .header(Constants.SECURITY_ATTRIBUTE_TOKEN, (String) loginData.get(Constants.SECURITY_ATTRIBUTE_TOKEN))
                .contentType(MediaType.APPLICATION_JSON)
                .body((AppUserDTO)loginData.get(Constants.APP_USER_DTO))).orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }


}
