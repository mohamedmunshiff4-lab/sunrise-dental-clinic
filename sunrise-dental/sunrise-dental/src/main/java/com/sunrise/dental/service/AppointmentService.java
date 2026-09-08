package com.sunrise.dental.service;

import com.sunrise.dental.entity.*;
import com.sunrise.dental.repository.*;
import com.sunrise.dental.singleton.AppointmentNumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepository;
    private final TreatmentTypeRepository treatmentTypeRepository;
    private final AppointmentNumberGenerator numberGenerator;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DentistRepository dentistRepository,
                              TreatmentTypeRepository treatmentTypeRepository,
                              AppointmentNumberGenerator numberGenerator) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.dentistRepository = dentistRepository;
        this.treatmentTypeRepository = treatmentTypeRepository;
        this.numberGenerator = numberGenerator;
    }

    /**
     * Registers a new appointment with full validation:
     *  - past dates are rejected
     *  - double booking for the same dentist/date/time is rejected
     *  - unknown dentist or treatment type is rejected
     * @return the saved Appointment with a generated appointment number
     */
    @Transactional
    public Appointment registerAppointment(String patientName, String address, String contactNo,
                                           Long dentistId, Long treatmentTypeId,
                                           LocalDate date, LocalTime time) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Appointment date cannot be in the past.");
        }
        if (appointmentRepository.existsByDentistIdAndDateAndTime(dentistId, date, time)) {
            throw new IllegalArgumentException(
                "Double booking: this dentist is already booked at " + date + " " + time);
        }
        Dentist dentist = dentistRepository.findById(dentistId)
            .orElseThrow(() -> new IllegalArgumentException("Selected dentist does not exist."));
        TreatmentType treatment = treatmentTypeRepository.findById(treatmentTypeId)
            .orElseThrow(() -> new IllegalArgumentException("Selected treatment type does not exist."));

        // Reuse existing patient record when the phone number is already registered
        Patient patient = patientRepository.findByContactNo(contactNo)
            .orElseGet(() -> patientRepository.save(new Patient(patientName, address, contactNo)));

        Appointment appt = new Appointment(numberGenerator.next(), patient, dentist,
                                           treatment, date, time);
        return appointmentRepository.save(appt);
    }

    public Appointment findByAppointmentNo(String appointmentNo) {
        return appointmentRepository.findByAppointmentNo(appointmentNo.trim())
            .orElseThrow(() -> new IllegalArgumentException(
                "No appointment found with number " + appointmentNo));
    }

    public List<Appointment> todaysAppointments() {
        return appointmentRepository.findByDate(LocalDate.now());
    }

    public List<Appointment> appointmentsBetween(LocalDate from, LocalDate to) {
        return appointmentRepository.findByDateBetween(from, to);
    }
}
