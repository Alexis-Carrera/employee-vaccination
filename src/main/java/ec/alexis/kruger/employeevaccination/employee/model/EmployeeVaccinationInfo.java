package ec.alexis.kruger.employeevaccination.employee.model;

import ec.alexis.kruger.employeevaccination.api.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee_vaccination_info")
public class EmployeeVaccinationInfo extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long vaccineTypeId;

    private Integer dosageNumberApplied;

    @Temporal(TemporalType.DATE)
    private Date vaccinationDate;

    @OneToOne(mappedBy = "employeeVaccinationInfo")
    private Employee employee;
}
