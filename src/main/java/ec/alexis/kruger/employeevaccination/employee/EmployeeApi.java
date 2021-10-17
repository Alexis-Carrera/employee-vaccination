package ec.alexis.kruger.employeevaccination.employee;

import ec.alexis.kruger.employeevaccination.employee.representation.CreateNewEmployeeRequest;
import ec.alexis.kruger.employeevaccination.employee.representation.CreateNewEmployeeResponse;
import ec.alexis.kruger.employeevaccination.employee.representation.RetrieveEmployeeResponse;
import ec.alexis.kruger.employeevaccination.employee.representation.UpdateEmployeeRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface EmployeeApi {

    String ROLE_ADMIN = "ROLE_ADMIN";
    String ROLE_USER = "ROLE_USER";

    @Secured({ ROLE_ADMIN })
    @PostMapping(value = "/employee",
            produces = {"application/json"},
            consumes = {"application/json"})
    CreateNewEmployeeResponse createNewEmployee(@Valid @RequestBody CreateNewEmployeeRequest request);

    @Secured({ ROLE_ADMIN, ROLE_USER })
    @PatchMapping(value = "/employee/{id}",
            produces = {"application/json"},
            consumes = {"application/json"})
    UpdateEmployeeRequest updateEmployeeRequest(@PathVariable("id") long employeeId, @Valid @RequestBody UpdateEmployeeRequest request);

    @Secured({ ROLE_ADMIN, ROLE_USER })
    @GetMapping(value = "/employee/{id}",
            produces = {"application/json"},
            consumes = {"application/json"})
    RetrieveEmployeeResponse retrieveEmployeeInformation(@PathVariable("id") long employeeId);

    @Secured({ ROLE_ADMIN})
    @DeleteMapping(value = "/employee/{id}",
            produces = {"application/json"},
            consumes = {"application/json"})
    String deleteEmployeeInformation(@PathVariable("id") long employeeId);

}
