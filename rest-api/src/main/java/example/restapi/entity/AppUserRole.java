package example.restapi.entity;

import example.restapi.entity.config.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;


@Entity
@Table(name= "app_user_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppUserRole extends BaseEntity {

    @Builder
    public AppUserRole(UUID id, Timestamp deletedAt, String key) {
        super(id, deletedAt);
        this.Key = key;
    }

    private String Key;
}
