package com.skillextractor.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillextractor.dto.QuizRequest;
import com.skillextractor.dto.QuizResponse;
import com.skillextractor.dto.QuizResultResponse;
import com.skillextractor.model.QuizResult;
import com.skillextractor.model.Skill;
import com.skillextractor.repository.QuizResultRepository;
import com.skillextractor.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizService {

    private final SkillRepository skillRepository;
    private final QuizResultRepository quizResultRepository;
    private final OpenAIService openAIService;
    private final ObjectMapper objectMapper;

    /**
     * Generate quiz questions for a skill
     */
    public QuizResponse generateQuiz(Long skillId) {
        log.info("Generating quiz for skill ID: {}", skillId);

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found: " + skillId));

        // Call OpenAI to generate quiz
        String quizJson = openAIService.generateQuiz(
                skill.getName(),
                skill.getCategory(),
                skill.getExampleUsage()
        );

        // Parse response
        return parseQuizFromResponse(quizJson, skillId, skill.getName());
    }

    /**
     * Submit quiz answers and calculate score
     */
    @Transactional
    public QuizResultResponse submitQuiz(QuizRequest request) {
        log.info("Submitting quiz for skill ID: {}", request.getSkillId());

        Skill skill = skillRepository.findById(request.getSkillId())
                .orElseThrow(() -> new RuntimeException("Skill not found: " + request.getSkillId()));

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

    /**
     * Get latest quiz result for a skill
     */
    public QuizResultResponse getLatestResult(Long skillId) {
        QuizResult result = quizResultRepository
                .findFirstBySkillIdOrderByCompletedAtDesc(skillId)
                .orElseThrow(() -> new RuntimeException("No quiz results found for skill: " + skillId));

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
            throw new RuntimeException("Failed to parse quiz from AI response");
        }
    }

    private String convertAnswersToJson(List<QuizRequest.Answer> answers) {
        try {
            return objectMapper.writeValueAsString(answers);
        } catch (Exception e) {
            log.error("Error converting answers to JSON", e);
            return "[]";
        }
    }
}