# CareerPilot AI — Full-stack starter

A runnable MVP starter for a resume analyzer and job matcher using React/Vite, Spring Boot, MySQL, PDFBox and an optional OpenAI-compatible API.

## Requirements
- Java 21
- Maven 3.9+
- Node.js 20+ and npm
- Docker Desktop (recommended)

## Run it
1. Extract the ZIP and open a terminal in `careerpilot-ai`.
2. Start MySQL:
   ```bash
   docker compose up -d mysql
   ```
3. Start backend in terminal 1:
   ```bash
   cd backend
   ```
   Set environment variables (PowerShell):
   ```powershell
   $env:DB_URL="jdbc:mysql://localhost:3306/careerpilot?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
   $env:DB_USERNAME="careerpilot"
   $env:DB_PASSWORD="careerpilot_dev"
   $env:JWT_SECRET="replace-this-with-a-long-random-secret-at-least-32-characters"
   # Optional: $env:OPENAI_API_KEY="your-key"
   mvn spring-boot:run
   ```
   macOS/Linux:
   ```bash
   export DB_URL='jdbc:mysql://localhost:3306/careerpilot?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC'
   export DB_USERNAME=careerpilot DB_PASSWORD=careerpilot_dev
   export JWT_SECRET='replace-this-with-a-long-random-secret-at-least-32-characters'
   mvn spring-boot:run
   ```
4. Start frontend in terminal 2:
   ```bash
   cd frontend
   cp .env.example .env
   npm install
   npm run dev
   ```
5. Open http://localhost:5173, register, upload a text-based PDF, and run an analysis.

API: http://localhost:8080/api/v1  
Swagger: http://localhost:8080/swagger-ui/index.html  
Health: http://localhost:8080/actuator/health

The API works without an AI key using fallback recommendations. For AI feedback, set `OPENAI_API_KEY` in the backend environment before starting Spring Boot. Do not commit secrets. This is a learning MVP; the match score is required-skill coverage, not an ATS score or hiring prediction.

## MVP workflow
Register/login → upload PDF → choose a sample job → calculate skill coverage → get recommendations → view history.

## Before public deployment
Replace all development secrets, use HTTPS and managed MySQL, configure exact CORS origins, add rate limiting and file scanning, implement data deletion/retention, and use Flyway/Liquibase migrations rather than `ddl-auto=update`. Review the AI provider's data policy before sending resumes.
