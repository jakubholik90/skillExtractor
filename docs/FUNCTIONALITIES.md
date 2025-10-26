# SkillExtractor - Functionalities

## MVP Functionalities (Priority 1)

### User Management
- User registration with email and password
- User login with Basic Authentication
- User profile management

### Project Management
- Upload project via local folder path
- Validate uploaded files (max 20 files, 10MB total)
- Support for .java, pom.xml, and .properties files
- Store project metadata (name, upload date, file list)
- Display list of user's projects

### Skill Analysis
- Send entire project to OpenAI API for analysis
- Parse and extract skills from LLM response
- Map skills to predefined categories (hardcoded)
- Save skills to database with parameters:
    - Skill name
    - Category
    - Short description (1 sentence max)
    - Usage example from code
    - Associated project name(s)
- Create "general skill" for each category
- Cache analysis results to avoid duplicate API calls

### Skill Assessment (Quiz)
- Generate quiz for specific skill (3-5 questions via LLM)
- Display quiz to user
- Evaluate quiz results
- Determine skill level based on score:
    - UNKNOWN (applied without knowledge)
    - BASIC (applied with basic knowledge)
    - GOOD (applied with good knowledge)
    - EXPERT (applied at expert level)
- Save quiz results and update skill level
- Allow multiple quiz attempts per skill

### User Interface
- Dashboard showing all user skills grouped by category
- Project upload form
- Skill details view
- Quiz interface
- Skill level indicators

---

## Future Functionalities (Post-MVP)

### To be updated:
- Showing which answer in quiz is false
- Showing a window with skills listed from project after analyzing
- Filtering skills by project
- Adjusting prompt to check if skill is already in database before adding new one
- Showing examples of usage in code for each skill (extracted from project with file name)
- Frameworks and libraries: skill name begining with framework name. be sure that quiz will only ask about this specific framework
- Quiz should generate for example 20 questions by one prompt, cache them or save to db, and show only random 4 questions for every click

### Enhanced Analysis
- Support for Python projects
- GitHub repository integration (direct analysis)
- Project comparison tool
- Skill timeline visualization

### Reporting & Export
- Export skills to PDF
- Export skills to JSON
- Generate CV-ready skill report
- Visual skill radar chart

### Intelligence & Recommendations
- LLM-powered learning path suggestions
- Identify skill gaps
- Recommend resources for improvement

### Gamification
- Achievement badges
- Skill milestones
- User progress tracking
- Leaderboard (optional)

### Advanced Quizzes
- Category-wide quizzes
- Adaptive difficulty
- Quiz history and analytics
- Timed challenges

### Collaboration
- Compare skills with other users
- Share skill profiles
- Team skill assessment