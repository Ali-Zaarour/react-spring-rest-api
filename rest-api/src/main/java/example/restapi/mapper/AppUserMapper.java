package example.restapi.mapper;

import example.restapi.dto.AppUserAuthoritiesDTO;
import example.restapi.entity.AppUser;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    AppUserMapper MAPPER = Mappers.getMapper(AppUserMapper.class);


    @Mapping(source = "appUser",target = "authorities",qualifiedByName = "setAuthorityMap")
    AppUserAuthoritiesDTO appUserToAppUserAuthoritiesDTO(AppUser appUser);

    @Named("setAuthorityMap")
    default Map<String,List<String>> setAuthorityMap(@NonNull AppUser appUser){
        return appUser.getAppUserRoleMappings().stream()
                .collect(Collectors.toMap(
                        role ->role.getAppUserRole().getKey(),
                        role -> role.getAppUserPermissionMappings().stream()
                                .map(permissionMapping -> permissionMapping.getAppUserPermission().getKey()).collect(Collectors.toList())));
    }

}
