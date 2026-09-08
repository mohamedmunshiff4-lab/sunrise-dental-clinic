package com.sunrise.dental.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointments", uniqueConstraints =
    @UniqueConstraint(columnNames = {"dentist_id", "date", "time"}))
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String appointmentNo; // e.g. APT-2024-0001 (auto-generated)

    @ManyToOne(optional = false)
    private Patient patient;

    @ManyToOne(optional = false)
    private Dentist dentist;

    @ManyToOne(optional = false)
    private TreatmentType treatmentType;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private String status; // SCHEDULED, COMPLETED, CANCELLED

    public Appointment() {}

    public Appointment(String appointmentNo, Patient patient, Dentist dentist,
                       TreatmentType treatmentType, LocalDate date, LocalTime time) {
        this.appointmentNo = appointmentNo;
        this.patient = patient;
        this.dentist = dentist;
        this.treatmentType = treatmentType;
        this.date = date;
        this.time = time;
        this.status = "SCHEDULED";
    }

    public Long getId() { return id; }
    public String getAppointmentNo() { return appointmentNo; }
    public Patient getPatient() { return patient; }
    public Dentist getDentist() { return dentist; }
    public TreatmentType getTreatmentType() { return treatmentType; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
