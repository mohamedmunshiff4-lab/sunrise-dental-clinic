-- ============================================================
-- Sunrise Dental Clinic Management System - Database Script
-- ============================================================

CREATE DATABASE IF NOT EXISTS sunrise_dental
  CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE sunrise_dental;

-- -----------------------------------------------------------
-- Table: staff (system users)
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS staff (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password_hash VARCHAR(64)  NOT NULL,
    role          VARCHAR(20)  NOT NULL
);

-- -----------------------------------------------------------
-- Table: patients
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS patients (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    address    VARCHAR(255) NOT NULL,
    contact_no VARCHAR(10)  NOT NULL UNIQUE
);

-- -----------------------------------------------------------
-- Table: dentists
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS dentists (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(100) NOT NULL,
    specialization VARCHAR(100)
);

-- -----------------------------------------------------------
-- Table: treatment_types
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS treatment_types (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    cost DOUBLE       NOT NULL CHECK (cost > 0)
);

-- -----------------------------------------------------------
-- Table: appointments
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS appointments (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_no    VARCHAR(20) NOT NULL UNIQUE,
    patient_id        BIGINT      NOT NULL,
    dentist_id        BIGINT      NOT NULL,
    treatment_type_id BIGINT      NOT NULL,
    date              DATE        NOT NULL,
    time              TIME        NOT NULL,
    status            VARCHAR(15) NOT NULL DEFAULT 'SCHEDULED',
    CONSTRAINT fk_appt_patient   FOREIGN KEY (patient_id)        REFERENCES patients (id),
    CONSTRAINT fk_appt_dentist   FOREIGN KEY (dentist_id)        REFERENCES dentists (id),
    CONSTRAINT fk_appt_treatment FOREIGN KEY (treatment_type_id) REFERENCES treatment_types (id),
    CONSTRAINT uq_no_double_booking UNIQUE (dentist_id, date, time)
);

-- -----------------------------------------------------------
-- Table: bills
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS bills (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    bill_no          VARCHAR(30) NOT NULL UNIQUE,
    appointment_id   BIGINT      NOT NULL UNIQUE,
    consultation_fee DOUBLE      NOT NULL,
    treatment_cost   DOUBLE      NOT NULL,
    discount         DOUBLE      NOT NULL DEFAULT 0,
    total            DOUBLE      NOT NULL,
    bill_date        DATE        NOT NULL,
    CONSTRAINT fk_bill_appt FOREIGN KEY (appointment_id) REFERENCES appointments (id)
);

-- ============================================================
-- SEED DATA
-- ============================================================
INSERT INTO staff (username, password_hash, role) VALUES
('admin',      '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMIN'),
('reception1', 'fcc3a23fc7232cc89c7cb0f23d8774fefb73d7dc2ab22e6a1b6b8b202b4dcc91', 'RECEPTIONIST');

INSERT INTO dentists (name, specialization) VALUES
('Dr. Nadeesha Perera', 'Orthodontics'),
('Dr. Kasun Fernando',  'Endodontics'),
('Dr. Dilani Silva',    'General Dentistry');

INSERT INTO treatment_types (name, cost) VALUES
('Consultation',        1000),
('Cleaning & Polishing', 3000),
('Filling',             5000),
('Root Canal',          15000),
('Tooth Extraction',    4000),
('Teeth Whitening',     12000);