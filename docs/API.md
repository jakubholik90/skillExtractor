# SkillExtractor - API Documentation

## Base URL
```
Local: http://localhost:8080/api
Production: https://your-app.railway.app/api
```

## Authentication

All endpoints (except registration and login) require **Basic Authentication**.

### Headers
```http
Authorization: Basic base64(username:password)
Content-Type: application/json
```

---

## Endpoints

### 🔐 Authentication

#### Register User
```http
POST /api/auth/register
```

**Request Body:**
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```

**Response (200 OK):**
```json
{
  "userId": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "message": "User registered successfully"
}
```

**Error Responses:**
- `409 Conflict` - Username or email already exists
- `400 Bad Request` - Invalid request data

---

#### Login
```http
GET /api/auth/login
```

**Headers:**
```http
Authorization: Basic base64(username:password)
```

**Response (200 OK):**
```json
"Login successful"
```

**Error Response:**
- `401 Unauthorized` - Invalid credentials

---

### 📁 Projects

#### Upload Project
```http
POST /api/projects/upload
```

**Request Body:**
```json
{
  "projectName": "My Spring Boot App",
  "description": "A REST API application",
  "files": [
    {
      "filename": "Main.java",
      "content": "public class Main { ... }",
      "extension": "java"
    },
    {
      "filename": "pom.xml",
      "content": "<project>...</project>",
      "extension": "xml"
    }
  ]
}
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Stream API with map and filter",
    "category": "STREAMS_LAMBDAS",
    "categoryDisplayName": "Stream API & Lambdas",
    "description": "Uses Stream API for data transformation and filtering.",
    "exampleUsage": "list.stream().filter(x -> x > 0).collect(Collectors.toList())",
    "level": "UNKNOWN",
    "levelDisplay": "🟥 UNKNOWN (0-40%)",
    "isGeneral": false,
    "projectName": "My Spring Boot App",
    "createdAt": "2025-01-15T10:30:00"
  }
]
```

**Error Responses:**
- `400 Bad Request` - File limit exceeded or invalid files
- `500 Internal Server Error` - Analysis failed

---

#### Get All Projects
```http
GET /api/projects
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "My Spring Boot App",
    "description": "A REST API application",
    "uploadedAt": "2025-01-15T10:30:00",
    "analyzedFiles": "Main.java,UserController.java,pom.xml",
    "totalFiles": 3,
    "totalSizeKb": 45
  }
]
```

---

#### Get Project by ID
```http
GET /api/projects/{id}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "My Spring Boot App",
  "description": "A REST API application",
  "uploadedAt": "2025-01-15T10:30:00",
  "analyzedFiles": "Main.java,UserController.java,pom.xml",
  "totalFiles": 3,
  "totalSizeKb": 45
}
```

**Error Response:**
- `404 Not Found` - Project not found

---

#### Delete Project
```http
DELETE /api/projects/{id}
```

**Response (200 OK):**
```json
"Project deleted successfully"
```

**Error Responses:**
- `404 Not Found` - Project not found
- `403 Forbidden` - Unauthorized to delete this project

---

### 🎯 Skills

#### Get All User Skills
```http
GET /api/skills
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Stream API with map and filter",
    "category": "STREAMS_LAMBDAS",
    "categoryDisplayName": "Stream API & Lambdas",
    "description": "Uses Stream API for data transformation and filtering.",
    "exampleUsage": "list.stream().filter(x -> x > 0).collect(Collectors.toList())",
    "level": "GOOD",
    "levelDisplay": "🟩 GOOD (61-85%)",
    "isGeneral": false,
    "projectName": "My Spring Boot App",
    "createdAt": "2025-01-15T10:30:00"
  }
]
```

---

#### Get Skills by Category
```http
GET /api/skills/category/{category}
```

**Path Parameters:**
- `category`: One of the predefined categories (e.g., `STREAMS_LAMBDAS`, `OOP`)

**Available Categories:**
- `SYNTAX_BASICS`
- `STREAMS_LAMBDAS`
- `OOP`
- `COLLECTIONS`
- `EXCEPTION_HANDLING`
- `FILE_HANDLING`
- `DATABASE`
- `FRAMEWORKS`
- `CLEAN_CODE`
- `ALGORITHMS`
- `TESTING`
- `BUILD_TOOLS`
- `DESIGN_PATTERNS`
- `CONCURRENCY`
- `REST_API`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Stream API with map and filter",
    "category": "STREAMS_LAMBDAS",
    "categoryDisplayName": "Stream API & Lambdas",
    "description": "Uses Stream API for data transformation.",
    "exampleUsage": "list.stream().filter(x -> x > 0).collect(Collectors.toList())",
    "level": "GOOD",
    "levelDisplay": "🟩 GOOD (61-85%)",
    "isGeneral": false,
    "projectName": "My Spring Boot App",
    "createdAt": "2025-01-15T10:30:00"
  }
]
```

