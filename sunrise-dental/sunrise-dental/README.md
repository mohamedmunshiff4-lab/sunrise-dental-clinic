# Sunrise Dental Clinic Management System

Task B implementation for CIS6003 Advanced Programming (WRIT1).
A distributed web application built with Java 17 + Spring Boot 3.

## Features
- Staff login with session management (only authorised staff can access the system)
- Register new appointment with full validation (past dates, double-booking,
  10-digit contact numbers, 30-minute time slots) and auto-generated appointment numbers
- Search / display appointment details by appointment number
- Bill calculation (consultation fee + treatment cost, optional 10% loyalty discount)
  with a printable receipt
- Management reports: daily schedule, daily revenue, appointment statistics
- Help section for new staff
- REST web services (/api/**) so the system can be consumed by other clients

## Technology
- Java 17, Spring Boot 3.2 (Web, Thymeleaf, Data JPA, Validation)
- H2 embedded database (file-based, persists between runs; MySQL config included in comments)
- Maven build

## Design patterns used
1. MVC - Spring MVC separates UI (Thymeleaf), controllers and services
2. Strategy - DiscountStrategy with NoDiscountStrategy / LoyaltyDiscountStrategy,
   chosen at runtime when billing
3. Singleton - AppointmentNumberGenerator (single Spring bean, thread-safe AtomicLong)
4. Factory - BillFactory centralises Bill object creation
5. DAO/Repository - Spring Data JPA repositories isolate persistence logic

## How to run
Requirements: JDK 17+ and Maven 3.8+

    mvn spring-boot:run

Then open http://localhost:8080

## Demo accounts
| Username    | Password   | Role         |
|-------------|------------|--------------|
| admin       | admin123   | ADMIN        |
| reception1  | welcome1   | RECEPTIONIST |

## REST API (web services)
| Method | Endpoint                | Description                          |
|--------|-------------------------|--------------------------------------|
| POST   | /api/auth/login         | Authenticate, returns role           |
| POST   | /api/appointments       | Register appointment (JSON body)     |
| GET    | /api/appointments/{no}  | Get appointment details              |
| GET    | /api/appointments?date= | List appointments for a date         |
| POST   | /api/bills              | Generate bill for an appointment     |
| GET    | /api/reports/daily?date=| Daily schedule + revenue report      |

Example: register an appointment
    curl -X POST http://localhost:8080/api/appointments \
      -H "Content-Type: application/json" \
      -d '{"patientName":"Kamal Perera","address":"Colombo 03",
           "contactNo":"0771234567","dentistId":1,"treatmentTypeId":3,
           "date":"2026-09-10","time":"10:30"}'

## Tests
    mvn test
Six JUnit tests cover: appointment number generation, past-date rejection,
double-booking rejection, bill calculation correctness, discount strategies,
and generator uniqueness.

## Project structure
    src/main/java/com/sunrise/dental/
      entity/       JPA entities (Staff, Patient, Dentist, TreatmentType, Appointment, Bill)
      repository/   Spring Data JPA repositories
      service/      Business logic (auth, appointments, billing, reports)
      controller/   MVC controllers + REST API (web services)
      strategy/     Strategy pattern: discount algorithms
      singleton/    Singleton pattern: appointment number generator
      factory/      Factory pattern: bill creation
      dto/          Validated form objects
      config/       Data seeder + login interceptor
    src/main/resources/templates/  Thymeleaf UI pages
    src/test/java/                 JUnit tests
