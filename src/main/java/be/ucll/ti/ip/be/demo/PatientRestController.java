package be.ucll.ti.ip.be.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

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
    public Iterable<Patient> addPatient(@Valid @RequestBody Patient patient){
        try {
            patientService.add(patient);
        }
        catch (ServiceException exc) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Email already exists", exc);
        }
        return patientService.getAll();
    }

    @DeleteMapping("/delete/{id}")
    public void deletePatient(@PathVariable("id") long id) {
        patientService.deletePatientById(id);
    }

}
