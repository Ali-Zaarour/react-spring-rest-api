package example.restapi.entity;

import example.restapi.entity.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "app_user_permission_mapping")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppUserPermissionMapping extends BaseEntity {

    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_mapping_id", referencedColumnName = "id")
    private AppUserRoleMapping appUserRoleMapping;

    @ManyToOne
    @JoinColumn(name = "permission_id",referencedColumnName = "id")
    private AppUserPermission appUserPermission;

    @Override
    public String toString(){
        return (appUserRoleMapping.getAppUserRole().getKey()+":"+appUserPermission.getKey()).toLowerCase();
    }
}
