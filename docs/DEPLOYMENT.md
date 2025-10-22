# SkillExtractor - Deployment Guide

## Local Development Setup

### Prerequisites
1. Java 17 or higher
2. Maven 3.8+
3. PostgreSQL 15 (or use Docker)
4. OpenAI API Key

### Steps

1. **Clone repository**
```bash
git clone https://github.com/yourusername/skillextractor.git
cd skillextractor
```

2. **Setup PostgreSQL**
```sql
CREATE DATABASE skillextractor;
CREATE USER skillextractor_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE skillextractor TO skillextractor_user;
```

3. **Configure application.properties**
   Create `src/main/resources/application-dev.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/skillextractor
spring.datasource.username=skillextractor_user
spring.datasource.password=your_password
openai.api.key=your_openai_api_key
```

4. **Set environment variable**
```bash
export OPENAI_API_KEY=your_openai_api_key
```

5. **Build and run**
```bash
mvn clean install
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

6. **Access application**
- Frontend: http://localhost:8080
- API docs: http://localhost:8080/api

---

## Railway.app Deployment

### Step 1: Prepare Repository

1. Ensure all code is committed to GitHub
2. Make sure `Dockerfile` is in project root
3. Verify `.gitignore` excludes sensitive files

### Step 2: Railway Setup

1. **Sign up/Login** at https://railway.app
2. **Create New Project** → "Deploy from GitHub repo"
3. **Select your repository**

### Step 3: Add PostgreSQL Database

1. In Railway dashboard, click **"New"** → **"Database"** → **"PostgreSQL"**
2. Railway will automatically create database and set `DATABASE_URL` environment variable

### Step 4: Configure Environment Variables

Add these variables in Railway project settings:

```
OPENAI_API_KEY=your_openai_api_key_here
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=${{Postgres.DATABASE_URL}}  # Auto-filled by Railway
```

### Step 5: Create application-prod.properties

Create `src/main/resources/application-prod.properties`:
```properties
# Database (Railway provides DATABASE_URL)
spring.datasource.url=${DATABASE_URL}
spring.jpa.hibernate.ddl-auto=update

# Production settings
spring.jpa.show-sql=false
logging.level.root=WARN
logging.level.com.skillextractor=INFO

# OpenAI
openai.api.key=${OPENAI_API_KEY}
```

### Step 6: Deploy

1. Push changes to GitHub:
```bash
git add .
git commit -m "Add production configuration"
git push origin main
```

2. Railway will automatically:
    - Detect Dockerfile
    - Build Docker image
    - Deploy application
    - Provide public URL

### Step 7: Verify Deployment

1. Click on deployment URL (e.g., `https://skillextractor-production.up.railway.app`)
2. Test registration and login
3. Upload a small test project

---

## Alternative: Docker Compose (Local)

Create `docker-compose.yml`:
```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: skillextractor
      POSTGRES_USER: skillextractor
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: prod
      DATABASE_URL: jdbc:postgresql://postgres:5432/skillextractor?user=skillextractor&password=password
      OPENAI_API_KEY: ${OPENAI_API_KEY}
    depends_on:
      - postgres

volumes:
  postgres_data:
```

Run:
```bash
docker-compose up --build
```

---

## Troubleshooting

### Issue: Database connection failed
**Solution**: Verify PostgreSQL is running and credentials are correct

### Issue: OpenAI API errors
**Solution**: Check API key is valid and has sufficient credits

### Issue: File upload fails
**Solution**: Check file size limits in `application.properties`

### Issue: 401 Unauthorized
**Solution**: Clear browser localStorage and login again

---

## Monitoring

### Railway Dashboard
- Check logs in Railway dashboard
- Monitor database usage
- Track deployment status

### Application Logs
```bash
# Railway CLI
railway logs

# Local
tail -f logs/spring.log
```

---

## Security Notes

1. **Never commit** API keys or passwords to Git
2. Use **environment variables** for sensitive data
3. Enable **HTTPS** in production (Railway provides this automatically)
4. Consider adding **rate limiting** for API endpoints
5. Implement **CORS** properly if frontend is on different domain

---

## Cost Estimation (Railway Free Tier)

- **Monthly credits**: $5
- **Estimated usage** (MVP):
    - App instance: ~$3/month
    - PostgreSQL: ~$2/month
    - **Total**: ~$5/month (within free tier)

**Note**: OpenAI API costs are separate - estimate $0.01-0.10 per project analysis

---

## Next Steps After Deployment

1. Test all features in production
2. Monitor OpenAI API usage
3. Collect user feedback
4. Plan v2 features
5. Consider upgrading Railway plan if needed