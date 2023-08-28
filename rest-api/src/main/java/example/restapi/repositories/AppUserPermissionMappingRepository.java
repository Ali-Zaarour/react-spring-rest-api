package example.restapi.repositories;

import example.restapi.entity.AppUserPermissionMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AppUserPermissionMappingRepository extends JpaRepository<AppUserPermissionMapping, UUID> {
}
