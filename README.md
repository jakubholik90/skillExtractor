# SkillExtractor 🚀

**SkillExtractor** is an AI-powered application that analyzes Java projects to identify programming skills and assess knowledge levels through automated quizzes.

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![OpenAI](https://img.shields.io/badge/OpenAI-GPT--4-412991.svg)](https://openai.com/)

---

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)

---

## 🎯 Overview

SkillExtractor helps developers:
- **Discover** programming skills used in their Java projects
- **Assess** knowledge level through AI-generated quizzes
- **Track** skill progression across multiple projects
- **Build** a comprehensive skill portfolio

### How It Works
1. **Upload** your Java project (up to 20 files)
2. **Analyze** code using OpenAI GPT-4
3. **Extract** skills mapped to predefined categories
4. **Quiz** yourself to determine proficiency level
5. **Track** your skills in a personal dashboard

---

## ✨ Features

### MVP (v1.0)
- ✅ User registration and authentication
- ✅ Java project upload and validation
- ✅ AI-powered skill extraction (15 predefined categories)
- ✅ Automated quiz generation for each skill
- ✅ Skill level assessment (Unknown → Basic → Good → Expert)
- ✅ Personal skill dashboard
- ✅ Project metadata storage
- ✅ Response caching to optimize API usage

### Upcoming Features
- 🔜 Python project support
- 🔜 GitHub repository integration
- 🔜 PDF/JSON skill export
- 🔜 Visual skill radar charts
- 🔜 Learning path recommendations
- 🔜 Achievement badges

---

## 🛠️ Tech Stack

### Backend
- **Java 17** with **Spring Boot 3.2**
- **Spring Security** (Basic Auth)
- **Spring Data JPA** + Hibernate
- **PostgreSQL 15**
- **OpenAI Java SDK**
- **Caffeine Cache**

### Frontend
- **HTML5 + CSS3 + Bootstrap 5**
- **Vanilla JavaScript** (Fetch API)

### Deployment
- **Railway.app** (Backend + Database)
- **GitHub** (CI/CD integration)

---

## 📁 Project Structure

