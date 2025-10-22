
// QuizResultResponse.java
package com.skillextractor.dto;

import com.skillextractor.enums.SkillLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class QuizResultResponse {
    private Long id;
    private Long skillId;
    private Integer score;
    private Integer correctAnswers;
    private Integer totalQuestions;
    private SkillLevel achievedLevel;
    private String levelDisplay;
    private LocalDateTime completedAt;
}

// ============================================
