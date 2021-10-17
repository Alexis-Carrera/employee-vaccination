package ec.alexis.kruger.employeevaccination.employee.repository;

import ec.alexis.kruger.employeevaccination.employee.model.Employee;
import ec.alexis.kruger.employeevaccination.vaccine.model.VaccinationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByNationalIdNumber(String nationalIdNumber);

    Employee findByUsername(String username);

    Optional<List<Employee>> findAllByVaccinationStatus(VaccinationStatus vaccinationStatus);


}
