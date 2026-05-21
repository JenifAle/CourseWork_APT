package com.quizsystem.model;

/**
 * POJO class representing a quiz.
 */
public class Quiz {

    private int quizId;
    private String title;
    private String description;
    private int categoryId;
    private String categoryName;   // populated by JOIN in DAO (display only)
    private int timeLimit;         // in minutes
    private int createdBy;

    public Quiz() {
    }

    public Quiz(String title, String description, int categoryId, int timeLimit, int createdBy) {
        this.title       = title;
        this.description = description;
        this.categoryId  = categoryId;
        this.timeLimit   = timeLimit;
        this.createdBy   = createdBy;
    }

    public int getQuizId() { return quizId; }
    public void setQuizId(int quizId) { this.quizId = quizId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public int getTimeLimit() { return timeLimit; }
    public void setTimeLimit(int timeLimit) { this.timeLimit = timeLimit; }

    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }
}
