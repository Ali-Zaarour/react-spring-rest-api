package example.restapi.controllers;

import example.restapi.dto.AppUserAuthoritiesDTO;
import example.restapi.entity.AppUser;
import example.restapi.exception.config.ErrorStruct;
import example.restapi.exception.config.ExceptionMessageConstant;
import example.restapi.payload.UserAuthorityRequest;
import example.restapi.services.AuthorityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/uni-api/authorities")
@Tag(name = "Set Authority")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AuthorityController {

    private final AuthorityService authorityService;

    @Autowired
    public  AuthorityController(AuthorityService authorityService){
        this.authorityService = authorityService;
    }

    @Operation(
            description = "Get User Authorities By Username",
            summary =  "Get user Authorities",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppUserAuthoritiesDTO.class))),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorStruct.class))),
                    @ApiResponse(
                            description = "Unauthorized / payload data validation problem",
                            responseCode = "401",
                            content = @Content)
            },
            parameters = @Parameter(
                    name = "username",
                    in = ParameterIn.PATH,
                    description = "username of appUser",
                    required = true,
                    example = "myExample@domain.com",
                    content = @Content(schema = @Schema(implementation = String.class)))
    )
    @GetMapping("/{username}")
    public ResponseEntity<AppUserAuthoritiesDTO> finByUsername(@NonNull @PathVariable @NotBlank String username){
        return authorityService.findAppUserAuthoritiesByUsername(username)
                .map(ResponseEntity::ok)
                .orElseThrow(()-> new EntityNotFoundException(ExceptionMessageConstant.ENTITY_NOT_FOUND));
    }

    @Operation(
            description = "Set User Authorities",
            summary = "Admin set user Authorities access level",
            responses = {
                    @ApiResponse(
                            description = "Created",
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppUserAuthoritiesDTO.class))),
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
                            schema = @Schema(implementation = UserAuthorityRequest.class))))
    @PostMapping
    public ResponseEntity<Void> setSingleUserAuthorities(@Valid @RequestBody UserAuthorityRequest userAuthorityRequest, @AuthenticationPrincipal @NonNull AppUser userDetails,UriComponentsBuilder uriComponentsBuilder){
        var appUserUsername = authorityService.createOrUpdateOneUserAuthorities(userAuthorityRequest, userDetails.getId());
        URI locationOfNewAuthorities = uriComponentsBuilder
                .path("/uni-api/authorities/{username}")
                .buildAndExpand(appUserUsername.get())
                .toUri();
        return ResponseEntity.created(locationOfNewAuthorities).build();
    }
}
