package com.skillextractor.enums;

/**
 * Skill proficiency levels based on quiz performance
 */
public enum SkillLevel {
    UNKNOWN("Applied without knowledge", 0, 40, "🟥"),
    BASIC("Applied with basic knowledge", 41, 60, "🟨"),
    GOOD("Applied with good knowledge", 61, 85, "🟩"),
    EXPERT("Applied at expert level", 86, 100, "🟦");

    private final String description;
    private final int minScore;
    private final int maxScore;
    private final String emoji;

    SkillLevel(String description, int minScore, int maxScore, String emoji) {
        this.description = description;
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.emoji = emoji;
    }

    public String getDescription() {
        return description;
    }

    public int getMinScore() {
        return minScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public String getEmoji() {
        return emoji;
    }

    /**
     * Determine skill level based on quiz score percentage
     */
    public static SkillLevel fromScore(int scorePercentage) {
        for (SkillLevel level : values()) {
            if (scorePercentage >= level.minScore && scorePercentage <= level.maxScore) {
                return level;
            }
        }
        return UNKNOWN; // Default fallback
    }

    public String getDisplayText() {
        return emoji + " " + name() + " (" + minScore + "-" + maxScore + "%)";
    }
}