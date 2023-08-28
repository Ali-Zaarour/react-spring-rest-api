package example.restapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUserAuthoritiesDTO {

    @JsonProperty("userId")
    private UUID id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("authorities")
    private Map<String,List<String>> authorities;
}
