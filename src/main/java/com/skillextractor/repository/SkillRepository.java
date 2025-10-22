// SkillRepository.java
package com.skillextractor.repository;

import com.skillextractor.enums.SkillCategory;
import com.skillextractor.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByUserId(Long userId);
    List<Skill> findByUserIdAndCategory(Long userId, SkillCategory category);
    List<Skill> findByProjectId(Long projectId);
    List<Skill> findByUserIdAndIsGeneral(Long userId, Boolean isGeneral);
}

// ============================================

