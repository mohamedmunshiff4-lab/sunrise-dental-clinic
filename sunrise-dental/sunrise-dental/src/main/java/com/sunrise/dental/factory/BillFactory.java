package com.sunrise.dental.factory;

import com.sunrise.dental.entity.Appointment;
import com.sunrise.dental.entity.Bill;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * FACTORY PATTERN: centralises Bill object creation. The billing service asks
 * the factory for a fully-initialised Bill instead of building one itself,
 * so bill construction rules live in exactly one place.
 */
@Component
public class BillFactory {
    private final AtomicLong seq = new AtomicLong(0);

    public Bill createBill(Appointment appointment, double consultationFee,
                           double treatmentCost, double discount, double total) {
        String billNo = "BILL-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
                        + "-" + String.format("%04d", seq.incrementAndGet());
        return new Bill(billNo, appointment, consultationFee, treatmentCost, discount, total);
    }
}
