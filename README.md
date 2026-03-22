# 🎯 job-tracker

Full stack job application tracker — Spring Boot + React + PostgreSQL

## Tech Stack

### Backend
- Java 21, Spring Boot 4.0.3, Gradle
- Spring Data JPA + Hibernate
- PostgreSQL
- Spring Security + BCrypt
- JWT Authentication
- Google OAuth2 Login
- Swagger UI (OpenAPI 3.0)
- Jakarta Validation

### Frontend (Coming Soon)
- React + Vite + TailwindCSS

## Features
- ✅ User registration with BCrypt encryption
- ✅ JWT login + Google OAuth2 login
- ✅ Logout endpoint
- ✅ User-specific company management
- ✅ Job application CRUD with ownership checks
- ✅ Input validation
- ✅ Global exception handling
- ✅ Swagger UI documentation
- ⏳ React frontend
- ⏳ Deploy on Railway + Vercel

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
| status | ENUM | APPLIED/SHORTLISTED/INTERVIEW/REJECTED/OFFER |
| work_mode | ENUM | REMOTE/ONSITE/HYBRID |
| platform | VARCHAR | LinkedIn/Naukri etc |
| job_url | VARCHAR | Job posting URL |
| notes | TEXT | Personal notes |
| applied_date | DATE | Application date |
| last_updated | TIMESTAMP | Last update time |

## API Documentation
After running locally, visit:
```
http://localhost:8080/swagger-ui/index.html
```

## API Endpoints
| Method | URL | Description | Auth |
|--------|-----|-------------|------|
| POST | /api/auth/register | Register user | ❌ |
| POST | /api/auth/login | Login get JWT | ❌ |
| POST | /api/auth/logout | Logout | ✅ |
| GET | /oauth2/authorization/google | Google login | ❌ |
| GET | /api/companies | My companies | ✅ |
| POST | /api/companies | Add company | ✅ |
| PUT | /api/companies/{id} | Update company | ✅ |
| DELETE | /api/companies/{id} | Delete company | ✅ |
| GET | /api/jobs | My jobs | ✅ |
| POST | /api/jobs | Add job | ✅ |
| GET | /api/jobs/{id} | Get job by id | ✅ |
| GET | /api/jobs/status/{status} | Filter by status | ✅ |
| PUT | /api/jobs/{id} | Update job | ✅ |
| DELETE | /api/jobs/{id} | Delete job | ✅ |

## Setup

### 1. Clone repo
```bash
git clone https://github.com/spksouns/job-tracker.git
```

### 2. Setup PostgreSQL
- Install PostgreSQL
- Create database: `job_tracker`

### 3. Setup Google OAuth2 Credentials
1. Go to [Google Cloud Console](https://console.cloud.google.com)
2. Create new project → `job-tracker`
3. Navigate to **APIs & Services → OAuth consent screen**
    - User Type: External → Create
    - Fill App name, support email → Save
4. Navigate to **APIs & Services → Credentials**
    - Click **Create Credentials → OAuth Client ID**
    - Application type: **Web application**
    - Authorized redirect URI:
```
     http://localhost:8080/login/oauth2/code/google
```
- Click Create → Copy **Client ID** and **Client Secret**

### 4. Set environment variables
```bash
JWT_SECRET=your-secret-key-minimum-32-chars
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
```

### 5. Configure application.properties
```bash
cp src/main/resources/application.properties.example \
   src/main/resources/application.properties
```
Fill in your PostgreSQL password and environment variables.

### 6. Run
```bash
./gradlew bootRun
```

### 7. Test
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Google login: http://localhost:8080/oauth2/authorization/google
