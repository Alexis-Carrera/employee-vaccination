package ec.alexis.kruger.employeevaccination.employee.model;

import ec.alexis.kruger.employeevaccination.api.AuditModel;
import lombok.*;

import javax.persistence.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    private String description;

}