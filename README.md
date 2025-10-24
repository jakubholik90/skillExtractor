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
- [Common Issues & Solutions](#common-issues--solutions)
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
- ✅ User registration and authentication (Basic Auth + Custom UserDetailsService)
- ✅ Java project upload and validation
- ✅ AI-powered skill extraction (15 predefined categories)
- ✅ Automated quiz generation for each skill
- ✅ Skill level assessment (Unknown → Basic → Good → Expert)
- ✅ Personal skill dashboard with real-time updates
- ✅ Project management (upload, list, delete)
- ✅ Response caching to optimize API usage
- ✅ JSON serialization protection (@JsonIgnore patterns)

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
- **Spring Security** (Basic Auth + Custom UserDetailsService)
- **Spring Data JPA** + Hibernate
- **PostgreSQL 15**
- **OpenAI Java SDK**
- **Caffeine Cache**

### Key Patterns & Solutions
- `@JsonIgnore` for preventing infinite recursion (User ↔ Project ↔ Skill)
- `@Transactional(readOnly = true)` for LazyInitializationException prevention
- Custom `@Query` annotations for complex JPA queries
- XSS protection with HTML escaping

### Frontend
- **HTML5 + CSS3 + Bootstrap 5**
- **Vanilla JavaScript** (Fetch API)
- Real-time dashboard updates

### Deployment
- **Local Development** (PostgreSQL + Maven)
- **Railway.app** (Production - optional)

---

## 📁 Project Structure

```
skillextractor/
│
├── src/
│   ├── main/
│   │   ├── java/com/skillextractor/
│   │   │   ├── SkillExtractorApplication.java
│   │   │   │
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java            # Spring Security + UserDetailsService
│   │   │   │   ├── CacheConfig.java               # Caffeine cache
│   │   │   │   └── OpenAIConfig.java              # OpenAI client
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java            # Registration/Login
│   │   │   │   ├── ProjectController.java         # CRUD operations (@Transactional)
│   │   │   │   ├── SkillController.java           # Skill retrieval
│   │   │   │   └── QuizController.java            # Quiz generation/submission
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── UserService.java               # User management
│   │   │   │   ├── CustomUserDetailsService.java  # ⭐ NEW: Spring Security integration
│   │   │   │   ├── ProjectService.java            # Project operations
│   │   │   │   ├── SkillAnalysisService.java      # AI-powered analysis
│   │   │   │   ├── OpenAIService.java             # OpenAI API integration
│   │   │   │   └── QuizService.java               # Quiz logic
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── ProjectRepository.java         # Custom @Query methods
│   │   │   │   ├── SkillRepository.java
│   │   │   │   └── QuizResultRepository.java
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── User.java                      # @JsonIgnore for security
│   │   │   │   ├── Project.java                   # @JsonIgnore for circular refs
│   │   │   │   ├── Skill.java
│   │   │   │   └── QuizResult.java
│   │   │   │
│   │   │   ├── dto/                               # 7 DTOs
│   │   │   ├── enums/                             # SkillCategory, SkillLevel
│   │   │   └── exception/                         # Global exception handling
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-prod.properties
│   │       │
│   │       └── static/
│   │           ├── index.html
│   │           ├── login.html
│   │           ├── register.html
│   │           ├── dashboard.html                 # Real-time updates
│   │           ├── css/style.css
│   │           └── js/
│   │               ├── app.js
│   │               └── dashboard.js               # Delete + auto-refresh
│
├── pom.xml
├── Dockerfile
├── docker-compose.yml
└── README.md

**Total: 48+ files** (including CustomUserDetailsService)
```

---

## 🚀 Getting Started

### Prerequisites
- **Java 17** or higher
- **Maven 3.8+**
- **PostgreSQL 15** (or use Docker)
- **OpenAI API Key** ([Get one here](https://platform.openai.com/))

### Installation

#### 1. Clone the repository
```bash
git clone https://github.com/yourusername/skillextractor.git
cd skillextractor
```

#### 2. Setup PostgreSQL
```sql
CREATE DATABASE skillextractor;
CREATE USER skillextractor_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE skillextractor TO skillextractor_user;
```

#### 3. Configure application
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/skillextractor
spring.datasource.username=your_username
spring.datasource.password=your_password

# OpenAI API Key
openai.api.key=${OPENAI_API_KEY}
```

#### 4. Set OpenAI API Key
```bash
export OPENAI_API_KEY=your_openai_api_key
```
Or add to `application-dev.properties`:
```properties
openai.api.key=sk-your-key-here
```

#### 5. Build and run
```bash
mvn clean install
mvn spring-boot:run
```

#### 6. Access the application
- Frontend: `http://localhost:8080`
- API: `http://localhost:8080/api`

### Using Docker Compose (Alternative)

```bash
# 1. Set environment variable
export OPENAI_API_KEY=your_openai_api_key

# 2. Run with Docker Compose
docker-compose up --build

# 3. Access application
http://localhost:8080
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
Skills are automatically extracted and categorized into 15 categories:
- **Syntax Basics**, **OOP**, **Collections**, **Streams & Lambdas**
- **Exception Handling**, **File Handling**, **Database**
- **Frameworks**, **Clean Code**, **Algorithms**
- **Testing**, **Build Tools**, **Design Patterns**
- **Concurrency**, **REST API**

### 4. Take Quiz
- Click on any skill to generate a quiz
- Answer 3-5 AI-generated questions
- Receive skill level assessment:
    - 🟥 **Unknown** (0-40%)
    - 🟨 **Basic** (41-60%)
    - 🟩 **Good** (61-85%)
    - 🟦 **Expert** (86-100%)

### 5. Manage Projects
- **View** all your projects in the dashboard
- **Delete** projects (with cascade delete of skills)
- **Track** skill progression across projects

---

## 🐛 Common Issues & Solutions

### Issue 1: "Unable to locate Attribute [userId]"
**Problem:** JPA query method name doesn't match entity structure.

**Solution:** Use `@Query` annotation:
```java
@Query("SELECT p FROM Project p WHERE p.user.id = :userId")
List<Project> findByUserId(@Param("userId") Long userId);
```

### Issue 2: "LazyInitializationException"
**Problem:** Hibernate tries to load relationships outside session.

**Solution:** Add `@Transactional(readOnly = true)`:
```java
@GetMapping
@Transactional(readOnly = true)
public ResponseEntity<List<Project>> getUserProjects() { ... }
```

### Issue 3: JSON Infinite Recursion (User ↔ Project)
**Problem:** Jackson tries to serialize circular references.

**Solution:** Add `@JsonIgnore` to relationships:
```java
@Entity
public class Project {
    @ManyToOne
    @JsonIgnore  // Prevent recursion
    private User user;
}
```

### Issue 4: Projects visible in API but not in frontend
**Problem:** Frontend doesn't refresh after operations.

**Solution:** Call `loadProjects()` after upload/delete:
```javascript
await Promise.all([loadProjects(), loadSkills()]);
```

### Issue 5: "httpBasic() is deprecated"
**Problem:** Using old Spring Security 5.x syntax.

**Solution:** Update to Spring Security 6.x:
```java
.httpBasic(Customizer.withDefaults())  // New syntax
```

### Issue 6: Delete button doesn't work
**Problem:** Function not globally accessible or wrong onclick.

**Solution:** Ensure `deleteProject()` is outside `DOMContentLoaded`:
```javascript
// Global function
async function deleteProject(projectId) { ... }
```

**For more troubleshooting, see:**
- `DIAGNOSTIC_GUIDE.md` - Complete diagnostic steps
- `FRONTEND_FIX_COMPLETE.md` - Frontend issues
- `DELETE_FIX_GUIDE.md` - Delete functionality

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
POST   /api/projects/upload          # Upload new project
GET    /api/projects                 # List user's projects
GET    /api/projects/{id}            # Get project details
DELETE /api/projects/{id}            # Delete project
```

#### Skills
```http
GET    /api/skills                   # List all user skills
GET    /api/skills/category/{cat}    # Skills by category
GET    /api/skills/{id}              # Skill details
```

#### Quizzes
```http
POST   /api/quiz/generate/{skillId}  # Generate quiz
POST   /api/quiz/submit              # Submit answers
GET    /api/quiz/results/{skillId}   # Get latest result
```

Full API documentation: [docs/API.md](docs/API.md)

---

## 🌐 Deployment

### Local Development
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Railway.app (Production)

1. **Connect GitHub repository** to Railway
2. **Add PostgreSQL database** (Railway provides free tier)
3. **Set environment variables:**
    - `OPENAI_API_KEY`
    - `SPRING_PROFILES_ACTIVE=prod`
4. **Deploy** - Railway auto-builds and deploys

Railway provides a public URL: `https://your-app.railway.app`

**Deployment guide:** [docs/DEPLOYMENT.md](docs/DEPLOYMENT.md)

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
- Database modeling with proper JPA patterns
- Spring Security with custom authentication
- Full-stack development with real-time updates
- JSON serialization best practices

---

## 🙏 Acknowledgments

- [OpenAI](https://openai.com/) for GPT API
- [Railway.app](https://railway.app/) for hosting
- [Spring Boot](https://spring.io/) for excellent framework
- Community for troubleshooting and best practices

---

## 📚 Additional Resources

### Documentation
- [FUNCTIONALITIES.md](docs/FUNCTIONALITIES.md) - Feature specifications
- [STACK.md](docs/STACK.md) - Technology stack details
- [DEPLOYMENT.md](docs/DEPLOYMENT.md) - Deployment guide
- [API.md](docs/API.md) - API documentation

### Troubleshooting Guides
- `DIAGNOSTIC_GUIDE.md` - Step-by-step diagnostics
- `FRONTEND_FIX_COMPLETE.md` - Frontend JSON issues
- `DELETE_FIX_GUIDE.md` - Delete functionality
- `REPOSITORY_FIX.md` - JPA query problems

### Debug Tools
- `DebugController.java` - Diagnostic endpoints (temporary)
- `test-api.html` - Frontend API tester

---

## 🔄 Recent Updates

### Version 1.0.1 (Current)
- ✅ Added `CustomUserDetailsService` for Spring Security
- ✅ Fixed JSON infinite recursion with `@JsonIgnore`
- ✅ Fixed `LazyInitializationException` with `@Transactional`
- ✅ Updated to Spring Security 6.x syntax (`Customizer.withDefaults()`)
- ✅ Fixed JPA queries with `@Query` annotations
- ✅ Added real-time dashboard updates
- ✅ Improved delete functionality with proper cascade
- ✅ Added XSS protection with HTML escaping
- ✅ Enhanced error logging and debugging

---

**⭐ Star this repo if you find it helpful!**

**💬 Questions? Open an issue!**

**🐛 Found a bug? Check troubleshooting guides first!**