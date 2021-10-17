package ec.alexis.kruger.employeevaccination.employee.parser;

import ec.alexis.kruger.employeevaccination.employee.model.Employee;
import ec.alexis.kruger.employeevaccination.employee.representation.RetrieveEmployeeResponse;
import ec.alexis.kruger.employeevaccination.employee.representation.VaccinationInformationRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeRepresentationParser {

    public List<RetrieveEmployeeResponse> transformListEmployeeResponseToEmployeeResponseList(List<Employee> employees) {
        return employees.stream()
                .map(this::transformEmployeeToResponse)
                .collect(Collectors.toList());
    }

    public RetrieveEmployeeResponse transformEmployeeToResponse(Employee employee) {
        RetrieveEmployeeResponse responseEmployeeInformation = RetrieveEmployeeResponse.builder()
                .employeeId(employee.getId().toString())
                .username(employee.getUsername())
                .nationalIdNumber(employee.getNationalIdNumber())
                .names(employee.getNames())
                .lastNames(employee.getLastNames())
                .email(employee.getEmail())
                .build();

        if(employee.getDateOfBirth() != null){
            VaccinationInformationRequest vaccinationInformationRequest = VaccinationInformationRequest.builder()
                    .vaccineTypeId(employee.getEmployeeVaccinationInfo().getVaccineTypeId())
                    .dosageNumberApplied(employee.getEmployeeVaccinationInfo().getDosageNumberApplied())
                    .vaccinationDate(employee.getEmployeeVaccinationInfo().getVaccinationDate().toString())
                    .build();
            responseEmployeeInformation.setDateOfBirth(employee.getDateOfBirth().toString());
            responseEmployeeInformation.setAddress(employee.getAddress());
            responseEmployeeInformation.setMobilePhoneNumber(employee.getMobilePhoneNumber());
            responseEmployeeInformation.setVaccineStatus(employee.getVaccinationStatus().getDescription());
            responseEmployeeInformation.setEmployeeVaccinationInfo(vaccinationInformationRequest);
        }
        return responseEmployeeInformation;
    }


}
