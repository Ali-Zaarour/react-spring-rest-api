package example.restapi.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class UserAuthorityRequest {

    @JsonProperty("username")
    @NotNull
    private String username;

    @JsonProperty("authority")
    @Size(min = 1, max = 3, message ="Role IDs should have 1 to 3 elements")
    private List<AuthDTO> authDTOS;


    @Getter
    public static class AuthDTO {

        @JsonProperty("roleId")
        @NotNull
        private UUID roleId;

        @JsonProperty("permissionIds")
        @NotNull
        private List<UUID> permissionIds;

    }
}
