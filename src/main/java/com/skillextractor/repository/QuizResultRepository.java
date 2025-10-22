// QuizResultRepository.java
package com.skillextractor.repository;

import com.skillextractor.model.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    List<QuizResult> findBySkillIdOrderByCompletedAtDesc(Long skillId);
    Optional<QuizResult> findFirstBySkillIdOrderByCompletedAtDesc(Long skillId);
}