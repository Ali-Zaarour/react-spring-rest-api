package example.restapi.repositories;

import example.restapi.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findAppUsersByUsername (String username);
    boolean existsAppUserByUsername(String username);
}
