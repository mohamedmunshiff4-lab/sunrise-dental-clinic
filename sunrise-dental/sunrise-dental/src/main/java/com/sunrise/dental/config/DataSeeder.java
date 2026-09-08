package com.sunrise.dental.config;

import com.sunrise.dental.entity.*;
import com.sunrise.dental.repository.*;
import com.sunrise.dental.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Seeds the database with an admin login, dentists and the treatment price list on first run. */
@Component
public class DataSeeder implements CommandLineRunner {
    private final StaffRepository staffRepository;
    private final DentistRepository dentistRepository;
    private final TreatmentTypeRepository treatmentTypeRepository;

    public DataSeeder(StaffRepository staffRepository,
                      DentistRepository dentistRepository,
                      TreatmentTypeRepository treatmentTypeRepository) {
        this.staffRepository = staffRepository;
        this.dentistRepository = dentistRepository;
        this.treatmentTypeRepository = treatmentTypeRepository;
    }

    @Override
    public void run(String... args) {
        if (staffRepository.count() == 0) {
            staffRepository.save(new Staff("admin", AuthService.hash("admin123"), "ADMIN"));
            staffRepository.save(new Staff("reception1", AuthService.hash("welcome1"), "RECEPTIONIST"));
        }
        if (dentistRepository.count() == 0) {
            dentistRepository.save(new Dentist("Dr. Nadeesha Perera", "Orthodontics"));
            dentistRepository.save(new Dentist("Dr. Kasun Fernando", "Endodontics"));
            dentistRepository.save(new Dentist("Dr. Dilani Silva", "General Dentistry"));
        }
        if (treatmentTypeRepository.count() == 0) {
            treatmentTypeRepository.save(new TreatmentType("Consultation", 1000));
            treatmentTypeRepository.save(new TreatmentType("Cleaning & Polishing", 3000));
            treatmentTypeRepository.save(new TreatmentType("Filling", 5000));
            treatmentTypeRepository.save(new TreatmentType("Root Canal", 15000));
            treatmentTypeRepository.save(new TreatmentType("Tooth Extraction", 4000));
            treatmentTypeRepository.save(new TreatmentType("Teeth Whitening", 12000));
        }
    }
}
