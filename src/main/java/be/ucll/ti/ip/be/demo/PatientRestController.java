package be.ucll.ti.ip.be.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/patient")
public class PatientRestController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/all")
    public Iterable<Patient> getAllPatients() {
        return patientService.getAll();
    }

    @PostMapping("/add")
    public Iterable<Patient> addPatient(@Valid @RequestBody Patient patient) {
        try {
            patientService.add(patient);
        }
        catch (ServiceException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email", exc);
        }
        return patientService.getAll();
    }

    @DeleteMapping("/delete/{id}")
    public void deletePatient(@PathVariable("id") long id) {
        patientService.deletePatientById(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, ResponseStatusException.class})
    public Map<String, String> handleValidationExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        if (ex instanceof MethodArgumentNotValidException) {
            ((MethodArgumentNotValidException)ex).getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
        }
        else {
            errors.put(((ResponseStatusException)ex).getReason(), ex.getCause().getMessage());
        }
        return errors;
    }

}
