# 📚 PHASE ONE: Basic Loan Eligibility System (Without JWT)

## 🏦 Loan Eligibility System - Phase 1
A robust backend API system that automatically determines loan eligibility based on financial data analysis. This system simulates real banking logic using risk assessment formulas and decision engines.

---

## 📋 Table of Contents
* [Overview](#-overview)
* [Features](#-features)
* [Tech Stack](#️-tech-stack)
* [System Architecture](#-system-architecture)
* [Business Logic](#-business-logic)
* [API Endpoints](#-api-endpoints)
* [Database Schema](#-database-schema)
* [Installation Guide](#-installation-guide)
* [Running the Application](#-running-the-application)
* [Testing with Postman](#-testing-with-postman)
* [Sample Test Cases](#-sample-test-cases)
* [Project Structure](#-project-structure)
* [Deployment Options](#-deployment-options)

---

## 🎯 Overview
This is a **loan eligibility prediction system** that acts like a robotic loan officer. Instead of humans manually deciding who gets a loan, this system automatically evaluates applicants based on:

* 💰 **Monthly Income (Salary)**
* 💸 **Monthly Expenses**
* 📊 **Credit Score**
* 📈 **Debt-to-Income Ratio (DTI)**
* 💵 **Disposable Income**

The system then decides: **APPROVED ✅**, **REJECTED ❌**, or **MANUAL REVIEW 🤔**

---

## ✨ Features

### ### Core Features
* ✅ **Automated Loan Decision Engine** - Makes instant decisions based on financial rules.
* ✅ **Risk Assessment Calculator** - Evaluates applicant risk (Low/Medium/High/Very High).
* ✅ **DTI Ratio Analysis** - Calculates Debt-to-Income percentage.
* ✅ **Disposable Income Calculator** - Determines money left after expenses.
* ✅ **Credit Score Rating** - Categorizes scores (Excellent/Good/Fair/Poor).
* ✅ **Detailed Decision Reasons** - Explains the "Why" behind the status.
* ✅ **Recommended Loan Amount** - Suggests a safe amount based on profile.
* ✅ **Complete CRUD Operations** - For user financial profiles.

### ### Business Features
* 📊 **Real Banking Logic** - Simulates actual bank risk assessment.
* 🔄 **Rule-Based Engine** - Configurable business rules.
* 👤 **User Profile Management** - Securely store financial history.

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
| :--- | :--- | :--- |
| **Java** | 17+ | Core programming language |
| **Spring Boot** | 3.1.x | Application framework |
| **Spring Data JPA** | 3.1.x | Database ORM |
| **PostgreSQL** | 14+ | Database |
| **Lombok** | 1.18.x | Reduce boilerplate code |
| **Maven** | 3.8.x | Build tool |
| **Hibernate Validator**| 8.0.x | Input validation |

---

## 🏗️ System Architecture

### Logic Flow
User Input (JSON) → Controller (REST) → Service (Logic) → Decision Engine (Rules) → Database (Save) → Response (Result)

# Component Diagram
```
┌─────────────────┐     ┌──────────────────┐     ┌─────────────────┐
│                 │     │                  │     │                 │
│   Client App    │────▶│   REST API       │────▶│   PostgreSQL    │
│   (Postman)     │     │   Controller     │     │   Database      │
│                 │◀────│                  │◀────│                 │
└─────────────────┘     └──────────────────┘     └─────────────────┘
                                │
                                ▼
                        ┌──────────────────┐
                        │  Decision Engine │
                        │  • DTI Calculator│
                        │  • Risk Analyzer │
                        │  • Rule Engine   │
                        └──────────────────┘
```

🧠 Business Logic
### Core Formulas
Debt-to-Income Ratio (DTI):
DTI = (Monthly Expenses ÷ Monthly Salary) × 100

Disposable Income:
Disposable Income = Monthly Salary - Monthly Expenses

### Decision Rules Engine
```
IF creditScore < 650 → REJECTED
ELSE IF DTI > 60% → REJECTED
ELSE IF disposableIncome < 3000 → REJECTED
ELSE IF creditScore > 720 AND DTI < 40% → APPROVED
ELSE → MANUAL REVIEW
```

# 🔌 API Endpoints
Base URL: http://localhost:8080/api

## 📌 API Endpoints

| Method | Endpoint              | Description                     | Auth Required |
|--------|-----------------------|---------------------------------|---------------|
| POST   | /loan/apply           | Apply for a new loan            | ❌ No         |
| GET    | /loan/{id}            | Get loan decision by ID         | ❌ No         |
| GET    | /loan/health          | System health check             | ❌ No         |
| PUT    | /loan/user/{userId}   | Update user profile             | ❌ No         |


# 📦 Database Schema
### Table: user_financial_profiles
## 📊 Database Schema — user_financial_profiles

| Column        | Type      | Constraints        | Description              |
|--------------|-----------|-------------------|--------------------------|
| id           | BIGSERIAL | PRIMARY KEY        | Unique identifier        |
| name         | VARCHAR   | NOT NULL           | User's full name         |
| email        | VARCHAR   | UNIQUE, NOT NULL   | User's email             |
| salary       | DECIMAL   | NOT NULL           | Monthly salary           |
| credit_score | INT       | NOT NULL           | 300–850 range            |

# 📥 Installation Guide
### Step 1: Clone the Repository
Bash
```
git clone [https://github.com/yourusername/loan-eligibility-system.git](https://github.com/yourusername/loan-eligibility-system.git)
cd loan-eligibility-system
```
### Step 2: Create Database
SQL
```
CREATE DATABASE loan_db;
CREATE USER loan_user WITH PASSWORD 'loan123';
GRANT ALL PRIVILEGES ON DATABASE loan_db TO loan_user;
```
### Step 3: Configure application.properties
Properties
```
spring.datasource.url=jdbc:postgresql://localhost:5432/loan_db
spring.datasource.username=postgres
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

# 🚀 Running the Application
Bash
# Using Maven Wrapper
```
./mvnw clean install
./mvnw spring-boot:run
```

# 🧪 Sample Test Case
Request: POST /api/loan/apply
```
{
    "salary": 60000,
    "expenses": 18000,
    "creditScore": 780,
    "amount": 150000,
    "termMonths": 36,
    "email": "perfect@example.com"
}
```
expected output
```
{
    "applicationId": 1,
    "decision": "APPROVED",
    "riskLevel": "LOW",
    "reason": "Approved - Excellent credit score (780) and healthy DTI ratio (30.0%)",
    "dtiRatio": 30.0,
    "disposableIncome": 42000,
    "message": "Congratulations! Your loan has been approved."
}
```
# 📁 Project Structure
```
loan-system/
├── src/main/java/com/loan/system/
│   ├── controller/      # REST Endpoints
│   ├── service/         # Business Logic
│   ├── model/           # Entities
│   ├── dto/             # Data Transfer Objects
│   └── rules/           # Risk Assessment Logic
├── src/main/resources/
│   └── application.properties
└── pom.xml
```
# 📊 Project Status
✅ Phase 1 Complete - Basic Loan Eligibility System

⏳ Phase 2 - JWT Authentication (Coming Soon)

⏳ Phase 3 - Admin Dashboard

👨‍💻 Author
Ntlemo Durksie

GitHub: @durksie

LinkedIn: [(https://www.linkedin.com/in/durksie-ntlemo-29782426b/)]
