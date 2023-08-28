package example.restapi.entity.config;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID id;

    @UpdateTimestamp
    protected Timestamp updatedAt;

    protected Timestamp deletedAt;
    @CreationTimestamp

    @Column(name = "created_at", nullable = false, updatable = false)
    protected Timestamp createAt;

    public BaseEntity(UUID id, Timestamp deletedAt) {
        this.id = id;
        this.deletedAt = deletedAt;
    }
}

