package com.skillextractor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    @Column(name = "analyzed_files", columnDefinition = "TEXT")
    private String analyzedFiles; // Comma-separated list of file names

    @Column(name = "total_files")
    private Integer totalFiles;

    @Column(name = "total_size_kb")
    private Long totalSizeKb;

    // ✅ CRITICAL: Ignore user relationship in JSON to prevent infinite recursion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore  // Also ignore skills to prevent recursion
    private List<Skill> skills = new ArrayList<>();

    // Helper method to add skill
    public void addSkill(Skill skill) {
        skills.add(skill);
        skill.setProject(this);
    }

    // ✅ Expose user ID for frontend without loading full user object
    @JsonProperty("userId")
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }
}