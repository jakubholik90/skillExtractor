// OpenAIService.java - ADD error handling

import com.skillextractor.exception.OpenAIApiException;

import java.util.List;

@Cacheable(value = "projectAnalysis", key = "#projectCode.hashCode()")
public String analyzeProjectForSkills(String projectCode, List<String> fileNames) {
    log.info("Analyzing project with {} files", fileNames.size());

    try {
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
    } catch (Exception e) {
        log.error("OpenAI API call failed", e);
        throw new OpenAIApiException("Failed to analyze project", e);
    }
}

@Cacheable(value = "quizGeneration", key = "#skillName + '-' + #category")
public String generateQuiz(String skillName, SkillCategory category, String exampleUsage) {
    log.info("Generating quiz for skill: {}", skillName);

    try {
        String prompt = buildQuizPrompt(skillName, category, exampleUsage);

        ChatMessage systemMessage = new ChatMessage("system",
                "You are a Java programming instructor creating quiz questions to assess skill proficiency.");
        ChatMessage userMessage = new ChatMessage("user", prompt);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(List.of(systemMessage, userMessage))
                .temperature(0.8)
                .maxTokens(1500)
                .build();

        return openAiService.createChatCompletion(request)
                .getChoices().get(0).getMessage().getContent();
    } catch (Exception e) {
        log.error("OpenAI API call failed for quiz generation", e);
        throw new OpenAIApiException("Failed to generate quiz", e);
    }
}