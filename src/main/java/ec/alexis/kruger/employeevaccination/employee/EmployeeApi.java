package ec.alexis.kruger.employeevaccination.employee;

import ec.alexis.kruger.employeevaccination.employee.representation.CreateNewEmployeeRequest;
import ec.alexis.kruger.employeevaccination.employee.representation.CreateNewEmployeeResponse;
import ec.alexis.kruger.employeevaccination.employee.representation.RetrieveEmployeeResponse;
import ec.alexis.kruger.employeevaccination.employee.representation.UpdateEmployeeRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface EmployeeApi {

    @PostMapping(value = "/employee",
            produces = {"application/json"},
            consumes = {"application/json"})
    CreateNewEmployeeResponse createNewEmployee(@Valid @RequestBody CreateNewEmployeeRequest request);


    @PatchMapping(value = "/employee/{id}",
            produces = {"application/json"},
            consumes = {"application/json"})
    UpdateEmployeeRequest updateEmployeeRequest(@PathVariable("id") long employeeId, @Valid @RequestBody UpdateEmployeeRequest request);

    @GetMapping(value = "/employee/{id}",
            produces = {"application/json"},
            consumes = {"application/json"})
    RetrieveEmployeeResponse retrieveEmployeeInformation(@PathVariable("id") long employeeId);


}
