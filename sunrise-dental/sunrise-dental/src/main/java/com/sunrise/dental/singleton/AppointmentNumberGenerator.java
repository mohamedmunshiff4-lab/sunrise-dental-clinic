package com.sunrise.dental.singleton;

import com.sunrise.dental.repository.AppointmentRepository;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SINGLETON PATTERN: exactly one generator exists in the JVM (default Spring
 * singleton bean). It hands out unique appointment numbers such as APT-2024-0001.
 * AtomicLong makes number generation thread-safe for concurrent web requests.
 */
@Component
public class AppointmentNumberGenerator {
    private final AtomicLong counter = new AtomicLong(0);

    public AppointmentNumberGenerator(AppointmentRepository appointmentRepository) {
        // Resume from the highest number already stored, so restarts never reuse a number
        appointmentRepository.findAll().stream()
            .map(a -> a.getAppointmentNo())
            .filter(n -> n != null && n.matches("APT-\\d{4}-\\d+"))
            .mapToLong(n -> Long.parseLong(n.substring(n.lastIndexOf('-') + 1)))
            .max().ifPresent(m -> counter.set(m));
    }

    public synchronized String next() {
        return String.format("APT-%d-%04d", java.time.Year.now().getValue(), counter.incrementAndGet());
    }
}
