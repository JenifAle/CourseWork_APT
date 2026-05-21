package com.quizsystem.model;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO class representing a question with its options.
 */
public class Question {

    private int questionId;
    private int quizId;
    private String questionText;
    private int marks;
    private List<Option> options = new ArrayList<>();

    public Question() {
    }

    public Question(int quizId, String questionText, int marks) {
        this.quizId       = quizId;
        this.questionText = questionText;
        this.marks        = marks;
    }

    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }

    public int getQuizId() { return quizId; }
    public void setQuizId(int quizId) { this.quizId = quizId; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public int getMarks() { return marks; }
    public void setMarks(int marks) { this.marks = marks; }

    public List<Option> getOptions() { return options; }
    public void setOptions(List<Option> options) { this.options = options; }
}
