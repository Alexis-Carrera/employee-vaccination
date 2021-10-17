package ec.alexis.kruger.employeevaccination.employee.repository;

import ec.alexis.kruger.employeevaccination.employee.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "SELECT * from role where role_Type IN (:roles)", nativeQuery = true)
    Set<Role> find(@Param("roles") List<String> roles);

}
