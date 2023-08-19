package example.restapi.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SignupRequest {
    @Email
    private String username;
    @NotNull
    @NotBlank
    private String password;
    private String firstName;
    private String lastName;
    @Past
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
}
