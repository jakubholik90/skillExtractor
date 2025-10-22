package com.skillextractor.exception;

/**
 * Custom exception classes for SkillExtractor application
 */

// Base exception
public class SkillExtractorException extends RuntimeException {
    public SkillExtractorException(String message) {
        super(message);
    }

    public SkillExtractorException(String message, Throwable cause) {
        super(message, cause);
    }
}

// User-related exceptions
class UserNotFoundException extends SkillExtractorException {
    public UserNotFoundException(String username) {
        super("User not found: " + username);
    }

    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }
}

class UserAlreadyExistsException extends SkillExtractorException {
    public UserAlreadyExistsException(String field, String value) {
        super(field + " already exists: " + value);
    }
}

// Project-related exceptions
class ProjectNotFoundException extends SkillExtractorException {
    public ProjectNotFoundException(Long id) {
        super("Project not found with id: " + id);
    }
}

class ProjectUploadException extends SkillExtractorException {
    public ProjectUploadException(String message) {
        super("Project upload failed: " + message);
    }
}

class FileSizeLimitExceededException extends SkillExtractorException {
    public FileSizeLimitExceededException(long maxSizeMb) {
        super("Total file size exceeds limit of " + maxSizeMb + "MB");
    }
}

class FileCountLimitExceededException extends SkillExtractorException {
    public FileCountLimitExceededException(int maxFiles) {
        super("Maximum " + maxFiles + " files allowed");
    }
}

// Skill-related exceptions
class SkillNotFoundException extends SkillExtractorException {
    public SkillNotFoundException(Long id) {
        super("Skill not found with id: " + id);
    }
}

class SkillAnalysisException extends SkillExtractorException {
    public SkillAnalysisException(String message) {
        super("Skill analysis failed: " + message);
    }

    public SkillAnalysisException(String message, Throwable cause) {
        super("Skill analysis failed: " + message, cause);
    }
}

// Quiz-related exceptions
class QuizNotFoundException extends SkillExtractorException {
    public QuizNotFoundException(Long skillId) {
        super("No quiz results found for skill: " + skillId);
    }
}

class QuizGenerationException extends SkillExtractorException {
    public QuizGenerationException(String message) {
        super("Quiz generation failed: " + message);
    }

    public QuizGenerationException(String message, Throwable cause) {
        super("Quiz generation failed: " + message, cause);
    }
}

// OpenAI API exceptions
class OpenAIApiException extends SkillExtractorException {
    public OpenAIApiException(String message) {
        super("OpenAI API error: " + message);
    }

    public OpenAIApiException(String message, Throwable cause) {
        super("OpenAI API error: " + message, cause);
    }
}

// Authorization exceptions
class UnauthorizedAccessException extends SkillExtractorException {
    public UnauthorizedAccessException(String resource) {
        super("Unauthorized access to: " + resource);
    }
}

// Validation exceptions
class InvalidRequestException extends SkillExtractorException {
    public InvalidRequestException(String message) {
        super("Invalid request: " + message);
    }
}