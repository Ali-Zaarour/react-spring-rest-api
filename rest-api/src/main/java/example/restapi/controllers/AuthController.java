package example.restapi.controllers;

import example.restapi.dto.AppUserDTO;
import example.restapi.exception.config.ErrorStruct;
import example.restapi.exception.config.ExceptionMessageConstant;
import example.restapi.payload.LoginCredentials;
import example.restapi.payload.SignupRequest;
import example.restapi.services.AuthService;
import example.restapi.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/uni-api/auth")
@Tag(name ="Auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            description = "Signup endpoint",
            summary = "Create new application user",
            responses = {
                @ApiResponse(
                        description = "Success / user created",
                        responseCode = "200",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = AppUserDTO.class))),
                @ApiResponse(
                        description = "Conflict / incorrect info",
                        responseCode = "409",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ErrorStruct.class))),
                @ApiResponse(
                        description = "Unauthorized / payload data validation problem",
                        responseCode = "401",
                        content = @Content)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SignupRequest.class))))
    @PostMapping("/signup")
    public ResponseEntity<AppUserDTO> signup(@Valid @RequestBody SignupRequest signupRequest){
        var createdUser = authService.createUser(signupRequest);
        return createdUser.map(ResponseEntity::ok)
                .orElseThrow(() -> new DataIntegrityViolationException(ExceptionMessageConstant.DATA_INTEGRITY_VIOLATION));
    }

    @Operation(
            description = "Login endpoint",
            summary = "Gain access to uni-api by providing valid credentials",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppUserDTO.class)),
                            headers = @Header(
                                    name ="token",
                                    description = "user jwt access token",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(
                            description = "Unauthorized / payload data validation problem",
                            responseCode = "401",
                            content = @Content),
                    @ApiResponse(
                            description = "Forbidden / user still disabled",
                            responseCode = "403",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorStruct.class)))
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginCredentials.class))))
    @PostMapping("/login")
    public ResponseEntity<AppUserDTO> login(@Valid @RequestBody LoginCredentials loginCredentials){
        var loginToken = authService.login(loginCredentials);
        return loginToken.map(loginData -> ResponseEntity.status(HttpStatus.OK)
                .header(Constants.SECURITY_ATTRIBUTE_TOKEN, (String) loginData.get(Constants.SECURITY_ATTRIBUTE_TOKEN))
                .contentType(MediaType.APPLICATION_JSON)
                .body((AppUserDTO)loginData.get(Constants.APP_USER_DTO))).orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }


}
