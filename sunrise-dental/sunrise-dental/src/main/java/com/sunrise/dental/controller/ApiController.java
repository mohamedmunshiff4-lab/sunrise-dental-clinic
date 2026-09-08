package com.sunrise.dental.controller;

import com.sunrise.dental.dto.AppointmentForm;
import com.sunrise.dental.entity.*;
import com.sunrise.dental.service.*;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * REST web services layer. The system is distributed: these JSON endpoints
 * can be consumed by the Thymeleaf UI, a mobile app, or any other client
 * over HTTP.
 */
@RestController
@RequestMapping("/api")
public class ApiController {
    private final AppointmentService appointmentService;
    private final BillingService billingService;
    private final ReportService reportService;
    private final AuthService authService;

    public ApiController(AppointmentService appointmentService, BillingService billingService,
                         ReportService reportService, AuthService authService) {
        this.appointmentService = appointmentService;
        this.billingService = billingService;
        this.reportService = reportService;
        this.authService = authService;
    }

    /** Authentication service for external clients (returns a simple token). */
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> creds) {
        return authService.login(creds.get("username"), creds.get("password"))
            .map(s -> ResponseEntity.ok(Map.of(
                "status", "OK", "username", s.getUsername(), "role", s.getRole())))
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("status", "FAILED", "message", "Invalid credentials")));
    }

    @GetMapping("/appointments/{appointmentNo}")
    public ResponseEntity<?> getAppointment(@PathVariable String appointmentNo) {
        try {
            return ResponseEntity.ok(appointmentService.findByAppointmentNo(appointmentNo));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/appointments")
    public ResponseEntity<?> createAppointment(@Valid @RequestBody AppointmentForm form) {
        try {
            Appointment saved = appointmentService.registerAppointment(
                form.getPatientName(), form.getAddress(), form.getContactNo(),
                form.getDentistId(), form.getTreatmentTypeId(), form.getDate(), form.getTime());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/appointments")
    public List<Appointment> appointmentsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return appointmentService.appointmentsBetween(date, date);
    }

    @PostMapping("/bills")
    public ResponseEntity<?> createBill(@RequestBody Map<String, Object> body) {
        try {
            Appointment appt = appointmentService.findByAppointmentNo((String) body.get("appointmentNo"));
            boolean loyalty = Boolean.TRUE.equals(body.get("loyalty"));
            return ResponseEntity.ok(billingService.generateBill(appt, loyalty));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/reports/daily")
    public ResponseEntity<?> dailyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(reportService.buildDailyReport(date));
    }
}
