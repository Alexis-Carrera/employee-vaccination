package ec.alexis.kruger.employeevaccination.employee;

import ec.alexis.kruger.employeevaccination.employee.representation.CreateNewEmployeeRequest;
import ec.alexis.kruger.employeevaccination.employee.representation.CreateNewEmployeeResponse;
import ec.alexis.kruger.employeevaccination.employee.representation.RetrieveEmployeeResponse;
import ec.alexis.kruger.employeevaccination.employee.representation.UpdateEmployeeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController implements EmployeeApi{

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public CreateNewEmployeeResponse createNewEmployee(CreateNewEmployeeRequest request) {
        return employeeService.createNewEmployee(request);
    }

    @Override
    public UpdateEmployeeRequest updateEmployeeRequest(long employeeId, UpdateEmployeeRequest request) {
        return employeeService.updateEmployeeRequest(employeeId, request);
    }

    @Override
    public RetrieveEmployeeResponse retrieveEmployeeInformation(long employeeId) {
        return employeeService.retrieveEmployeeInformation(employeeId);
    }

    @Override
    public String deleteEmployeeInformation(long employeeId) {
        return employeeService.deleteEmployeeInformation(employeeId);
    }

    @Override
    public List<RetrieveEmployeeResponse> retrieveEmployeeList(Map<String, String> allParams) {
        return employeeService.retrieveEmployeeList(allParams);
    }
}
