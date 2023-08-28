package example.restapi.entity;

import example.restapi.entity.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "app_user_role_mapping")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppUserRoleMapping extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "role_id",referencedColumnName = "id")
    private AppUserRole appUserRole;

    @OneToMany(mappedBy = "appUserRoleMapping",fetch = FetchType.EAGER)
    private List<AppUserPermissionMapping> appUserPermissionMappings;


    @Override
    public String toString(){
        return "Role:'"+ appUserRole.getKey() + "', Permission " +appUserPermissionMappings.stream()
                .map(permission -> permission.getAppUserPermission().getKey())
                .toList();
    }

}
