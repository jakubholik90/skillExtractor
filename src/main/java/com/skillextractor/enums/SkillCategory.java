package com.skillextractor.enums;

/**
 * Predefined skill categories for Java project analysis.
 * These categories are hardcoded and used by the LLM to classify extracted skills.
 */
public enum SkillCategory {
    SYNTAX_BASICS("Basic Syntax & Core Concepts",
            "Fundamental Java syntax including loops, conditionals, and basic language features"),

    STREAMS_LAMBDAS("Stream API & Lambdas",
            "Functional programming with Stream API and lambda expressions"),

    OOP("Object Oriented Programming",
            "OOP principles: encapsulation, inheritance, polymorphism, abstraction"),

    COLLECTIONS("Collections & Data Structures",
            "Java Collections Framework: List, Set, Map, Queue and custom data structures"),

    EXCEPTION_HANDLING("Exception Handling",
            "Try-catch blocks, custom exceptions, and error management"),

    FILE_HANDLING("File Operations",
            "Reading/writing files: TXT, CSV, PDF, and file I/O operations"),

    DATABASE("Database Operations",
            "JDBC, JPA, Hibernate, SQL queries, and database connectivity"),

    FRAMEWORKS("Frameworks & Libraries",
            "Spring Boot, Hibernate, and other Java frameworks"),

    CLEAN_CODE("Clean Code Principles",
            "Code quality, naming conventions, SOLID principles, refactoring"),

    ALGORITHMS("Algorithms & Problem Solving",
            "Sorting, searching, recursion, and algorithmic thinking"),

    TESTING("Testing",
            "JUnit, Mockito, unit tests, integration tests, test-driven development"),

    BUILD_TOOLS("Build Tools & Dependencies",
            "Maven, Gradle, dependency management, build lifecycle"),

    DESIGN_PATTERNS("Design Patterns",
            "Singleton, Factory, Strategy, Observer and other design patterns"),

    CONCURRENCY("Multithreading & Concurrency",
            "Threads, ExecutorService, synchronization, concurrent collections"),

    REST_API("REST API Development",
            "RESTful services, HTTP methods, API design, JSON processing");

    private final String displayName;
    private final String description;

    SkillCategory(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns formatted list of all categories for LLM prompts
     */
    public static String getAllCategoriesForPrompt() {
        StringBuilder sb = new StringBuilder("Available skill categories:\n");
        for (SkillCategory category : values()) {
            sb.append("- ").append(category.name())
                    .append(": ").append(category.displayName)
                    .append(" (").append(category.description).append(")\n");
        }
        return sb.toString();
    }
}