package com.sunrise.dental.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalTime;

/** Input model for the Register New Appointment form, with validation rules. */
public class AppointmentForm {

    @NotBlank(message = "Patient name is required")
    @Size(min = 3, max = 100, message = "Name must be 3-100 characters")
    private String patientName;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "\\d{10}", message = "Contact number must be exactly 10 digits")
    private String contactNo;

    @NotNull(message = "Please select a dentist")
    private Long dentistId;

    @NotNull(message = "Please select a treatment type")
    private Long treatmentTypeId;

    @NotNull(message = "Appointment date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @FutureOrPresent(message = "Appointment date cannot be in the past")
    private LocalDate date;

    @NotNull(message = "Appointment time is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime time;

    // getters/setters
    public String getPatientName() { return patientName; }
    public void setPatientName(String v) { patientName = v; }
    public String getAddress() { return address; }
    public void setAddress(String v) { address = v; }
    public String getContactNo() { return contactNo; }
    public void setContactNo(String v) { contactNo = v; }
    public Long getDentistId() { return dentistId; }
    public void setDentistId(Long v) { dentistId = v; }
    public Long getTreatmentTypeId() { return treatmentTypeId; }
    public void setTreatmentTypeId(Long v) { treatmentTypeId = v; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate v) { date = v; }
    public LocalTime getTime() { return time; }
    public void setTime(LocalTime v) { time = v; }
}
