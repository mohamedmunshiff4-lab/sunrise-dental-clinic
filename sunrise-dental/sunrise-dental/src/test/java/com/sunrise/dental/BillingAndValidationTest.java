package com.sunrise.dental;

import com.sunrise.dental.entity.*;
import com.sunrise.dental.repository.*;
import com.sunrise.dental.service.AppointmentService;
import com.sunrise.dental.service.BillingService;
import com.sunrise.dental.strategy.LoyaltyDiscountStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BillingAndValidationTest {

    @Autowired AppointmentService appointmentService;
    @Autowired BillingService billingService;
    @Autowired DentistRepository dentistRepository;
    @Autowired TreatmentTypeRepository treatmentTypeRepository;
    @Autowired LoyaltyDiscountStrategy loyalty;

    Long dentistId, fillingId;

    @BeforeEach
    void seed() {
        Dentist d = dentistRepository.save(new Dentist("Dr. Validator", "Testing"));
        TreatmentType t = treatmentTypeRepository.save(new TreatmentType("Filling", 5000));
        dentistId = d.getId(); fillingId = t.getId();
    }

    @Test
    void unknownDentist_isRejected() {
        assertThrows(IllegalArgumentException.class, () ->
            appointmentService.registerAppointment("Test", "Colombo", "0771000001",
                999999L, fillingId, LocalDate.now().plusDays(5), LocalTime.of(9, 30)));
    }

    @Test
    void unknownTreatmentType_isRejected() {
        assertThrows(IllegalArgumentException.class, () ->
            appointmentService.registerAppointment("Test", "Colombo", "0771000002",
                dentistId, 999999L, LocalDate.now().plusDays(5), LocalTime.of(10, 30)));
    }

    @Test
    void loyaltyBill_totalIs5400() {
        // 1000 consultation + 5000 filling = 6000; 10% loyalty discount = 600; total = 5400
        Appointment a = appointmentService.registerAppointment("Loyal Patient", "Colombo",
            "0771000003", dentistId, fillingId,
            LocalDate.now().plusDays(6), LocalTime.of(11, 30));
        Bill bill = billingService.generateBill(a, loyalty);
        assertEquals(5400.0, bill.getTotal(), 0.001);
        assertEquals(600.0, bill.getDiscount(), 0.001);
    }

    @Test
    void searchUnknownNumber_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
            appointmentService.findByAppointmentNo("APT-9999-9999"));
    }

    @Test
    void appointmentNumber_matchesExpectedFormat() {
        Appointment a = appointmentService.registerAppointment("Format Test", "Colombo",
            "0771000004", dentistId, fillingId,
            LocalDate.now().plusDays(7), LocalTime.of(13, 0));
        assertTrue(a.getAppointmentNo().matches("APT-\\d{4}-\\d{4}"));
    }
}
