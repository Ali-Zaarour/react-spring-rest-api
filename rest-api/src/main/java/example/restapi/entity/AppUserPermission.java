package example.restapi.entity;

import example.restapi.entity.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "app_user_permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppUserPermission extends BaseEntity {
    @Builder
    public AppUserPermission(UUID id, Timestamp deletedAt, String key) {
        super(id, deletedAt);
        Key = key;
    }

    private String Key;
}
