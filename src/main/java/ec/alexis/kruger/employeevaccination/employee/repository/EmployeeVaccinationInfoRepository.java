package ec.alexis.kruger.employeevaccination.employee.repository;

import ec.alexis.kruger.employeevaccination.employee.model.EmployeeVaccinationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeVaccinationInfoRepository extends JpaRepository<EmployeeVaccinationInfo, Long> {

    Optional<EmployeeVaccinationInfo> findByEmployeeId(Long employeeId);

    Optional<List<EmployeeVaccinationInfo>> findAllByVaccineTypeId(Long vaccineTypeId);

    Optional<List<EmployeeVaccinationInfo>> findAllByVaccinationDateBetween(Date vaccinationDateInit,Date vaccinationDateFinal);

}
