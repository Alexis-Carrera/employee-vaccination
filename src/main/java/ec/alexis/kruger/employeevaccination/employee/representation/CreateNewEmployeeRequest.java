package ec.alexis.kruger.employeevaccination.employee.representation;

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
public class CreateNewEmployeeRequest {

    @JsonProperty("nationalIdNumber")
    @NotBlank(message = "nationalIdNumber is mandatory")
    private String nationalIdNumber;

    @JsonProperty("names")
    @NotBlank(message = "names is mandatory")
    private String names;

    @JsonProperty("lastNames")
    @NotBlank(message = "lastNames is mandatory")
    private String lastNames;

    @JsonProperty("email")
    @NotBlank(message = "email is mandatory")
    private String email;

}