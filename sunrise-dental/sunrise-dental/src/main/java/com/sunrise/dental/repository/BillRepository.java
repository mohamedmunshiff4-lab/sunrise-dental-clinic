package com.sunrise.dental.repository;

import com.sunrise.dental.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByAppointment_AppointmentNo(String appointmentNo);
    List<Bill> findByBillDateBetween(LocalDate from, LocalDate to);

    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b WHERE b.billDate BETWEEN :from AND :to")
    double totalRevenueBetween(LocalDate from, LocalDate to);
}
