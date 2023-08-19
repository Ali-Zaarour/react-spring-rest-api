package example.restapi.services;

import example.restapi.dto.AppUserDTO;
import example.restapi.entity.AppUser;
import example.restapi.entity.AppUserRole;
import example.restapi.payload.LoginCredentials;
import example.restapi.payload.SignupRequest;
import example.restapi.repositories.AppUserRepository;
import example.restapi.utils.Constants;
import example.restapi.utils.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService{

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder argon2PasswordEncoder;
    private final JWTUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(AppUserRepository appUserRepository, PasswordEncoder argon2PasswordEncoder, JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.appUserRepository = appUserRepository;
        this.argon2PasswordEncoder = argon2PasswordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Optional<AppUserDTO> createUser(SignupRequest signupRequest) {
        //check if user already exist
        var exist = appUserRepository.existsAppUserByUsername(signupRequest.getUsername());
        if (!exist) {
            var appUser = AppUser.builder()
                    .username(signupRequest.getUsername())
                    .password(argon2PasswordEncoder.encode(signupRequest.getPassword()))
                    .firstName(signupRequest.getFirstName())
                    .lastName(signupRequest.getLastName())
                    .birthDate(signupRequest.getBirthDate())
                    .address(signupRequest.getAddress())
                    .phoneNumber(signupRequest.getPhoneNumber())
                    //set by defaults for now //todo set role
                    .role(AppUserRole.builder().id(UUID.fromString("17c07bd1-3407-47c6-979e-4ceb92613770")).build())
                    .build();
            var createdUser = appUserRepository.save(appUser);
            return Optional.ofNullable(AppUserDTO.builder()
                    .id(createdUser.getId())
                    .username(createdUser.getUsername())
                    .build());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Map<String, Object>> login(LoginCredentials loginCredentials) {
        //validate provided username and password
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginCredentials.getUsername(), loginCredentials.getPassword()));
        //get app user information
        var appUser = appUserRepository.findAppUsersByUsername(loginCredentials.getUsername());
        if (appUser.isPresent()){
            String token = jwtUtil.generateJWTToken(appUser.get());
            return Optional.of(new HashMap<>() {{
                put(Constants.APP_USER_DTO, new AppUserDTO(appUser.get().getId(), appUser.get().getUsername()));
                put(Constants.SECURITY_ATTRIBUTE_TOKEN, token);
            }});
        }
        return Optional.empty();
    }
}
