package ec.alexis.kruger.employeevaccination.employee.model;

import ec.alexis.kruger.employeevaccination.api.AuditModel;
import ec.alexis.kruger.employeevaccination.vaccine.model.VaccinationStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
@ToString(exclude = {"employeeVaccinationInfo"})
public class Employee extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = "^[A-Za-z]+$")
    private String names;

    @NotNull
    @Pattern(regexp = "^[A-Za-z]+$")
    private String lastNames;

    @Column(unique = true)
    @NotNull
    @Size(min = 10, max = 10)
    private String nationalIdNumber;

    @NotNull
    @Pattern(regexp = "^(.+)@(.+)$")
    private String email;

    @Column(unique = true)
    @NotNull
    private String username;

    @NotNull
    private String password;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    private String address;

    private String  mobilePhoneNumber;

    private VaccinationStatus vaccinationStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_vaccination_info_id", referencedColumnName = "id")
    private EmployeeVaccinationInfo employeeVaccinationInfo;

}
