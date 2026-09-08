package com.sunrise.dental.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "dentists")
public class Dentist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Dentist name is required")
    private String name;

    private String specialization;

    public Dentist() {}

    public Dentist(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
}
