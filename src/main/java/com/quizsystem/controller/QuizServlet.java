package com.quizsystem.controller;

import com.quizsystem.dao.CategoryDAO;
import com.quizsystem.dao.QuizDAO;
import com.quizsystem.model.Quiz;
import com.quizsystem.model.User;
import com.quizsystem.util.SessionUtil;
import com.quizsystem.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * CRUD controller for quizzes.
 *
 * Routes:
 *   GET  /admin/quizzes                : list all
 *   POST /admin/quizzes?action=add     : add new
 *   POST /admin/quizzes?action=update  : update
 *   POST /admin/quizzes?action=delete  : delete
 */
@WebServlet("/admin/quizzes")
public class QuizServlet extends HttpServlet {

    private final QuizDAO quizDAO         = new QuizDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("quizzes", quizDAO.getAllQuizzes());
        request.setAttribute("categories", categoryDAO.getAllCategories());
        request.getRequestDispatcher("/pages/admin/manageQuiz.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String action = request.getParameter("action");
        if (action == null) action = "add";

        try {
            switch (action) {
                case "add":    handleAdd(request);    break;
                case "update": handleUpdate(request); break;
                case "delete": handleDelete(request); break;
                default:       request.getSession().setAttribute("error", "Unknown action.");
            }
        } catch (Exception ex) {
            request.getSession().setAttribute("error", "Operation failed: " + ex.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/quizzes");
    }

    private void handleAdd(HttpServletRequest request) {
        String title = request.getParameter("title");
        String desc  = request.getParameter("description");
        String catIdStr  = request.getParameter("categoryId");
        String timeStr   = request.getParameter("timeLimit");

        if (ValidationUtil.isNullOrEmpty(title) || ValidationUtil.isNullOrEmpty(catIdStr)
                || ValidationUtil.isNullOrEmpty(timeStr)) {
            request.getSession().setAttribute("error", "Title, category, and time limit are required.");
            return;
        }

        User admin = SessionUtil.getLoggedUser(request);

        Quiz q = new Quiz();
        q.setTitle(title.trim());
        q.setDescription(desc != null ? desc.trim() : "");
        q.setCategoryId(Integer.parseInt(catIdStr));
        q.setTimeLimit(Integer.parseInt(timeStr));
        q.setCreatedBy(admin.getUserId());

        if (quizDAO.insertQuiz(q)) {
            request.getSession().setAttribute("success", "Quiz added successfully.");
        } else {
            request.getSession().setAttribute("error", "Failed to add quiz.");
        }
    }

    private void handleUpdate(HttpServletRequest request) {
        int id        = Integer.parseInt(request.getParameter("quizId"));
        String title  = request.getParameter("title");
        String desc   = request.getParameter("description");
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        int timeLimit  = Integer.parseInt(request.getParameter("timeLimit"));

        if (ValidationUtil.isNullOrEmpty(title)) {
            request.getSession().setAttribute("error", "Title is required.");
            return;
        }

        Quiz q = new Quiz();
        q.setQuizId(id);
        q.setTitle(title.trim());
        q.setDescription(desc != null ? desc.trim() : "");
        q.setCategoryId(categoryId);
        q.setTimeLimit(timeLimit);

        if (quizDAO.updateQuiz(q)) {
            request.getSession().setAttribute("success", "Quiz updated successfully.");
        } else {
            request.getSession().setAttribute("error", "Failed to update quiz.");
        }
    }

    private void handleDelete(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("quizId"));
        if (quizDAO.deleteQuiz(id)) {
            request.getSession().setAttribute("success", "Quiz deleted successfully.");
        } else {
            request.getSession().setAttribute("error", "Failed to delete quiz.");
        }
    }
}
