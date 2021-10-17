package ec.alexis.kruger.employeevaccination.login;

import ec.alexis.kruger.employeevaccination.login.representation.LoginRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface LoginApi {

    @PostMapping(value = "/login",
            produces = {"application/json"},
            consumes = {"application/json"})
    void createNewEmployee(@Valid @RequestBody LoginRequest request);

}
