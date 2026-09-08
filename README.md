# Sunrise Dental Clinic Management System

**Unit Code:** CIS6003 - Advanced Programming  
**Student Name:** MOAHMED ASHRAFF MOHAMED MUNSHIFF[cite: 8]  
**Student ID:** BSCSD/34/37[cite: 8]  

## Overview
The Sunrise Dental Clinic Management System is a web-based, three-tier distributed application designed to replace paper-based patient management and appointment booking. The system prevents double bookings, calculates automated bills, and provides structured daily management reports.

## Technology Stack
* **Language:** Java 17
* **Framework:** Spring Boot 3.2 (Spring MVC, Spring Data JPA)
* **Frontend:** Thymeleaf, HTML5, CSS3
* **Database:** MySQL (XAMPP / phpMyAdmin)
* **Testing:** JUnit 5, Spring Boot Test, H2 In-Memory Database
* **Build Tool:** Maven

## Key Features
* **Authentication:** Secure staff login with password encryption.
* **Appointment Management:** Auto-generation of unique appointment IDs and double-booking prevention logic.
* **Billing Module:** Integrated consultation and treatment fees calculation with dynamic discount strategies (Strategy Pattern).
* **Reporting:** Daily revenue and appointment schedule summary.
* **Automated Testing:** Unit and integration testing suite verifying business logic and constraints.

## How to Run
1. Import `database.sql` into MySQL via phpMyAdmin (`sunrise_dental` database).
2. Configure database credentials in `src/main/resources/application.properties`.
3. Run the application using Maven:
   ```bash
   mvn spring-boot:run
   Access the web interface at http://localhost:8080/login
   
