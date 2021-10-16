package ec.alexis.kruger.employeevaccination.employee.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@EqualsAndHashCode
@AllArgsConstructor
public class CreateNewEmployeeResponse {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

}
