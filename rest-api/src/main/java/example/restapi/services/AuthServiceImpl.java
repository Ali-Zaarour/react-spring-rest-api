package example.restapi.services;

import example.restapi.dto.AppUserDTO;
import example.restapi.entity.AppUser;
import example.restapi.payload.SignupRequest;
import example.restapi.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService{

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder argon2PasswordEncoder;

    @Autowired
    public AuthServiceImpl(AppUserRepository appUserRepository, PasswordEncoder argon2PasswordEncoder) {
        this.appUserRepository = appUserRepository;
        this.argon2PasswordEncoder = argon2PasswordEncoder;
    }

    @Override
    public Optional<AppUserDTO> createUser(SignupRequest signupRequest) {
        var appUser = new AppUser();
        appUser.setUsername(signupRequest.getUsername());
        appUser.setPassword(argon2PasswordEncoder.encode(signupRequest.getPassword()));
        AppUser createdUser  = appUserRepository.save(appUser);
        return Optional.ofNullable(AppUserDTO.builder()
                .id(createdUser.getId())
                .username(createdUser.getUsername())
                .build());
    }
}
