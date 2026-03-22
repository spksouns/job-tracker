# 🎯 job-tracker

Full stack job application tracker — Spring Boot + React + PostgreSQL

## Tech Stack

### Backend
- Java 21, Spring Boot 4.0.3, Gradle
- Spring Data JPA + Hibernate
- PostgreSQL
- Spring Security + BCrypt
- JWT Authentication ✅
- Google OAuth2 Login ✅
- Swagger UI (OpenAPI 3.0) ✅
- Jakarta Validation ✅

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
```bash
# Clone repo
git clone https://github.com/spksouns/job-tracker.git

# Copy properties template
cp src/main/resources/application.properties.example \
   src/main/resources/application.properties

# Set environment variables
JWT_SECRET=your-secret-key-minimum-32-chars
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret

# Run
./gradlew bootRun
```