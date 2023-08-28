package example.restapi.services;

import example.restapi.dto.AppUserAuthoritiesDTO;
import example.restapi.payload.UserAuthorityRequest;

import java.util.Optional;
import java.util.UUID;

public interface AuthorityService {
    Optional<AppUserAuthoritiesDTO> findAppUserAuthoritiesByUsername(String username);
    Optional<String> createOrUpdateOneUserAuthorities(UserAuthorityRequest userAuthorityRequest, UUID userId);
}
