package ec.alexis.kruger.employeevaccination.employee.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Data
@EqualsAndHashCode
@Validated
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveEmployeeResponse {

    @JsonProperty("employeeId")
    private String employeeId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("nationalIdNumber")
    private String nationalIdNumber;

    @JsonProperty("names")
    private String names;

    @JsonProperty("lastNames")
    private String lastNames;

    @JsonProperty("email")
    private String email;

    @JsonProperty("dateOfBirth")
    private String dateOfBirth;

    @JsonProperty("address")
    private String address;

    @JsonProperty("mobilePhoneNumber")
    private String mobilePhoneNumber;

    @JsonProperty("vaccineStatus")
    private String vaccineStatus;

    @JsonProperty("employeeVaccinationInfo")
    private VaccinationInformationRequest employeeVaccinationInfo;
}
