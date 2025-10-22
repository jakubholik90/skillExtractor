// QuizController.java
package com.skillextractor.controller;

import com.skillextractor.dto.QuizRequest;
import com.skillextractor.dto.QuizResponse;
import com.skillextractor.dto.QuizResultResponse;
import com.skillextractor.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/generate/{skillId}")
    public ResponseEntity<QuizResponse> generateQuiz(@PathVariable Long skillId) {
        QuizResponse quiz = quizService.generateQuiz(skillId);
        return ResponseEntity.ok(quiz);
    }

    @PostMapping("/submit")
    public ResponseEntity<QuizResultResponse> submitQuiz(@RequestBody QuizRequest request) {
        QuizResultResponse result = quizService.submitQuiz(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/results/{skillId}")
    public ResponseEntity<QuizResultResponse> getLatestResult(@PathVariable Long skillId) {
        QuizResultResponse result = quizService.getLatestResult(skillId);
        return ResponseEntity.ok(result);
    }
}