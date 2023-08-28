package example.restapi.repositories;

import example.restapi.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findAppUsersByUsername (String username);
    boolean existsAppUserByUsername(String username);
    @Query("select u.id from AppUser u where u.username = :username")
    Optional<UUID> findAppUserIdByUsername(String username);

    @Modifying
    @Query("update AppUser as appUser set appUser.active = :value where appUser.username = :username")
    void updateAppUserActiveField(String username, boolean value);
}
