package ec.alexis.kruger.employeevaccination.employee;

import ec.alexis.kruger.employeevaccination.employee.model.Employee;
import ec.alexis.kruger.employeevaccination.employee.model.EmployeeVaccinationInfo;
import ec.alexis.kruger.employeevaccination.employee.parser.EmployeeRepresentationParser;
import ec.alexis.kruger.employeevaccination.employee.repository.EmployeeRepository;
import ec.alexis.kruger.employeevaccination.employee.repository.EmployeeVaccinationInfoRepository;
import ec.alexis.kruger.employeevaccination.employee.repository.RoleRepository;
import ec.alexis.kruger.employeevaccination.employee.representation.*;
import ec.alexis.kruger.employeevaccination.vaccine.model.VaccinationStatus;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.OperationNotSupportedException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service
public class EmployeeService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeVaccinationInfoRepository employeeVaccinationInfoRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRepresentationParser employeeRepresentationParser;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeVaccinationInfoRepository employeeVaccinationInfoRepository, RoleRepository roleRepository, EmployeeRepresentationParser employeeRepresentationParser) {
        this.employeeRepository = employeeRepository;
        this.employeeVaccinationInfoRepository = employeeVaccinationInfoRepository;
        this.roleRepository = roleRepository;
        this.employeeRepresentationParser = employeeRepresentationParser;
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
                .password(passwordEncoder.encode(password))
                .roles(roleRepository.find(request.getRole()))
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
            return employeeRepresentationParser.transformEmployeeToResponse(employee);
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

    public List<RetrieveEmployeeResponse> retrieveEmployeeList(Map<String, String> parameters) {
        if (parameters.isEmpty()) {
            return employeeRepresentationParser.transformListEmployeeResponseToEmployeeResponseList(findAllEmployee());
        }
        List<String> allowedParameters = new ArrayList<>(Arrays.asList("vaccinationStatus", "vaccineTypeId", "rangeInit","rangeFinal"));
        if (parameters.keySet().stream()
                .filter(allowedParameters::contains)
                .noneMatch(e -> true)) {
            throw new RuntimeException("WRONG PARAMS", new Exception());
        }
        String parameterValue;
        List<Employee> results = new ArrayList<>();
        parameterValue = getStringValueFromStream("vaccinationStatus", parameters.entrySet().stream());
        if (!parameterValue.isEmpty()) {
            String finalParameterValue = parameterValue;
            VaccinationStatus vaccinationStatus = VaccinationStatus.valueOfDescription(parameterValue);
            results.addAll(retrieveEmployeeByVaccinationStatus(vaccinationStatus).orElseThrow(() ->
                    new EntityNotFoundException(MessageFormat.format("Vaccination Status not found: {0}", finalParameterValue))
            ));
        }
        parameterValue = getStringValueFromStream("vaccineTypeId", parameters.entrySet().stream());
        if (!parameterValue.isEmpty()) {
            results.addAll(retrieveEmployeeByVaccineId(parameterValue));
        }
        parameterValue = getStringValueFromStream("rangeInit", parameters.entrySet().stream());
        String parameterRangeFinal = getStringValueFromStream("rangeFinal", parameters.entrySet().stream());
        if (!parameterValue.isEmpty() || !parameterRangeFinal.isEmpty()) {
            results.addAll(retrieveEmployeeByDateBetween(parameterValue,parameterRangeFinal));
        }
        return employeeRepresentationParser.transformListEmployeeResponseToEmployeeResponseList(results);
    }

    private Optional<List<Employee>>retrieveEmployeeByVaccinationStatus(VaccinationStatus vaccinationStatus) {
        return employeeRepository.findAllByVaccinationStatus(vaccinationStatus);
    }

    private List<Employee>retrieveEmployeeByVaccineId(String parameterValue) {
        List<EmployeeVaccinationInfo> employeeVaccinationInfoList = employeeVaccinationInfoRepository.findAllByVaccineTypeId(Long.parseLong(parameterValue)).get();
        return employeeVaccinationInfoList.stream()
                .map(EmployeeVaccinationInfo::getEmployee)
                .collect(Collectors.toList());
    }

    private List<Employee>retrieveEmployeeByDateBetween(String vaccinationDateInit,String vaccinationDateFinal) {
        try{
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date vaccinationDateI =  df.parse(vaccinationDateInit);
            Date vaccinationDateF =  df.parse(vaccinationDateFinal);
            List<EmployeeVaccinationInfo> employeeVaccinationInfoList = employeeVaccinationInfoRepository.findAllByVaccinationDateBetween(vaccinationDateI,vaccinationDateF).get();
            return employeeVaccinationInfoList.stream()
                    .map(EmployeeVaccinationInfo::getEmployee)
                    .collect(Collectors.toList());
        }
        catch (Exception e){
            throw new RuntimeException("Range NOT FOUND", e);
        }
    }

    private List<Employee> findAllEmployee() {
        return employeeRepository.findAll();
    }

    private String getStringValueFromStream(String key, Stream<Map.Entry<String, String>> stream) {
        return stream.filter(element -> key.equalsIgnoreCase(element.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.joining());
    }

    private String generateRandomSpecialCharacters(int length) {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(65,90)
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