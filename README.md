# 🎯 job-tracker

Full stack job application tracker — Spring Boot + React + PostgreSQL

## Live Demo
🚀 **Live API:** https://job-tracker-production-939a.up.railway.app/swagger-ui/index.html


---

## Tech Stack

### Backend
- Java 21, Spring Boot 4.0.3, Gradle
- Spring Data JPA + Hibernate
- PostgreSQL
- Spring Security + BCrypt
- JWT Authentication (jwt 0.12.6)
- Google OAuth2 Login
- Swagger UI (springdoc-openapi 2.8.6)
- Jakarta Validation
- Docker (multi-stage build)
- Deployed on Railway

### Frontend
- React + Vite + TailwindCSS *(in progress)*

---

## Features
- ✅ User registration with BCrypt encryption
- ✅ JWT login + Google OAuth2 login
- ✅ Logout endpoint
- ✅ User-specific company management with ownership validation
- ✅ Job application CRUD with ownership checks
- ✅ Filter jobs by status
- ✅ Input validation (Jakarta Validation)
- ✅ Global exception handling
- ✅ Swagger UI documentation
- ✅ Unit tests — JUnit 5 + Mockito (18 tests)
- ✅ Dockerized (multi-stage build)
- ✅ Deployed on Railway with PostgreSQL
- ⏳ React frontend (in progress)
- ⏳ Deploy frontend on Vercel

---

## Database Design

### Entity Relationship
```
users (1) ──── (many) companies
users (1) ──── (many) jobs
companies (1) ──── (many) jobs
```

### Tables

#### users
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT (PK) | Auto generated |
| name | VARCHAR | User full name |
| email | VARCHAR (unique) | Login email |
| password | VARCHAR | BCrypt hashed |
| created_at | TIMESTAMP | Account creation time |

#### companies
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT (PK) | Auto generated |
| user_id | BIGINT (FK) | Owner user |
| name | VARCHAR | Company name |
| website | VARCHAR | Company website |
| location | VARCHAR | Company location |

#### jobs
| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT (PK) | Auto generated |
| user_id | BIGINT (FK) | Owner user |
| company_id | BIGINT (FK) | Applied company |
| role | VARCHAR | Job role/title |
| salary_min | INTEGER | Minimum salary |
| salary_max | INTEGER | Maximum salary |
| status | ENUM | APPLIED / INTERVIEW / OFFERED / REJECTED / ACCEPTED / WITHDRAWN |
| work_mode | ENUM | REMOTE / ONSITE / HYBRID |
| platform | VARCHAR | LinkedIn / Naukri etc |
| job_url | VARCHAR | Job posting URL |
| notes | TEXT | Personal notes |
| applied_date | DATE | Application date |
| last_updated | TIMESTAMP | Last update time |

---

## API Endpoints

| Method | URL | Description | Auth |
|--------|-----|-------------|------|
| POST | /api/auth/register | Register user | ❌ |
| POST | /api/auth/login | Login — returns JWT | ❌ |
| POST | /api/auth/logout | Logout | ✅ |
| GET | /oauth2/authorization/google | Google OAuth2 login | ❌ |
| GET | /api/companies | Get my companies | ✅ |
| POST | /api/companies | Create company | ✅ |
| GET | /api/companies/{id} | Get company by ID | ✅ |
| PUT | /api/companies/{id} | Update company | ✅ |
| DELETE | /api/companies/{id} | Delete company | ✅ |
| GET | /api/jobs/user/{userId} | Get all my jobs | ✅ |
| POST | /api/jobs | Create job | ✅ |
| GET | /api/jobs/{id} | Get job by ID | ✅ |
| GET | /api/jobs/user/{userId}/status?status=APPLIED | Filter by status | ✅ |
| PUT | /api/jobs/{id} | Update job | ✅ |
| DELETE | /api/jobs/{id} | Delete job | ✅ |

---

## Running Locally

### 1. Clone repo
```bash
git clone https://github.com/spksouns/job-tracker.git
cd job-tracker
```

### 2. Setup PostgreSQL
```sql
CREATE DATABASE job_tracker;
```

### 3. Setup Google OAuth2 Credentials
1. Go to [Google Cloud Console](https://console.cloud.google.com)
2. Create project → `job-tracker`
3. APIs & Services → OAuth consent screen → External → Create
4. APIs & Services → Credentials → Create OAuth Client ID
   - Application type: Web application
   - Authorized redirect URI:
     ```
     http://localhost:8080/login/oauth2/code/google
     ```
5. Copy Client ID and Client Secret

### 4. Set environment variables (Windows)
```powershell
$env:JWT_SECRET="your-secret-key-minimum-32-chars"
$env:GOOGLE_CLIENT_ID="your-google-client-id"
$env:GOOGLE_CLIENT_SECRET="your-google-client-secret"
```

### 5. Configure application.properties
```bash
cp src/main/resources/application.properties.example \
   src/main/resources/application.properties
```
Fill in your PostgreSQL password.

### 6. Run
```bash
./gradlew bootRun
```

### 7. Test
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Google login: http://localhost:8080/oauth2/authorization/google

---

## Running Tests
```bash
./gradlew clean test

# View HTML report:
# build/reports/tests/test/index.html
```

### Test Coverage
- `JobServiceTest` — 7 tests (CRUD + ownership validation)
- `CompanyServiceTest` — 6 tests (CRUD + ownership validation)
- `AuthControllerTest` — 5 tests (register, login, error cases)

---

## Docker

### Build image locally
```bash
docker build -t job-tracker .
```

### Run with Docker
```bash
docker run -p 8080:8080 \
  -e JWT_SECRET=your_secret \
  -e GOOGLE_CLIENT_ID=your_id \
  -e GOOGLE_CLIENT_SECRET=your_secret \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/job_tracker \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=your_password \
  job-tracker
```

---

## Deployment (Railway)

App is live on Railway with PostgreSQL.

**Live Swagger UI:**
https://job-tracker-production-939a.up.railway.app/swagger-ui/index.html

---

## Project Structure

```
src/
├── main/java/com/github/spksouns/job_tracker/
│   ├── config/
│   │   ├── SecurityConfig.java      # CORS + JWT filter + OAuth2
│   │   ├── JwtUtil.java             # JWT generate + validate
│   │   ├── JwtFilter.java           # JWT request filter
│   │   └── OAuth2SuccessHandler.java
│   ├── controller/
│   │   ├── AuthController.java      # register, login, logout
│   │   ├── CompanyController.java
│   │   └── JobController.java
│   ├── dto/
│   │   └── LoginRequest.java
│   ├── entity/
│   │   ├── User.java
│   │   ├── Company.java
│   │   └── Job.java                 # Status + WorkMode enums
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── CompanyRepository.java
│   │   └── JobRepository.java
│   └── service/
│       ├── JobService.java
│       └── CompanyService.java
└── test/java/com/github/spksouns/job_tracker/
    ├── service/
    │   ├── JobServiceTest.java
    │   └── CompanyServiceTest.java
    └── controller/
        └── AuthControllerTest.java
```
