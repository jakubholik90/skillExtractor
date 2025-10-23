// ProjectRepository.java - FIXED
package com.skillextractor.repository;

import com.skillextractor.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // ✅ POPRAWIONE: user.id zamiast userId (bo pole nazywa się 'user', nie 'userId')
    @Query("SELECT p FROM Project p WHERE p.user.id = :userId ORDER BY p.uploadedAt DESC")
    List<Project> findByUserIdOrderByUploadedAtDesc(@Param("userId") Long userId);

    // Alternatywna metoda - Spring automatycznie generuje query
    @Query("SELECT p FROM Project p WHERE p.user.id = :userId")
    List<Project> findByUserId(@Param("userId") Long userId);
}