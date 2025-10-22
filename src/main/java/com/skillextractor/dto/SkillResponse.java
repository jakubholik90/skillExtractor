// SkillResponse.java
package com.skillextractor.dto;

import com.skillextractor.enums.SkillCategory;
import com.skillextractor.enums.SkillLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SkillResponse {
    private Long id;
    private String name;
    private SkillCategory category;
    private String categoryDisplayName;
    private String description;
    private String exampleUsage;
    private SkillLevel level;
    private String levelDisplay;
    private Boolean isGeneral;
    private String projectName;
    private LocalDateTime createdAt;
}

// ============================================

