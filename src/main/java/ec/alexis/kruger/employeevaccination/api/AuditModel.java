package ec.alexis.kruger.employeevaccination.api;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditModel {
}
