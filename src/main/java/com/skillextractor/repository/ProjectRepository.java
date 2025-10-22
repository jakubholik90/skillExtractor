// ProjectRepository.java
package com.skillextractor.repository;

import com.skillextractor.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUserId(Long userId);
    List<Project> findByUserIdOrderByUploadedAtDesc(Long userId);
}

// ============================================

