package ec.alexis.kruger.employeevaccination.vaccine.model;

import ec.alexis.kruger.employeevaccination.api.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vaccine_type")
public class VaccineType extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String vaccineName;

    @NotNull
    private Integer dosageNumber;

}
