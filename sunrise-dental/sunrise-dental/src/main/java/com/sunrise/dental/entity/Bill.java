package com.sunrise.dental.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "bills")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String billNo;

    @OneToOne(optional = false)
    private Appointment appointment;

    private double consultationFee;
    private double treatmentCost;
    private double discount;
    private double total;

    private LocalDate billDate;

    public Bill() {}

    public Bill(String billNo, Appointment appointment, double consultationFee,
                double treatmentCost, double discount, double total) {
        this.billNo = billNo;
        this.appointment = appointment;
        this.consultationFee = consultationFee;
        this.treatmentCost = treatmentCost;
        this.discount = discount;
        this.total = total;
        this.billDate = LocalDate.now();
    }

    public Long getId() { return id; }
    public String getBillNo() { return billNo; }
    public Appointment getAppointment() { return appointment; }
    public double getConsultationFee() { return consultationFee; }
    public double getTreatmentCost() { return treatmentCost; }
    public double getDiscount() { return discount; }
    public double getTotal() { return total; }
    public LocalDate getBillDate() { return billDate; }
}