```
skillextractor/
│
├── docs/                                    # Documentation
│   ├── FUNCTIONALITIES.md                   # Feature specifications
│   ├── STACK.md                             # Technology stack details
│   ├── DEPLOYMENT.md                        # Deployment guide
│   └── API.md                               # API documentation
│
├── src/
│   ├── main/
│   │   ├── java/com/skillextractor/
│   │   │   ├── SkillExtractorApplication.java    # Main application class
│   │   │   │
│   │   │   ├── config/                           # Configuration classes
│   │   │   │   ├── SecurityConfig.java           # Spring Security setup
│   │   │   │   ├── CacheConfig.java              # Caffeine cache config
│   │   │   │   └── OpenAIConfig.java             # OpenAI API client
│   │   │   │
│   │   │   ├── controller/                       # REST Controllers
│   │   │   │   ├── AuthController.java           # Registration/Login
│   │   │   │   ├── ProjectController.java        # Project management
│   │   │   │   ├── SkillController.java          # Skill retrieval
│   │   │   │   └── QuizController.java           # Quiz generation/submission
│   │   │   │
│   │   │   ├── service/                          # Business logic
│   │   │   │   ├── UserService.java              # User management
│   │   │   │   ├── ProjectService.java           # Project operations
│   │   │   │   ├── SkillAnalysisService.java     # AI-powered analysis
│   │   │   │   ├── OpenAIService.java            # OpenAI API integration
│   │   │   │   └── QuizService.java              # Quiz logic
│   │   │   │
│   │   │   ├── repository/                       # JPA Repositories
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── ProjectRepository.java
│   │   │   │   ├── SkillRepository.java
│   │   │   │   └── QuizResultRepository.java
│   │   │   │
│   │   │   ├── model/                            # Entity classes
│   │   │   │   ├── User.java
│   │   │   │   ├── Project.java
│   │   │   │   ├── Skill.java
│   │   │   │   └── QuizResult.java
│   │   │   │
│   │   │   ├── dto/                              # Data Transfer Objects
│   │   │   │   ├── ProjectUploadRequest.java
│   │   │   │   ├── SkillResponse.java
│   │   │   │   ├── QuizRequest.java
│   │   │   │   ├── QuizResponse.java
│   │   │   │   ├── QuizResultResponse.java
│   │   │   │   ├── UserRegistrationRequest.java
│   │   │   │   └── AuthResponse.java
│   │   │   │
│   │   │   ├── enums/                            # Enumerations
│   │   │   │   ├── SkillCategory.java            # 15 predefined categories
│   │   │   │   └── SkillLevel.java               # 4 proficiency levels
│   │   │   │
│   │   │   └── exception/                        # Exception handling
│   │   │       ├── GlobalExceptionHandler.java   # Global error handler
│   │   │       └── CustomExceptions.java         # Custom exception classes
│   │   │
│   │   └── resources/
│   │       ├── application.properties            # Main configuration
│   │       ├── application-dev.properties        # Development config
│   │       ├── application-prod.properties       # Production config
│   │       │
│   │       └── static/                           # Frontend files
│   │           ├── index.html                    # Landing page
│   │           ├── login.html                    # Login page
│   │           ├── register.html                 # Registration page
│   │           ├── dashboard.html                # User dashboard
│   │           │
│   │           ├── css/
│   │           │   └── style.css                 # Custom styles
│   │           │
│   │           └── js/
│   │               ├── app.js                    # Common utilities
│   │               └── dashboard.js              # Dashboard logic
│   │
│   └── test/                                     # Tests (optional)
│       └── java/com/skillextractor/
│
├── pom.xml                                       # Maven dependencies
├── Dockerfile                                    # Docker configuration
├── docker-compose.yml                            # Docker Compose setup
├── .gitignore                                    # Git ignore rules
└── README.md                                     # This file
```

---

## 📦 Complete File List

### Documentation (5 files)
- `README.md`
- `FUNCTIONALITIES.md`
- `STACK.md`
- `DEPLOYMENT.md`
- `docs/API.md`

### Configuration (6 files)
- `pom.xml`
- `Dockerfile`
- `docker-compose.yml`
- `.gitignore`
- `src/main/resources/application.properties`
- `src/main/resources/application-dev.properties`
- `src/main/resources/application-prod.properties`

### Backend Java (29 files)
**Main Application:**
- `SkillExtractorApplication.java`

**Config (3 files):**
- `SecurityConfig.java`
- `CacheConfig.java`
- `OpenAIConfig.java`

**Controllers (4 files):**
- `AuthController.java`
- `ProjectController.java`
- `SkillController.java`
- `QuizController.java`

**Services (5 files):**
- `UserService.java`
- `ProjectService.java`
- `SkillAnalysisService.java`
- `OpenAIService.java`
- `QuizService.java`

**Repositories (4 files):**
- `UserRepository.java`
- `ProjectRepository.java`
- `SkillRepository.java`
- `QuizResultRepository.java`

**Models (4 files):**
- `User.java`
- `Project.java`
- `Skill.java`
- `QuizResult.java`

**DTOs (7 files):**
- `ProjectUploadRequest.java`
- `SkillResponse.java`
- `QuizRequest.java`
- `QuizResponse.java`
- `QuizResultResponse.java`
- `UserRegistrationRequest.java`
- `AuthResponse.java`

**Enums (2 files):**
- `SkillCategory.java`
- `SkillLevel.java`

**Exceptions (2 files):**
- `GlobalExceptionHandler.java`
- `CustomExceptions.java`

### Frontend (7 files)
**HTML (4 files):**
- `index.html`
- `login.html`
- `register.html`
- `dashboard.html`

**CSS (1 file):**
- `style.css`

**JavaScript (2 files):**
- `app.js`
- `dashboard.js`

