# SkillExtractor - Technology Stack

## Backend

### Core
- **Java**: 17
- **Spring Boot**: 3.2.x
- **Build Tool**: Maven

### Frameworks & Libraries
- **Spring Web**: REST API development
- **Spring Data JPA**: Database ORM with Hibernate
- **Spring Security**: User authentication (Basic Auth)
- **Spring Cache**: Caching abstraction
- **Caffeine**: In-memory cache implementation
- **Lombok**: Reduce boilerplate code

### Database
- **PostgreSQL**: 15 (primary database)
- **HikariCP**: Connection pooling (default in Spring Boot)

### External APIs
- **OpenAI API**: GPT-4 for code analysis and quiz generation
- **OpenAI Java SDK**: `com.theokanning.openai-gpt3-java:service:0.18.2`

### Utilities
- **Jackson**: JSON processing
- **SLF4J + Logback**: Logging

---

## Frontend

### Core Technologies
- **HTML5**: Markup
- **CSS3**: Styling
- **Bootstrap 5**: Responsive design framework
- **Vanilla JavaScript**: Client-side logic
- **Fetch API**: HTTP requests to backend

### Optional
- **Thymeleaf**: Server-side templating (if needed)

---

## Deployment

### Hosting Platform
- **Railway.app**: Backend + PostgreSQL hosting
    - Free tier: $5 monthly credits
    - Automatic deployment from GitHub
    - Built-in PostgreSQL database

### Version Control
- **Git**: Version control
- **GitHub**: Code repository + CI/CD integration

---

## Development Tools

### Recommended IDEs
- **IntelliJ IDEA**: Primary Java IDE
- **VS Code**: Frontend development

### API Testing
- **Postman**: API testing
- **Thunder Client**: VS Code extension for API testing

### Database Management
- **DBeaver**: PostgreSQL client
- **pgAdmin**: Alternative PostgreSQL client

---

## Configuration Files

### Backend
- `application.properties`: Main configuration
- `application-dev.properties`: Development environment
- `application-prod.properties`: Production environment (Railway)
- `pom.xml`: Maven dependencies

### Deployment
- `Dockerfile`: Container configuration for Railway
- `.env`: Environment variables (not committed to Git)

---

## Security

### Authentication
- Spring Security Basic Authentication
- BCrypt password encoding

### API Security
- OpenAI API key stored in environment variables
- CORS configuration for frontend

---

## Predefined Skill Categories (Hardcoded)

```java
public enum SkillCategory {
    SYNTAX_BASICS,           // Basic Syntax & Core Concepts
    STREAMS_LAMBDAS,         // Stream API & Lambdas
    OOP,                     // Object Oriented Programming
    COLLECTIONS,             // Collections & Data Structures
    EXCEPTION_HANDLING,      // Exception Handling
    FILE_HANDLING,           // File Operations
    DATABASE,                // Database Operations
    FRAMEWORKS,              // Frameworks & Libraries
    CLEAN_CODE,              // Clean Code Principles
    ALGORITHMS,              // Algorithms & Problem Solving
    TESTING,                 // Testing (JUnit, Mockito)
    BUILD_TOOLS,             // Build Tools (Maven, Gradle)
    DESIGN_PATTERNS,         // Design Patterns
    CONCURRENCY,             // Multithreading & Concurrency
    REST_API                 // REST API Development
}
```

---

## Project Constraints

### File Upload Limits
- Maximum 20 files per project
- Maximum 10MB total size
- Supported extensions: `.java`, `.xml`, `.properties`

### OpenAI API Limits
- Model: GPT-4 or GPT-3.5-turbo
- Token limit per request: ~8000 tokens (depends on model)
- Response caching to minimize API calls