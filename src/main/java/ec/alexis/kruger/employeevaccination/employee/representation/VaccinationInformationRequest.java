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
public class VaccinationInformationRequest {

    @JsonProperty("vaccineTypeId")
    private Long vaccineTypeId;

    @JsonProperty("dosageNumberApplied")
    private Integer dosageNumberApplied;

    @JsonProperty("vaccinationDate")
    private String vaccinationDate;

}
