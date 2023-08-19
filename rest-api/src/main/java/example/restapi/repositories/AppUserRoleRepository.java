package example.restapi.repositories;

import example.restapi.entity.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppUserRoleRepository extends JpaRepository<AppUserRole, UUID> {
    Optional<AppUserRole> findAppUserRoleById(UUID uuid);
    Optional<AppUserRole> findAppUserRoleByKey(String key);
}
