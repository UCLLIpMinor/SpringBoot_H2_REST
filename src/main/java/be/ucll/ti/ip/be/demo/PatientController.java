package be.ucll.ti.ip.be.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/overview")
    public String showPatientOverview(Model model) {
        model.addAttribute("patients", patientService.getAll());
        model.addAttribute("sortedpatients", patientService.getAllSortedByName());
        model.addAttribute("adultpatients", patientService.getAllAdults());
        return "overview-patient";
    }

    @GetMapping("/add")
    public String addForm(Patient patient) {
        return "add-patient";
    }

    @PostMapping("/add")
    public String addPatient(@Valid Patient patient, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-patient";
        }
        try {
            patientService.add(patient);
        } catch (ServiceException exc) {
            model.addAttribute("emailError", exc.getMessage());
            return "add-patient";
        }
        return "redirect:/overview";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        try {
            Patient patient = patientService.findPatientById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
            model.addAttribute("patient", patient);
        } catch (IllegalArgumentException exc) {
            model.addAttribute("error", exc.getMessage());
        }
        return "update-patient";
    }

    @PostMapping("/update/{id}")
    public String updatePatient(@PathVariable("id") long id, @Valid Patient patient,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "update-patient";
        }
        try {
            patientService.update(patient);
        } catch (ServiceException e) {
            model.addAttribute("emailError", e.getMessage());
            return "update-patient";
        }
        return "redirect:/overview";
    }

}
