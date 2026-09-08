package com.sunrise.dental.service;

import com.sunrise.dental.repository.AppointmentRepository;
import com.sunrise.dental.repository.BillRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

/**
 * Produces management reports: daily schedule, revenue summary and
 * appointment statistics. These help clinic management with decisions.
 */
@Service
public class ReportService {
    private final AppointmentRepository appointmentRepository;
    private final BillRepository billRepository;

    public ReportService(AppointmentRepository appointmentRepository, BillRepository billRepository) {
        this.appointmentRepository = appointmentRepository;
        this.billRepository = billRepository;
    }

    public record DailyScheduleRow(String time, String patient, String dentist,
                                   String treatment, String status) {}

    public record Report(LocalDate date, List<DailyScheduleRow> schedule,
                         double dayRevenue, long scheduledCount, long completedCount) {}

    public Report buildDailyReport(LocalDate date) {
        List<DailyScheduleRow> schedule = appointmentRepository.findByDate(date).stream()
            .map(a -> new DailyScheduleRow(
                a.getTime().toString(), a.getPatient().getName(),
                a.getDentist().getName(), a.getTreatmentType().getName(), a.getStatus()))
            .toList();
        double revenue = billRepository.totalRevenueBetween(date, date);
        return new Report(date, schedule, revenue,
                appointmentRepository.countByStatus("SCHEDULED"),
                appointmentRepository.countByStatus("COMPLETED"));
    }
}
