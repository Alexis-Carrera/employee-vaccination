package ec.alexis.kruger.employeevaccination.login.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode
@Validated
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @JsonProperty("username")
    @NotBlank(message = "username is mandatory")
    private String username;

    @JsonProperty("password")
    @NotBlank(message = "password is mandatory")
    private String password;

}
