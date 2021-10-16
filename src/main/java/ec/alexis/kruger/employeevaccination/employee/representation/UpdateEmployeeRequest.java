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
public class UpdateEmployeeRequest {

    @JsonProperty("dateOfBirth")
    @NotBlank(message = "dateOfBirth is mandatory")
    private String dateOfBirth;

    @JsonProperty("address")
    @NotBlank(message = "address is mandatory")
    private String address;

    @JsonProperty("mobilePhoneNumber")
    @NotBlank(message = "mobilePhoneNumber is mandatory")
    private String mobilePhoneNumber;

    @JsonProperty("vaccineStatus")
    @NotBlank(message = "vaccineStatus is mandatory")
    private String vaccineStatus;

    @JsonProperty("employeeVaccinationInfo")
    @NotBlank(message = "employeeVaccinationInfo is mandatory")
    private VaccinationInformationRequest employeeVaccinationInfo;

}