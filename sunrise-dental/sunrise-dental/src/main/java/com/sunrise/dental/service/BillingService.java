package com.sunrise.dental.service;

import com.sunrise.dental.entity.Appointment;
import com.sunrise.dental.entity.Bill;
import com.sunrise.dental.factory.BillFactory;
import com.sunrise.dental.repository.BillRepository;
import com.sunrise.dental.strategy.DiscountStrategy;
import com.sunrise.dental.strategy.LoyaltyDiscountStrategy;
import com.sunrise.dental.strategy.NoDiscountStrategy;
import org.springframework.stereotype.Service;

@Service
public class BillingService {
    public static final double CONSULTATION_FEE = 1000.0;

    private final BillRepository billRepository;
    private final BillFactory billFactory;
    private final NoDiscountStrategy noDiscount;
    private final LoyaltyDiscountStrategy loyaltyDiscount;

    public BillingService(BillRepository billRepository, BillFactory billFactory,
                          NoDiscountStrategy noDiscount, LoyaltyDiscountStrategy loyaltyDiscount) {
        this.billRepository = billRepository;
        this.billFactory = billFactory;
        this.noDiscount = noDiscount;
        this.loyaltyDiscount = loyaltyDiscount;
    }

    /**
     * Calculates and stores the bill. The DiscountStrategy (Strategy pattern)
     * is chosen at runtime: NoDiscountStrategy for normal patients,
     * LoyaltyDiscountStrategy for regular patients.
     */
    public Bill generateBill(Appointment appointment, DiscountStrategy discountStrategy) {
        return billRepository.findByAppointment_AppointmentNo(appointment.getAppointmentNo())
            .orElseGet(() -> {
                double treatmentCost = appointment.getTreatmentType().getCost();
                double discount = (CONSULTATION_FEE + treatmentCost) * discountStrategy.discountPercentage();
                double total = CONSULTATION_FEE + treatmentCost - discount;
                return billRepository.save(
                    billFactory.createBill(appointment, CONSULTATION_FEE, treatmentCost, discount, total));
            });
    }

    /** Convenience overload - the loyalty flag picks the strategy. */
    public Bill generateBill(Appointment appointment, boolean loyalty) {
        return generateBill(appointment, loyalty ? loyaltyDiscount : noDiscount);
    }

    public Bill findByAppointmentNo(String appointmentNo) {
        return billRepository.findByAppointment_AppointmentNo(appointmentNo.trim())
            .orElseThrow(() -> new IllegalArgumentException(
                "No bill exists for appointment " + appointmentNo + " yet."));
    }

    /** Safe lookup used by the UI: returns null instead of throwing. */
    public Bill findByAppointmentNoSafe(String appointmentNo) {
        return billRepository.findByAppointment_AppointmentNo(appointmentNo.trim()).orElse(null);
    }
}
