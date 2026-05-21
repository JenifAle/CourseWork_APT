package com.quizsystem.controller;

import com.quizsystem.dao.AttemptDAO;
import com.quizsystem.model.Attempt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Admin-only servlet that lists all quiz attempts by every user.
 */
@WebServlet("/admin/results")
public class AdminViewResultsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Attempt> attempts = new AttemptDAO().getAllAttempts();
        request.setAttribute("attempts", attempts);
        request.getRequestDispatcher("/pages/admin/viewResults.jsp").forward(request, response);
    }
}
