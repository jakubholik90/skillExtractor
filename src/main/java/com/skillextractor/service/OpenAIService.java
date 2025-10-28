package com.skillextractor.service;

import com.skillextractor.enums.SkillCategory;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAIService {

    private final OpenAiService openAiService;

    @Value("${openai.model:gpt-4-mini}")
    private String model;

    @Value("${openai.temperature:0.7}")
    private double temperature;

    @Value("${openai.max.tokens:2000}")
    private int maxTokens;

    /**
     * Analyze project code and extract skills
     */
    @Cacheable(value = "projectAnalysis", key = "#projectCode.hashCode()")
    public String analyzeProjectForSkills(String projectCode, List<String> fileNames) {
        log.info("Analyzing project with {} files", fileNames.size());

        String prompt = buildSkillAnalysisPrompt(projectCode, fileNames);

        ChatMessage systemMessage = new ChatMessage("system",
                "You are a Java programming expert analyzing code to extract specific skills.");
        ChatMessage userMessage = new ChatMessage("user", prompt);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(List.of(systemMessage, userMessage))
                .temperature(temperature)
                .maxTokens(maxTokens)
                .build();

        return openAiService.createChatCompletion(request)
                .getChoices().get(0).getMessage().getContent();
    }

    /**
     * Generate quiz for a specific skill
     */
    @Cacheable(value = "quizGeneration", key = "#skillName + '-' + #category")
    public String generateQuiz(String skillName, SkillCategory category, String exampleUsage) {
        log.info("Generating quiz for skill: {}", skillName);

        String prompt = buildQuizPrompt(skillName, category, exampleUsage);

        ChatMessage systemMessage = new ChatMessage("system",
                "You are a Java programming instructor creating quiz questions to assess skill proficiency.");
        ChatMessage userMessage = new ChatMessage("user", prompt);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(List.of(systemMessage, userMessage))
                .temperature(0.8) // Higher temperature for varied questions
                .maxTokens(1500)
                .build();

        return openAiService.createChatCompletion(request)
                .getChoices().get(0).getMessage().getContent();
    }

    private String buildSkillAnalysisPrompt(String projectCode, List<String> fileNames) {
        return String.format("""
            Analyze the following Java project and extract specific programming skills used.
            
            FILES ANALYZED: %s
            
            PROJECT CODE:
            %s
            
            TASK:
            1. Identify distinct programming skills demonstrated in this code
            2. Classify each skill into ONE of these categories:
            %s
            
            3. For each skill, provide:
               - Skill name (concise, e.g., "Stream API filtering", "JPA Entity Mapping")
               - Category (from the list above)
               - Description (ONE sentence maximum)
               - Example from code (short snippet showing usage)
            
            IMPORTANT:
            - Only use the predefined categories above
            - Focus on skills actually demonstrated in the code
            - Avoid generic skills like "Java syntax" - be specific
            - Create ONE general skill per category used (e.g., "OOP - General Knowledge")
            
            OUTPUT FORMAT (JSON):
            {
              "skills": [
                {
                  "name": "Stream API with map and filter",
                  "category": "STREAMS_LAMBDAS",
                  "description": "Uses Stream API for data transformation and filtering.",
                  "exampleUsage": "list.stream().filter(x -> x > 0).map(x -> x * 2).collect(Collectors.toList())",
                  "isGeneral": false
                },
                ...
              ]
            }
            """,
                String.join(", ", fileNames),
                projectCode,
                SkillCategory.getAllCategoriesForPrompt()
        );
    }

    private String buildQuizPrompt(String skillName, SkillCategory category, String exampleUsage) {
        return String.format("""
        Create a quiz to assess knowledge of the following Java programming skill:
        
        SKILL: %s
        CATEGORY: %s (%s)
        EXAMPLE USAGE:
        %s
        
        Generate 4 multiple-choice questions that test:
        1. Basic understanding (1 question)
        2. Practical application (2 questions)
        3. Advanced/edge cases (1 question)
        
        REQUIREMENTS:
        - 4 answer options (A, B, C, D) per question
        - Only ONE correct answer per question
        - Questions should be specific and practical
        - Avoid trivial or overly theoretical questions
        - When including code snippets in questions, wrap them in markdown code blocks using triple backticks
        - Use ```java for Java code blocks
        - Example: "What does this code do?\\n```java\\nSystem.out.println(\\"Hello\\");\\n```"
        
        OUTPUT FORMAT (JSON):
        {
          "questions": [
            {
              "number": 1,
              "text": "What is the primary purpose of Stream API?",
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
              "text": "What will this code output?\\n```java\\nList<String> names = Arrays.asList(\\"Alice\\", \\"Bob\\");\\nnames.stream().map(String::toUpperCase).forEach(System.out::println);\\n```",
              "options": [
                "A) alice bob",
                "B) ALICE BOB",
                "C) AliceBob",
                "D) Compilation error"
              ],
              "correctAnswer": "B"
            }
          ]
        }
        
        IMPORTANT: Return ONLY valid JSON. Do not include any text before or after the JSON.
        """,
                skillName,
                category.getDisplayName(),
                category.getDescription(),
                exampleUsage
        );
    }
}