
// QuizResponse.java
package com.skillextractor.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuizResponse {
    private Long skillId;
    private String skillName;
    private List<Question> questions;

    @Data
    @Builder
    public static class Question {
        private int number;
        private String text;
        private List<String> options; // A, B, C, D
        private String correctAnswer; // Only for results
    }
}

// ============================================

