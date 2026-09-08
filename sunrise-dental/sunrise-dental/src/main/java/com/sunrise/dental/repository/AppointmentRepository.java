package com.sunrise.dental.repository;

import com.sunrise.dental.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByAppointmentNo(String appointmentNo);
    boolean existsByDentistIdAndDateAndTime(Long dentistId, LocalDate date, java.time.LocalTime time);
    List<Appointment> findByDate(LocalDate date);
    List<Appointment> findByDateBetween(LocalDate from, LocalDate to);
    long countByStatus(String status);
}
