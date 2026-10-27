# CareerPilot AI 🚀

**AI-Assisted Resume Analysis and Career Guidance Platform**

CareerPilot AI is a web application that helps users evaluate their resumes against job requirements, identify skill gaps, and receive AI-assisted recommendations to improve their job readiness.

## ✨ Features

- **User Authentication:** Register and sign in to access your workspace.
- **Resume Management:** Upload and manage PDF resumes.
- **Job Exploration:** Browse job roles, companies, descriptions, and required skills.
- **Resume Matching:** Compare resume skills with a selected job's requirements.
- **Skill Gap Analysis:** Identify matched and missing skills.
- **AI-Assisted Recommendations:** Receive suggestions based on resume content and job requirements.
- **Analysis History:** View previously saved analysis results.
- **Dashboard Analytics:** Track uploaded resumes, completed analyses, available roles, and average match score.

## 🛠️ Tech Stack

| Component | Technologies |
|---|---|
| Frontend | React, Vite, JavaScript |
| UI Components | CSS, Lucide React |
| Backend | Java, Spring Boot |
| Database | MySQL |
| ORM | Spring Data JPA, Hibernate |
| API Communication | REST APIs, Axios |
| Containerization | Docker |
| Build Tool | Maven |

## 📂 Project Structure

```text
careerpilot-ai/
├── frontend/
│   ├── src/
│   ├── package.json
│   └── .env.example
├── backend/
│   ├── src/
│   └── pom.xml
├── docker-compose.yml
├── .gitignore
└── README.md
```

## ⚙️ Prerequisites

Install the following software:

- Java Development Kit compatible with the backend
- Maven
- Node.js and npm
- Docker Desktop
- Git

## 🚀 Installation and Setup

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/careerpilot-ai.git
cd careerpilot-ai
```

Replace `YOUR_USERNAME` with your GitHub username.

### 2. Configure environment variables

Configure the backend with the required environment variables:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `SERVER_PORT`

Configure the frontend API URL in `frontend/.env`:

```dotenv
VITE_API_BASE_URL=http://localhost:8081/api/v1
```

Keep actual credentials and secrets in your local environment. Never commit passwords, private tokens, or real `.env` files.

### 3. Start MySQL

Ensure Docker Desktop is running, then execute this command from the project root:

```bash
docker compose up -d mysql
```

Verify that the MySQL container is running before proceeding.

### 4. Start the Spring Boot backend

Open a terminal:

```bash
cd backend
mvn spring-boot:run
```

The development backend uses port `8081`. Configure the required environment variables before starting the application.

### 5. Start the React frontend

Open another terminal:

```bash
cd frontend
npm install
npm run dev
```

Open the local URL displayed by Vite, usually:

http://localhost:5173

## 🧭 Application Workflow

1. Register or sign in.
2. Upload a PDF resume.
3. Explore available job roles.
4. Select a target role.
5. Choose a resume and run the analysis.
6. Review the match score, matched skills, missing skills, and recommendations.
7. View previous analyses in Analysis History.

## 📊 Resume Matching Methodology

CareerPilot AI compares extracted resume text against the required skills for a selected job.

The match score is calculated using required-skill coverage:

**Match Score = (Matched Required Skills / Total Required Skills) × 100**

This is a heuristic measure of skill coverage, not an official ATS score or a guarantee of interview selection.

## 🔒 Security and Privacy

- Keep database credentials and JWT secrets out of version control.
- Do not upload real resumes or personal user data to the repository.
- Enforce authentication and authorization for protected endpoints.
- Ensure users can access only their own resumes and analyses before deploying the application.

## 🔮 Future Enhancements

- Semantic resume-to-job matching.
- Improved PDF parsing and support for scanned documents.
- Personalized skill development roadmaps.
- Resume improvement suggestions.
- Automated unit and integration testing.
- Cloud deployment and continuous integration.

## 👩‍💻 Author

**Pavithra Annapurna**

- GitHub: [Pavithra-Annapurna](https://github.com/Pavithra-Annapurna)
- LinkedIn: [Connect on LinkedIn](https://www.linkedin.com/in/pavithra-annapurna-245274339/)

---

*CareerPilot AI — Turn your skills into your next career opportunity.*
