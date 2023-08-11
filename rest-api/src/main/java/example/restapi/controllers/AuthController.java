package example.restapi.controllers;

import example.restapi.dto.AppUserDTO;
import example.restapi.payload.SignupRequest;
import example.restapi.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/uni-api/auth")
public class AuthController {

    private final AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/signup")
    public ResponseEntity<Optional<AppUserDTO>> createUser(@Valid @RequestBody SignupRequest signupRequest){
        var createUser = authService.createUser(signupRequest);
        if (createUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(createUser);
    }


}
