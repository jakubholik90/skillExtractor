package com.skillextractor.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(nullable = false)
    private Integer score; // Percentage (0-100)

    @Column(columnDefinition = "TEXT")
    private String answersJson; // JSON with questions and user answers

    @Column(nullable = false)
    private Integer totalQuestions;

    @Column(nullable = false)
    private Integer correctAnswers;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime completedAt;

    // Helper method to calculate score
    public static int calculateScore(int correct, int total) {
        return (int) Math.round((correct * 100.0) / total);
    }
}