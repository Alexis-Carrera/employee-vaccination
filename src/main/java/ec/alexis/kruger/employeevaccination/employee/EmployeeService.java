package ec.alexis.kruger.employeevaccination.employee;

import ec.alexis.kruger.employeevaccination.employee.model.Employee;
import ec.alexis.kruger.employeevaccination.employee.model.EmployeeVaccinationInfo;
import ec.alexis.kruger.employeevaccination.employee.repository.EmployeeRepository;
import ec.alexis.kruger.employeevaccination.employee.repository.EmployeeVaccinationInfoRepository;
import ec.alexis.kruger.employeevaccination.employee.representation.*;
import ec.alexis.kruger.employeevaccination.vaccine.model.VaccinationStatus;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class EmployeeService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeVaccinationInfoRepository employeeVaccinationInfoRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeVaccinationInfoRepository employeeVaccinationInfoRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeVaccinationInfoRepository = employeeVaccinationInfoRepository;
    }

    public CreateNewEmployeeResponse createNewEmployee(CreateNewEmployeeRequest request) {
        if(employeeRepository.findByNationalIdNumber(request.getNationalIdNumber()).isPresent()){
            throw new RuntimeException("Employee exists", new Exception());
        }
        if(!isEcuadorianDocumentValid(request.getNationalIdNumber())){
            throw new RuntimeException("National Id Number Wrong", new Exception());
        }
        String username = request.getNationalIdNumber();
        String password = generateRandomSpecialCharacters(8);
        Employee employee = Employee.builder()
                .names(request.getNames())
                .lastNames(request.getLastNames())
                .nationalIdNumber(request.getNationalIdNumber())
                .email(request.getEmail())
                .username(username)
                .password(password)
                .build();
        employeeRepository.save(employee);
        return CreateNewEmployeeResponse.builder()
                .username(username)
                .password(password)
                .build();
    }

    public UpdateEmployeeRequest updateEmployeeRequest(long employeeId, UpdateEmployeeRequest request){
        if(!employeeRepository.findById(employeeId).isPresent()){
            throw new RuntimeException("Employee not Exists", new Exception());
        }
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            VaccinationStatus vaccinationStatus= VaccinationStatus.valueOfDescription(request.getVaccineStatus());
            Employee employee = employeeRepository.findById(employeeId).get();

            Employee updateEmployee = Employee.builder()
                    .id(employee.getId())
                    .nationalIdNumber(employee.getNationalIdNumber())
                    .names(employee.getNames())
                    .lastNames(employee.getLastNames())
                    .email(employee.getEmail())
                    .username(employee.getUsername())
                    .password(employee.getPassword())
                    .address(request.getAddress())
                    .mobilePhoneNumber(request.getMobilePhoneNumber())
                    .dateOfBirth(df.parse(request.getDateOfBirth()))
                    .vaccinationStatus(vaccinationStatus)
                    .build();
            if(vaccinationStatus != VaccinationStatus.NO_VACCINATED){
                if(!employeeRepository.findById(employeeId).isPresent()){
                    throw new RuntimeException("Employee not Exists", new Exception());
                }
                EmployeeVaccinationInfo employeeVaccinationInfo = EmployeeVaccinationInfo.builder()
                        .vaccinationDate(df.parse(request.getEmployeeVaccinationInfo().getVaccinationDate()))
                        .dosageNumberApplied(request.getEmployeeVaccinationInfo().getDosageNumberApplied())
                        .vaccineTypeId(request.getEmployeeVaccinationInfo().getVaccineTypeId())
                        .build();
                if(employeeVaccinationInfoRepository.findByEmployeeId(employee.getId()).isPresent()){
                    employeeVaccinationInfo.setId(employeeVaccinationInfoRepository.findByEmployeeId(employee.getId()).get().getId());
                }
                employeeVaccinationInfoRepository.save(employeeVaccinationInfo);
                updateEmployee.setEmployeeVaccinationInfo(employeeVaccinationInfo);
            }
            employeeRepository.save(updateEmployee);
        } catch (ParseException e) {
            throw new RuntimeException("API ERROR", new Exception());
        }
        return request;
    }

    public RetrieveEmployeeResponse retrieveEmployeeInformation(long employeeId){
        if(!employeeRepository.findById(employeeId).isPresent()){
            throw new RuntimeException("Employee not Exists", new Exception());
        }
        try {
            Employee employee = employeeRepository.findById(employeeId).get();
            RetrieveEmployeeResponse responseEmployeeInformation = RetrieveEmployeeResponse.builder()
                    .employeeId(employee.getId().toString())
                    .username(employee.getUsername())
                    .password(employee.getPassword())
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
        } catch (Exception e) {
            throw new RuntimeException("API ERROR", e);
        }
    }

    public String deleteEmployeeInformation(long employeeId){
        if(!employeeRepository.findById(employeeId).isPresent()){
            throw new RuntimeException("Employee not Exists", new Exception());
        }
        try {
            Employee employee = employeeRepository.findById(employeeId).get();
            if(employee.getVaccinationStatus() != VaccinationStatus.NO_VACCINATED){
                if(employeeVaccinationInfoRepository.findByEmployeeId(employeeId).isPresent()){
                    EmployeeVaccinationInfo employeeVaccinationInfo = employeeVaccinationInfoRepository.findByEmployeeId(employeeId).get();
                    employeeVaccinationInfoRepository.delete(employeeVaccinationInfo);
                }
            }
            employeeRepository.delete(employee);
            return "Employee deleted";
        } catch (Exception e) {
            throw new RuntimeException("API ERROR", e);
        }
    }

    public String generateRandomSpecialCharacters(int length) {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(64,122)
                .build();
        return pwdGenerator.generate(length);
    }

    private boolean isEcuadorianDocumentValid(String document) {
        byte sum = 0;
        try {
            if (document.trim().length() != 10)
                return false;
            String[] data = document.split("");
            byte verifier = Byte.parseByte(data[0] + data[1]);
            if (verifier < 1 || verifier > 24)
                return false;
            byte[] digits = new byte[data.length];
            for (byte i = 0; i < digits.length; i++)
                digits[i] = Byte.parseByte(data[i]);
            if (digits[2] > 6)
                return false;
            for (byte i = 0; i < digits.length - 1; i++) {
                if (i % 2 == 0) {
                    verifier = (byte) (digits[i] * 2);
                    if (verifier > 9)
                        verifier = (byte) (verifier - 9);
                } else
                    verifier = (byte) (digits[i] * 1);
                sum = (byte) (sum + verifier);
            }
            if ((sum - (sum % 10) + 10 - sum) == digits[9])
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByUsername(username);
        if (employee == null)
            throw new UsernameNotFoundException("Invalid username or password.");
        Set<GrantedAuthority> grantedAuthorities = employee.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleType().toString().toUpperCase()))
                .collect(Collectors.toSet());
        return new User(employee.getUsername(), employee.getPassword(), grantedAuthorities);
    }
}