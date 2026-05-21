package com.quizsystem.dao;

import com.quizsystem.model.Attempt;
import com.quizsystem.util.DbConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for the attempts + answers tables.
 */
public class AttemptDAO {

    // ------------------------------------------------------------------
    // Save attempt WITH answers map — matches AttemptQuizServlet signature:
    // saveAttempt(userId, quizId, score, totalMarks, answers)
    //
    // answers = Map<questionId, selectedOptionId>  (-1 = skipped)
    // Inserts one row into `attempts` and one row per question into `answers`.
    // Returns the new attempt_id, or -1 on failure.
    // ------------------------------------------------------------------
    public int saveAttempt(int userId, int quizId, int score, int totalMarks,
                           Map<Integer, Integer> answers) {

        Connection conn = null;
        try {
            conn = DbConfig.getConnection();
            conn.setAutoCommit(false);  // transaction — both inserts succeed or both roll back

            // 1) Insert into attempts
            int attemptId = insertAttempt(conn, userId, quizId, score, totalMarks);
            if (attemptId == -1) {
                conn.rollback();
                return -1;
            }

            // 2) Insert each answer
            String answerSql = "INSERT INTO answers (attempt_id, question_id, selected_option_id) "
                    + "VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(answerSql)) {
                for (Map.Entry<Integer, Integer> entry : answers.entrySet()) {
                    ps.setInt(1, attemptId);
                    ps.setInt(2, entry.getKey());

                    int optionId = entry.getValue();
                    if (optionId == -1) {
                        ps.setNull(3, Types.INTEGER);   // user skipped this question
                    } else {
                        ps.setInt(3, optionId);
                    }
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
            return attemptId;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            return -1;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    // ------------------------------------------------------------------
    // Save attempt WITHOUT answers map — used by ProfileServlet / other places
    // ------------------------------------------------------------------
    public int saveAttempt(int userId, int quizId, int score, int totalMarks) {
        try (Connection conn = DbConfig.getConnection()) {
            return insertAttempt(conn, userId, quizId, score, totalMarks);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ------------------------------------------------------------------
    // Private helper — inserts one row into attempts, returns generated PK
    // ------------------------------------------------------------------
    private int insertAttempt(Connection conn, int userId, int quizId,
                              int score, int totalMarks) throws SQLException {
        String sql = "INSERT INTO attempts (user_id, quiz_id, score, total_marks) "
                + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setInt(2, quizId);
            ps.setInt(3, score);
            ps.setInt(4, totalMarks);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        }
        return -1;
    }

    // ------------------------------------------------------------------
    // Get all attempts by a user (for profile Quiz History tab)
    // Joins quizzes + categories for display names.
    // ------------------------------------------------------------------
    public List<Attempt> getAttemptsByUser(int userId) {
        List<Attempt> list = new ArrayList<>();

        String sql = "SELECT a.attempt_id, a.user_id, a.quiz_id, "
                + "       a.score, a.total_marks, a.attempt_date, "
                + "       q.title         AS quiz_title, "
                + "       c.category_name "
                + "FROM   attempts   a "
                + "JOIN   quizzes    q ON a.quiz_id      = q.quiz_id "
                + "JOIN   categories c ON q.category_id  = c.category_id "
                + "WHERE  a.user_id = ? "
                + "ORDER  BY a.attempt_date DESC";

        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Attempt a = new Attempt();
                a.setAttemptId(rs.getInt("attempt_id"));
                a.setUserId(rs.getInt("user_id"));
                a.setQuizId(rs.getInt("quiz_id"));
                a.setScore(rs.getInt("score"));
                a.setTotalMarks(rs.getInt("total_marks"));
                a.setAttemptDate(rs.getTimestamp("attempt_date"));
                a.setQuizTitle(rs.getString("quiz_title"));
                a.setCategoryName(rs.getString("category_name"));
                list.add(a);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ------------------------------------------------------------------
    // Get a single attempt by ID (used by ResultServlet)
    // ------------------------------------------------------------------
    public Attempt getAttemptById(int attemptId) {
        String sql = "SELECT a.*, q.title AS quiz_title, c.category_name "
                + "FROM   attempts   a "
                + "JOIN   quizzes    q ON a.quiz_id     = q.quiz_id "
                + "JOIN   categories c ON q.category_id = c.category_id "
                + "WHERE  a.attempt_id = ?";

        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, attemptId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Attempt a = new Attempt();
                a.setAttemptId(rs.getInt("attempt_id"));
                a.setUserId(rs.getInt("user_id"));
                a.setQuizId(rs.getInt("quiz_id"));
                a.setScore(rs.getInt("score"));
                a.setTotalMarks(rs.getInt("total_marks"));
                a.setAttemptDate(rs.getTimestamp("attempt_date"));
                a.setQuizTitle(rs.getString("quiz_title"));
                a.setCategoryName(rs.getString("category_name"));
                return a;
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ------------------------------------------------------------------
    // Get all attempts (admin — view all results)
    // ------------------------------------------------------------------
    public List<Attempt> getAllAttempts() {
        List<Attempt> list = new ArrayList<>();

        String sql = "SELECT a.attempt_id, a.user_id, a.score, a.total_marks, "
                + "       a.attempt_date, "
                + "       u.full_name, u.email, "
                + "       q.title         AS quiz_title, "
                + "       c.category_name "
                + "FROM   attempts   a "
                + "JOIN   users      u ON a.user_id      = u.user_id "
                + "JOIN   quizzes    q ON a.quiz_id       = q.quiz_id "
                + "JOIN   categories c ON q.category_id   = c.category_id "
                + "ORDER  BY a.attempt_date DESC";

        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Attempt a = new Attempt();
                a.setAttemptId(rs.getInt("attempt_id"));
                a.setUserId(rs.getInt("user_id"));
                a.setScore(rs.getInt("score"));
                a.setTotalMarks(rs.getInt("total_marks"));
                a.setAttemptDate(rs.getTimestamp("attempt_date"));
                a.setUserFullName(rs.getString("full_name"));
                a.setUserEmail(rs.getString("email"));
                a.setQuizTitle(rs.getString("quiz_title"));
                a.setCategoryName(rs.getString("category_name"));
                list.add(a);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
}