---

#### Get Skill by ID
```http
GET /api/skills/{id}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Stream API with map and filter",
  "category": "STREAMS_LAMBDAS",
  "categoryDisplayName": "Stream API & Lambdas",
  "description": "Uses Stream API for data transformation.",
  "exampleUsage": "list.stream().filter(x -> x > 0).collect(Collectors.toList())",
  "level": "GOOD",
  "levelDisplay": "🟩 GOOD (61-85%)",
  "isGeneral": false,
  "projectName": "My Spring Boot App",
  "createdAt": "2025-01-15T10:30:00"
}
```

**Error Response:**
- `404 Not Found` - Skill not found

---

### 📝 Quizzes

#### Generate Quiz
```http
POST /api/quiz/generate/{skillId}
```

**Response (200 OK):**
```json
{
  "skillId": 1,
  "skillName": "Stream API with map and filter",
  "questions": [
    {
      "number": 1,
      "text": "What is the primary purpose of the Stream API in Java?",
      "options": [
        "A) To handle file I/O operations",
        "B) To process collections functionally",
        "C) To manage threads",
        "D) To connect to databases"
      ],
      "correctAnswer": "B"
    },
    {
      "number": 2,
      "text": "Which method is used to transform elements in a Stream?",
      "options": [
        "A) filter()",
        "B) map()",
        "C) collect()",
        "D) forEach()"
      ],
      "correctAnswer": "B"
    }
  ]
}
```

**Error Responses:**
- `404 Not Found` - Skill not found
- `500 Internal Server Error` - Quiz generation failed

---

#### Submit Quiz
```http
POST /api/quiz/submit
```

**Request Body:**
```json
{
  "skillId": 1,
  "answers": [
    {
      "questionNumber": 1,
      "selectedAnswer": "B"
    },
    {
      "questionNumber": 2,
      "selectedAnswer": "B"
    },
    {
      "questionNumber": 3,
      "selectedAnswer": "A"
    },
    {
      "questionNumber": 4,
      "selectedAnswer": "C"
    }
  ]
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "skillId": 1,
  "score": 75,
  "correctAnswers": 3,
  "totalQuestions": 4,
  "achievedLevel": "GOOD",
  "levelDisplay": "🟩 GOOD (61-85%)",
  "completedAt": "2025-01-15T11:00:00"
}
```

**Error Response:**
- `404 Not Found` - Skill not found

---

#### Get Latest Quiz Result
```http
GET /api/quiz/results/{skillId}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "skillId": 1,
  "score": 75,
  "correctAnswers": 3,
  "totalQuestions": 4,
  "achievedLevel": "GOOD",
  "levelDisplay": "🟩 GOOD (61-85%)",
  "completedAt": "2025-01-15T11:00:00"
}
```

**Error Response:**
- `404 Not Found` - No quiz results found for this skill

---

## Skill Levels

| Level | Score Range | Description | Emoji |
|-------|-------------|-------------|-------|
| UNKNOWN | 0-40% | Applied without knowledge | 🟥 |
| BASIC | 41-60% | Applied with basic knowledge | 🟨 |
| GOOD | 61-85% | Applied with good knowledge | 🟩 |
| EXPERT | 86-100% | Applied at expert level | 🟦 |

---

## Error Response Format

All error responses follow this structure:

```json
{
  "timestamp": "2025-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Project not found with id: 123",
  "path": "/api/projects/123"
}
```

---

## Rate Limiting

- No rate limiting in MVP
- Future: Consider implementing rate limits for OpenAI API calls

---

## Testing with cURL

### Register
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"password123"}'
```

### Login & Get Skills
```bash
curl -X GET http://localhost:8080/api/skills \
  -u testuser:password123
```

### Upload Project
```bash
curl -X POST http://localhost:8080/api/projects/upload \
  -u testuser:password123 \
  -H "Content-Type: application/json" \
  -d @project-data.json
```

---

## Postman Collection

Import this collection to test all endpoints:
```json
{
  "info": {
    "name": "SkillExtractor API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "auth": {
    "type": "basic",
    "basic": [
      {"key": "username", "value": "{{username}}"},
      {"key": "password", "value": "{{password}}"}
    ]
  },
  "item": [
    {
      "name": "Register",
      "request": {
        "method": "POST",
        "url": "{{baseUrl}}/api/auth/register",
        "body": {
          "mode": "raw",
          "raw": "{\n  \"username\": \"testuser\",\n  \"email\": \"test@example.com\",\n  \"password\": \"password123\"\n}"
        }
      }
    }
  ]
}
```