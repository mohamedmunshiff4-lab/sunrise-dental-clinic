package com.sunrise.dental.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "treatment_types")
public class TreatmentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Treatment name is required")
    private String name;

    @Positive(message = "Cost must be a positive value")
    private double cost;

    public TreatmentType() {}

    public TreatmentType(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public double getCost() { return cost; }
}
