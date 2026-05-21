package com.quizsystem.dao;

import com.quizsystem.model.Quiz;
import com.quizsystem.util.DbConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for the `quizzes` table.
 */
public class QuizDAO {

    public boolean insertQuiz(Quiz q) {
        String sql = "INSERT INTO quizzes (title, description, category_id, time_limit, created_by) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, q.getTitle());
            ps.setString(2, q.getDescription());
            ps.setInt(3, q.getCategoryId());
            ps.setInt(4, q.getTimeLimit());
            ps.setInt(5, q.getCreatedBy());
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<Quiz> getAllQuizzes() {
        List<Quiz> list = new ArrayList<>();
        // JOIN to also fetch the category name for display
        String sql = "SELECT q.*, c.category_name " +
                     "FROM quizzes q JOIN categories c ON q.category_id = c.category_id " +
                     "ORDER BY q.title";
        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public Quiz getQuizById(int quizId) {
        String sql = "SELECT q.*, c.category_name " +
                     "FROM quizzes q JOIN categories c ON q.category_id = c.category_id " +
                     "WHERE q.quiz_id = ?";
        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quizId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean updateQuiz(Quiz q) {
        String sql = "UPDATE quizzes SET title = ?, description = ?, category_id = ?, time_limit = ? " +
                     "WHERE quiz_id = ?";
        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, q.getTitle());
            ps.setString(2, q.getDescription());
            ps.setInt(3, q.getCategoryId());
            ps.setInt(4, q.getTimeLimit());
            ps.setInt(5, q.getQuizId());
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteQuiz(int quizId) {
        String sql = "DELETE FROM quizzes WHERE quiz_id = ?";
        try (Connection conn = DbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quizId);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private Quiz mapRow(ResultSet rs) throws SQLException {
        Quiz q = new Quiz();
        q.setQuizId(rs.getInt("quiz_id"));
        q.setTitle(rs.getString("title"));
        q.setDescription(rs.getString("description"));
        q.setCategoryId(rs.getInt("category_id"));
        q.setCategoryName(rs.getString("category_name"));
        q.setTimeLimit(rs.getInt("time_limit"));
        q.setCreatedBy(rs.getInt("created_by"));
        return q;
    }
}
