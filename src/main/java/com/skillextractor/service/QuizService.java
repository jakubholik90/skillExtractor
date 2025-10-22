// QuizService.java - UPDATED VERSION (key methods)

import com.skillextractor.exception.OpenAIApiException;
import com.skillextractor.exception.QuizGenerationException;
import com.skillextractor.exception.QuizNotFoundException;
import com.skillextractor.exception.SkillNotFoundException;

import java.util.List;

public QuizResponse generateQuiz(Long skillId) {
    log.info("Generating quiz for skill ID: {}", skillId);

    Skill skill = skillRepository.findById(skillId)
            .orElseThrow(() -> new SkillNotFoundException(skillId));

    try {
        String quizJson = openAIService.generateQuiz(
                skill.getName(),
                skill.getCategory(),
                skill.getExampleUsage()
        );
        return parseQuizFromResponse(quizJson, skillId, skill.getName());
    } catch (Exception e) {
        log.error("Quiz generation failed", e);
        throw new QuizGenerationException("Failed to generate quiz for skill: " + skill.getName(), e);
    }
}

@Transactional
public QuizResultResponse submitQuiz(QuizRequest request) {
    log.info("Submitting quiz for skill ID: {}", request.getSkillId());

    Skill skill = skillRepository.findById(request.getSkillId())
            .orElseThrow(() -> new SkillNotFoundException(request.getSkillId()));

    // Generate quiz again to get correct answers (cached)
    QuizResponse quiz = generateQuiz(request.getSkillId());

    // Calculate score
    int correctAnswers = 0;
    int totalQuestions = quiz.getQuestions().size();

    for (QuizRequest.Answer userAnswer : request.getAnswers()) {
        QuizResponse.Question question = quiz.getQuestions().stream()
                .filter(q -> q.getNumber() == userAnswer.getQuestionNumber())
                .findFirst()
                .orElse(null);

        if (question != null && question.getCorrectAnswer().equals(userAnswer.getSelectedAnswer())) {
            correctAnswers++;
        }
    }

    int scorePercentage = QuizResult.calculateScore(correctAnswers, totalQuestions);

    // Update skill level
    skill.updateLevel(scorePercentage);
    skillRepository.save(skill);

    // Save quiz result
    QuizResult result = QuizResult.builder()
            .skill(skill)
            .score(scorePercentage)
            .totalQuestions(totalQuestions)
            .correctAnswers(correctAnswers)
            .answersJson(convertAnswersToJson(request.getAnswers()))
            .build();

    result = quizResultRepository.save(result);

    return QuizResultResponse.builder()
            .id(result.getId())
            .skillId(skill.getId())
            .score(scorePercentage)
            .correctAnswers(correctAnswers)
            .totalQuestions(totalQuestions)
            .achievedLevel(skill.getLevel())
            .levelDisplay(skill.getLevel().getDisplayText())
            .completedAt(result.getCompletedAt())
            .build();
}

public QuizResultResponse getLatestResult(Long skillId) {
    QuizResult result = quizResultRepository
            .findFirstBySkillIdOrderByCompletedAtDesc(skillId)
            .orElseThrow(() -> new QuizNotFoundException(skillId));

    return QuizResultResponse.builder()
            .id(result.getId())
            .skillId(result.getSkill().getId())
            .score(result.getScore())
            .correctAnswers(result.getCorrectAnswers())
            .totalQuestions(result.getTotalQuestions())
            .achievedLevel(result.getSkill().getLevel())
            .levelDisplay(result.getSkill().getLevel().getDisplayText())
            .completedAt(result.getCompletedAt())
            .build();
}

private QuizResponse parseQuizFromResponse(String jsonResponse, Long skillId, String skillName) {
    try {
        JsonNode root = objectMapper.readTree(jsonResponse);
        JsonNode questionsNode = root.get("questions");

        List<QuizResponse.Question> questions = new ArrayList<>();

        if (questionsNode != null && questionsNode.isArray()) {
            for (JsonNode qNode : questionsNode) {
                List<String> options = new ArrayList<>();
                JsonNode optionsNode = qNode.get("options");

                if (optionsNode.isArray()) {
                    for (JsonNode option : optionsNode) {
                        options.add(option.asText());
                    }
                }

                QuizResponse.Question question = QuizResponse.Question.builder()
                        .number(qNode.get("number").asInt())
                        .text(qNode.get("text").asText())
                        .options(options)
                        .correctAnswer(qNode.get("correctAnswer").asText())
                        .build();

                questions.add(question);
            }
        }

        return QuizResponse.builder()
                .skillId(skillId)
                .skillName(skillName)
                .questions(questions)
                .build();

    } catch (Exception e) {
        log.error("Error parsing quiz from OpenAI response", e);
        throw new QuizGenerationException("Failed to parse quiz response", e);
    }
}

// ============================================

