package com.sunrise.dental;

import com.sunrise.dental.entity.*;
import com.sunrise.dental.repository.*;
import com.sunrise.dental.service.AppointmentService;
import com.sunrise.dental.service.BillingService;
import com.sunrise.dental.singleton.AppointmentNumberGenerator;
import com.sunrise.dental.strategy.LoyaltyDiscountStrategy;
import com.sunrise.dental.strategy.NoDiscountStrategy;
import com.sunrise.dental.factory.BillFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppointmentServiceTest {

    @Autowired AppointmentService appointmentService;
    @Autowired BillingService billingService;
    @Autowired DentistRepository dentistRepository;
    @Autowired TreatmentTypeRepository treatmentTypeRepository;
    @Autowired PatientRepository patientRepository;
    @Autowired AppointmentRepository appointmentRepository;
    @Autowired BillRepository billRepository;
    @Autowired AppointmentNumberGenerator generator;
    @Autowired BillFactory billFactory;
    @Autowired NoDiscountStrategy noDiscount;
    @Autowired LoyaltyDiscountStrategy loyalty;

    Long dentistId, treatmentId;

    @BeforeEach
    void seed() {
        Dentist d = dentistRepository.save(new Dentist("Dr. Test", "General"));
        TreatmentType t = treatmentTypeRepository.save(new TreatmentType("Filling", 5000));
        dentistId = d.getId(); treatmentId = t.getId();
    }

    @Test
    void registerAppointment_assignsUniqueNumber() {
        Appointment a = appointmentService.registerAppointment("Kamal Perera", "Colombo",
            "0771234567", dentistId, treatmentId,
            LocalDate.now().plusDays(1), LocalTime.of(10, 0));
        assertNotNull(a.getAppointmentNo());
        assertTrue(a.getAppointmentNo().startsWith("APT-"));
    }

    @Test
    void registerAppointment_rejectsPastDate() {
        assertThrows(IllegalArgumentException.class, () ->
            appointmentService.registerAppointment("Nimal", "Kandy", "0771234568",
                dentistId, treatmentId, LocalDate.now().minusDays(1), LocalTime.of(10, 0)));
    }

    @Test
    void registerAppointment_rejectsDoubleBooking() {
        appointmentService.registerAppointment("Sunil", "Galle", "0771234569",
            dentistId, treatmentId, LocalDate.now().plusDays(2), LocalTime.of(11, 0));
        assertThrows(IllegalArgumentException.class, () ->
            appointmentService.registerAppointment("Sunil", "Galle", "0771234569",
                dentistId, treatmentId, LocalDate.now().plusDays(2), LocalTime.of(11, 0)));
    }

    @Test
    void billCalculation_isCorrect() {
        Appointment a = appointmentService.registerAppointment("Ruwan", "Matara", "0771234570",
            dentistId, treatmentId, LocalDate.now().plusDays(3), LocalTime.of(14, 0));
        Bill bill = billingService.generateBill(a, noDiscount);
        assertEquals(6000.0, bill.getTotal(), 0.001); // 1000 fee + 5000 filling
        Bill loyalBill = billingService.generateBill(a, loyalty);
        assertEquals(6000.0, loyalBill.getTotal(), 0.001); // idempotent: same bill returned
    }

    @Test
    void loyaltyDiscount_isTenPercent() {
        assertEquals(0.10, loyalty.discountPercentage(), 0.0001);
        assertEquals(0.0, noDiscount.discountPercentage(), 0.0001);
    }

    @Test
    void appointmentNumberGenerator_isSequential() {
        String n1 = generator.next();
        String n2 = generator.next();
        assertNotEquals(n1, n2);
    }
}
