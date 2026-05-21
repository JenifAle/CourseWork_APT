package com.quizsystem.dao;

import com.quizsystem.model.Option;
import com.quizsystem.model.Question;
import com.quizsystem.util.DbConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for the `questions` and `options` tables.
 *
 * A question is created together with its options in one logical unit.
 */
public class QuestionDAO {

    /**
     * Inserts a question and all of its options.
     * Returns the new question_id, or -1 if insertion failed.
     */
    public int insertQuestionWithOptions(Question q) {
        String insertQ = "INSERT INTO questions (quiz_id, question_text, marks) VALUES (?, ?, ?)";
        String insertO = "INSERT INTO options (question_id, option_text, is_correct) VALUES (?, ?, ?)";

        try (Connection conn = DbConfig.getConnection()) {
            // Step 1: insert the question and get the generated question_id
            int questionId;
            try (PreparedStatement ps = conn.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, q.getQuizId());
                ps.setString(2, q.getQuestionText());
                ps.setInt(3, q.getMarks());
                if (ps.executeUpdate() == 0) return -1;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) return -1;
                    questionId = rs.getInt(1);
                }
            }

            // Step 2: insert all options
            try (PreparedStatement ps = conn.prepareStatement(insertO)) {
                for (Option opt : q.getOptions()) {
                    ps.setInt(1, questionId);
                    ps.setString(2, opt.getOptionText());
                    ps.setBoolean(3, opt.isCorrect());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            return questionId;

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /** Returns all questions (with their options) for a given quiz. */
    public List<Question> getQuestionsByQuiz(int quizId) {
        List<Question> list = new ArrayList<>();
        String sql = "SELECT * FROM questions WHERE quiz_id = ? ORDER BY question_id";
        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quizId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Question q = new Question();
                    q.setQuestionId(rs.getInt("question_id"));
                    q.setQuizId(rs.getInt("quiz_id"));
                    q.setQuestionText(rs.getString("question_text"));
                    q.setMarks(rs.getInt("marks"));
                    q.setOptions(getOptionsByQuestion(q.getQuestionId()));
                    list.add(q);
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /** Returns all options for a given question. */
    public List<Option> getOptionsByQuestion(int questionId) {
        List<Option> list = new ArrayList<>();
        String sql = "SELECT * FROM options WHERE question_id = ? ORDER BY option_id";
        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, questionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Option o = new Option();
                    o.setOptionId(rs.getInt("option_id"));
                    o.setQuestionId(rs.getInt("question_id"));
                    o.setOptionText(rs.getString("option_text"));
                    o.setCorrect(rs.getBoolean("is_correct"));
                    list.add(o);
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public boolean deleteQuestion(int questionId) {
        // ON DELETE CASCADE on the options table will remove its options automatically
        String sql = "DELETE FROM questions WHERE question_id = ?";
        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, questionId);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /** Returns the option_id of the correct option for a question. */
    public int getCorrectOptionId(int questionId) {
        String sql = "SELECT option_id FROM options WHERE question_id = ? AND is_correct = TRUE";
        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, questionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("option_id");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return -1;
    }
}
