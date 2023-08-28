package example.restapi.services;

import example.restapi.dto.AppUserAuthoritiesDTO;
import example.restapi.entity.*;
import example.restapi.exception.SQLBatchOperationException;
import example.restapi.exception.config.ExceptionMessageConstant;
import example.restapi.mapper.AppUserMapper;
import example.restapi.payload.UserAuthorityRequest;
import example.restapi.repositories.AppUserPermissionMappingRepository;
import example.restapi.repositories.AppUserRepository;
import example.restapi.repositories.AppUserRoleMappingRepository;
import jakarta.persistence.EntityManager;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorityServiceImpl implements AuthorityService{

    private final AppUserRoleMappingRepository appUserRoleMappingRepository;

    private final AppUserPermissionMappingRepository appUserPermissionMappingRepository;

    private final AppUserRepository appUserRepository;

    @Autowired
    public AuthorityServiceImpl(AppUserRoleMappingRepository appUserRoleMappingRepository, AppUserPermissionMappingRepository appUserPermissionMappingRepository, AppUserRepository appUserRepository) {
        this.appUserRoleMappingRepository = appUserRoleMappingRepository;
        this.appUserPermissionMappingRepository = appUserPermissionMappingRepository;
        this.appUserRepository = appUserRepository;
    }


    @Override
    public Optional<AppUserAuthoritiesDTO> findAppUserAuthoritiesByUsername(String username) {
        var appUser = appUserRepository.findAppUsersByUsername(username);
        return appUser.map(AppUserMapper.MAPPER::appUserToAppUserAuthoritiesDTO);
    }

    @Override
    @Transactional
    public Optional<String> createOrUpdateOneUserAuthorities(@NonNull UserAuthorityRequest userAuthorityRequest, UUID principalId) {
        //ToDo: update methode to care about update or create data later
        //ToDo: save principal id responsible for adding this role and permission (create column, add process)

        // 1. validate username -> get AppUser.id
        var appUserId = appUserRepository.findAppUserIdByUsername(userAuthorityRequest.getUsername())
                .orElseThrow(()->new UsernameNotFoundException(ExceptionMessageConstant.USERNAME_NOT_FOUND));

        // 2. create user role - > AppUserRoleMapping
        try {
            var appUser = AppUser.builder().id(appUserId).build();
            List<AppUserRoleMapping> appUserRoleMappingList = userAuthorityRequest.getAuthDTOS().stream()
                    .map(authDTO ->
                            AppUserRoleMapping.builder()
                                    .appUser(appUser)
                                    .appUserRole(AppUserRole.builder().id(authDTO.getRoleId()).build())
                                    .build()
                    ).toList();
            var roleMappingsResult = appUserRoleMappingRepository.saveAll(appUserRoleMappingList);

            // 3. create permission -> map base on request data between role and permission
            List<Map.Entry<UUID, UUID>> rolePermissionPairs = userAuthorityRequest.getAuthDTOS().stream()
                    .flatMap(authDTO -> authDTO.getPermissionIds().stream().map(permissionId -> Map.entry(authDTO.getRoleId(), permissionId)))
                    .toList();

            List<AppUserPermissionMapping> userPermissionMappingList = rolePermissionPairs.stream()
                    .map(entry -> {
                        //match request and saved role id to set permission
                        var matchFilter = roleMappingsResult.stream().filter(appUserRoleMapping -> appUserRoleMapping.getAppUserRole().getId() == entry.getKey());
                        //map every permission to it's role
                        return AppUserPermissionMapping.builder()
                                .appUserRoleMapping(matchFilter.toList().get(0))
                                .appUserPermission(AppUserPermission.builder().id(entry.getValue()).build())
                                .build();
                    }).toList();

            appUserPermissionMappingRepository.saveAll(userPermissionMappingList);

            //4. activate user profile
            appUserRepository.updateAppUserActiveField(userAuthorityRequest.getUsername(), true);

        } catch (Exception exception){
            throw new SQLBatchOperationException();
        }

        return Optional.ofNullable(userAuthorityRequest.getUsername());
    }
}
