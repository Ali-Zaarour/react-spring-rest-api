package example.restapi.services;

import example.restapi.dto.AppUserDTO;
import example.restapi.entity.AppUser;
import example.restapi.payload.LoginCredentials;
import example.restapi.payload.SignupRequest;
import example.restapi.repositories.AppUserRepository;
import example.restapi.utils.Constants;
import example.restapi.utils.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService{

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder argon2PasswordEncoder;
    private final JWTUtil jwtUtil;

    @Autowired
    public AuthServiceImpl(AppUserRepository appUserRepository, PasswordEncoder argon2PasswordEncoder, JWTUtil jwtUtil) {
        this.appUserRepository = appUserRepository;
        this.argon2PasswordEncoder = argon2PasswordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Optional<AppUserDTO> createUser(SignupRequest signupRequest) {
        //check if user already exist
        var exist = appUserRepository.existsAppUserByUsername(signupRequest.getUsername());
        if (!exist) {
            var appUser = new AppUser();
            appUser.setUsername(signupRequest.getUsername());
            appUser.setPassword(argon2PasswordEncoder.encode(signupRequest.getPassword()));
            AppUser createdUser = appUserRepository.save(appUser);
            return Optional.ofNullable(AppUserDTO.builder()
                    .id(createdUser.getId())
                    .username(createdUser.getUsername())
                    .build());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Map<String, Object>> login(LoginCredentials loginCredentials) {
        //get app user information
        var appUser = appUserRepository.findAppUsersByUsername(loginCredentials.getUsername());
        if (appUser.isPresent()){
            //validate password
            boolean valid = argon2PasswordEncoder.matches(loginCredentials.getPassword(),appUser.get().getPassword());
            if (!valid) return Optional.empty();
            //generate token, set role as default for now 'ADMIN'
            String token = jwtUtil.generateJWTToken(appUser.get().getId(), appUser.get().getUsername(),"ADMIN");
            return Optional.of(new HashMap<>() {{
                put(Constants.APP_USER_DTO, new AppUserDTO(appUser.get().getId(), appUser.get().getUsername()));
                put(Constants.SECURITY_ATTRIBUTE_TOKEN, token);
            }});
        }
        return Optional.empty();
    }
}