**Total: 47 files created**

---

## 🚀 Getting Started

### Prerequisites
- **Java 17** or higher
- **Maven 3.8+**
- **PostgreSQL 15** (or use Docker)
- **OpenAI API Key** ([Get one here](https://platform.openai.com/))

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/skillextractor.git
cd skillextractor
```

2. **Configure database** (application.properties)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/skillextractor
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. **Set OpenAI API Key**
```bash
export OPENAI_API_KEY=your_openai_api_key
```
Or add to `application-dev.properties`:
```properties
openai.api.key=${OPENAI_API_KEY}
```

4. **Build and run**
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

5. **Access the application**
- Frontend: `http://localhost:8080`
- API: `http://localhost:8080/api`

### Using Docker Compose (Alternative)

```bash
# 1. Clone repository
git clone https://github.com/yourusername/skillextractor.git
cd skillextractor

# 2. Set environment variable
export OPENAI_API_KEY=your_openai_api_key

# 3. Run with Docker Compose
docker-compose up --build

# 4. Access application
http://localhost:8080

# Stop containers
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

---

## 📖 Usage

### 1. Register Account
Navigate to `/register` and create your account.

### 2. Upload Project
- Go to Dashboard → "Upload Project"
- Select local folder containing your Java project
- Supported files: `.java`, `pom.xml`, `.properties`
- Max: 20 files, 10MB total

### 3. View Extracted Skills
Skills are automatically extracted and categorized:
- **Syntax Basics** (loops, conditionals)
- **OOP** (inheritance, polymorphism)
- **Collections** (List, Map, Set)
- **Frameworks** (Spring, Hibernate)
- And 11 more categories...

### 4. Take Quiz
- Click on any skill to generate a quiz
- Answer 3-5 AI-generated questions
- Receive skill level assessment:
    - 🟥 **Unknown** (0-40%)
    - 🟨 **Basic** (41-60%)
    - 🟩 **Good** (61-85%)
    - 🟦 **Expert** (86-100%)

---

## 📡 API Documentation

### Authentication
All endpoints (except registration/login) require Basic Auth:
```
Authorization: Basic base64(username:password)
```

### Key Endpoints

#### Projects
```http
POST /api/projects/upload          # Upload new project
GET  /api/projects                 # List user's projects
GET  /api/projects/{id}            # Get project details
DELETE /api/projects/{id}          # Delete project
```

#### Skills
```http
GET  /api/skills                   # List all user skills
GET  /api/skills/category/{cat}    # Skills by category
GET  /api/skills/{id}              # Skill details
```

#### Quizzes
```http
POST /api/quiz/generate/{skillId}  # Generate quiz for skill
POST /api/quiz/submit              # Submit quiz answers
GET  /api/quiz/results/{skillId}   # Get latest quiz result
```

Full API documentation: [API.md](docs/API.md)

---

## 🌐 Deployment (Railway.app)

### 1. Prepare for Deployment
```bash
# Create Dockerfile in project root
FROM openjdk:17-jdk-slim
COPY backend/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

### 2. Configure Railway
1. Connect GitHub repository to Railway
2. Add PostgreSQL database (Railway provides free tier)
3. Set environment variables:
    - `OPENAI_API_KEY`
    - `SPRING_PROFILES_ACTIVE=prod`

### 3. Deploy
```bash
git push origin main  # Auto-deploys to Railway
```

Railway will provide a public URL: `https://your-app.railway.app`

---

## 🤝 Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

---

## 📄 License

This project is licensed under the MIT License - see [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

Created as a learning project to practice:
- Java & Spring Boot development
- REST API design
- OpenAI API integration
- Database modeling
- Full-stack development

---

## 🙏 Acknowledgments

- [OpenAI](https://openai.com/) for GPT API
- [Railway.app](https://railway.app/) for hosting
- [Spring Boot](https://spring.io/) for excellent framework

---

**⭐ Star this repo if you find it helpful!**