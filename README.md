# 🎯 job-tracker

Full stack job application tracker — Spring Boot + React + PostgreSQL

## Tech Stack
- Java 21, Spring Boot 4.0.3, Gradle
- Spring Data JPA + Hibernate
- PostgreSQL
- Spring Security + BCrypt
- JWT Authentication (in progress)
- React + Vite + TailwindCSS (coming!)

## Features
- ✅ User registration with BCrypt encryption
- ✅ User login with password validation
- ✅ Company CRUD APIs
- ✅ Job application CRUD APIs
- ⏳ JWT token authentication
- ⏳ React frontend
- ⏳ Deploy on Railway + Vercel

## API Endpoints
| Method | URL | Description |
|--------|-----|-------------|
| POST | /api/auth/register | Register user |
| POST | /api/auth/login | Login user |
| GET | /api/companies | Get all companies |
| POST | /api/companies | Create company |
| GET | /api/jobs/user/{id} | Get jobs by user |
| POST | /api/jobs | Create job |
