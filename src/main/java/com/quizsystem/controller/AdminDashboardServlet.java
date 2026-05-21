package com.quizsystem.controller;

import com.quizsystem.dao.CategoryDAO;
import com.quizsystem.dao.QuizDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Admin dashboard servlet — shows simple counts and links to the management pages.
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Simple counts to display on the dashboard.
        int totalCategories = new CategoryDAO().getAllCategories().size();
        int totalQuizzes    = new QuizDAO().getAllQuizzes().size();

        request.setAttribute("totalCategories", totalCategories);
        request.setAttribute("totalQuizzes", totalQuizzes);

        request.getRequestDispatcher("/pages/admin/dashboard.jsp").forward(request, response);
    }
}
