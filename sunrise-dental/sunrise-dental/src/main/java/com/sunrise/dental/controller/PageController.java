package com.sunrise.dental.controller;

import com.sunrise.dental.dto.AppointmentForm;
import com.sunrise.dental.entity.*;
import com.sunrise.dental.repository.DentistRepository;
import com.sunrise.dental.repository.TreatmentTypeRepository;
import com.sunrise.dental.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class PageController {
    private final AppointmentService appointmentService;
    private final BillingService billingService;
    private final ReportService reportService;
    private final DentistRepository dentistRepository;
    private final TreatmentTypeRepository treatmentTypeRepository;

    public PageController(AppointmentService appointmentService, BillingService billingService,
                          ReportService reportService, DentistRepository dentistRepository,
                          TreatmentTypeRepository treatmentTypeRepository) {
        this.appointmentService = appointmentService;
        this.billingService = billingService;
        this.reportService = reportService;
        this.dentistRepository = dentistRepository;
        this.treatmentTypeRepository = treatmentTypeRepository;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("today", appointmentService.todaysAppointments());
        model.addAttribute("report", reportService.buildDailyReport(LocalDate.now()));
        return "dashboard";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("form", new AppointmentForm());
        model.addAttribute("dentists", dentistRepository.findAll());
        model.addAttribute("treatments", treatmentTypeRepository.findAll());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@Valid @ModelAttribute("form") AppointmentForm form,
                                 BindingResult binding, Model model,
                                 RedirectAttributes redirect) {
        model.addAttribute("dentists", dentistRepository.findAll());
        model.addAttribute("treatments", treatmentTypeRepository.findAll());
        if (binding.hasErrors()) return "register";

        // Extra time-slot business validation (dentists work 09:00-17:00, 30-min slots)
        LocalTime t = form.getTime();
        if (t.isBefore(LocalTime.of(9, 0)) || t.isAfter(LocalTime.of(16, 30)) || t.getMinute() % 30 != 0) {
            binding.rejectValue("time", "slot", "Time must be a 30-minute slot between 09:00 and 16:30");
            return "register";
        }
        try {
            Appointment appt = appointmentService.registerAppointment(
                form.getPatientName(), form.getAddress(), form.getContactNo(),
                form.getDentistId(), form.getTreatmentTypeId(), form.getDate(), form.getTime());
            redirect.addFlashAttribute("success",
                "Appointment registered successfully. Appointment number: " + appt.getAppointmentNo());
            return "redirect:/register";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("businessError", ex.getMessage());
            return "register";
        }
    }

    @GetMapping("/search")
    public String searchPage() { return "search"; }

    @GetMapping("/search/result")
    public String searchResult(@RequestParam String appointmentNo, Model model) {
        try {
            model.addAttribute("appointment", appointmentService.findByAppointmentNo(appointmentNo));
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "search";
    }

    @GetMapping("/bill")
    public String billPage(@RequestParam(required = false) String appointmentNo, Model model) {
        if (appointmentNo != null && !appointmentNo.isBlank()) {
            try {
                Appointment appt = appointmentService.findByAppointmentNo(appointmentNo);
                model.addAttribute("appointment", appt);
                model.addAttribute("existingBill",
                    billingService.findByAppointmentNoSafe(appointmentNo));
            } catch (IllegalArgumentException ex) {
                model.addAttribute("error", ex.getMessage());
            }
        }
        return "bill";
    }

    @PostMapping("/bill")
    public String generateBill(@RequestParam String appointmentNo,
                               @RequestParam(defaultValue = "false") boolean loyalty,
                               Model model) {
        try {
            Appointment appt = appointmentService.findByAppointmentNo(appointmentNo);
            Bill bill = billingService.generateBill(appt, loyalty);
            model.addAttribute("appointment", appt);
            model.addAttribute("bill", bill);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
        }
        return "bill";
    }

    @GetMapping("/help")
    public String help() { return "help"; }

    @GetMapping("/reports")
    public String reports(@RequestParam(required = false) String date, Model model) {
        LocalDate d = (date == null || date.isBlank()) ? LocalDate.now() : LocalDate.parse(date);
        model.addAttribute("report", reportService.buildDailyReport(d));
        return "reports";
    }
}
