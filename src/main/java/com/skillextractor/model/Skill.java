package com.skillextractor.model;

import com.skillextractor.enums.SkillCategory;
import com.skillextractor.enums.SkillLevel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkillCategory category;

    @Column(length = 500)
    private String description; // Max 1 sentence

    @Column(columnDefinition = "TEXT")
    private String exampleUsage; // Code snippet from project

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private SkillLevel level = SkillLevel.UNKNOWN;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isGeneral = false; // True for category-wide skills

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<QuizResult> quizResults = new ArrayList<>();

    // Helper method
    public void updateLevel(int quizScore) {
        this.level = SkillLevel.fromScore(quizScore);
    }
}