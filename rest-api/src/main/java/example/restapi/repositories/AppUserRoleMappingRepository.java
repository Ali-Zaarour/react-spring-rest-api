package example.restapi.repositories;

import example.restapi.entity.AppUserRoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AppUserRoleMappingRepository extends JpaRepository<AppUserRoleMapping, UUID> {
}
