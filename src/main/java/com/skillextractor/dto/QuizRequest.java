// QuizRequest.java
package com.skillextractor.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuizRequest {
    private Long skillId;
    private List<Answer> answers;

    @Data
    public static class Answer {
        private int questionNumber;
        private String selectedAnswer; // A, B, C, or D
    }
}

// ============================================
