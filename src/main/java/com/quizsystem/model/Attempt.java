package com.quizsystem.model;

import java.sql.Timestamp;

/**
 * Model for the attempts table.
 * Also carries joined fields (not DB columns): quizTitle, categoryName,
 * userFullName, userEmail — populated by AttemptDAO queries.
 */
public class Attempt {

    private int       attemptId;
    private int       userId;
    private int       quizId;
    private int       score;
    private int       totalMarks;
    private Timestamp attemptDate;

    // Joined fields — populated by AttemptDAO, not stored in DB
    private String quizTitle;
    private String categoryName;
    private String userFullName;   // used by admin getAllAttempts()
    private String userEmail;      // used by admin getAllAttempts()

    // ── Constructors ────────────────────────────────────────────────────

    public Attempt() {}

    public Attempt(int userId, int quizId, int score, int totalMarks) {
        this.userId     = userId;
        this.quizId     = quizId;
        this.score      = score;
        this.totalMarks = totalMarks;
    }

    // ── Getters & Setters ────────────────────────────────────────────────

    public int getAttemptId()               { return attemptId; }
    public void setAttemptId(int attemptId) { this.attemptId = attemptId; }

    public int getUserId()               { return userId; }
    public void setUserId(int userId)    { this.userId = userId; }

    public int getQuizId()               { return quizId; }
    public void setQuizId(int quizId)    { this.quizId = quizId; }

    public int getScore()                { return score; }
    public void setScore(int score)      { this.score = score; }

    public int getTotalMarks()                   { return totalMarks; }
    public void setTotalMarks(int totalMarks)    { this.totalMarks = totalMarks; }

    public Timestamp getAttemptDate()                    { return attemptDate; }
    public void setAttemptDate(Timestamp attemptDate)    { this.attemptDate = attemptDate; }

    public String getQuizTitle()                 { return quizTitle; }
    public void setQuizTitle(String quizTitle)   { this.quizTitle = quizTitle; }

    public String getCategoryName()                      { return categoryName; }
    public void setCategoryName(String categoryName)     { this.categoryName = categoryName; }

    public String getUserFullName()                      { return userFullName; }
    public void setUserFullName(String userFullName)     { this.userFullName = userFullName; }

    public String getUserEmail()                         { return userEmail; }
    public void setUserEmail(String userEmail)           { this.userEmail = userEmail; }

    // Convenience: percentage score (0–100)
    public int getPercentage() {
        if (totalMarks == 0) return 0;
        return score * 100 / totalMarks;
    }
}
