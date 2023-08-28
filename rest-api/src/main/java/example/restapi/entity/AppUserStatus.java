package example.restapi.entity;

import example.restapi.entity.config.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name= "app_user_status")
@Data
@EqualsAndHashCode(callSuper = true)
public class AppUserStatus extends BaseEntity {

    private String Key;
}
