package com.quizsystem.controller;

import com.quizsystem.dao.AttemptDAO;
import com.quizsystem.model.Attempt;
import com.quizsystem.model.User;
import com.quizsystem.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet for the result page.
 *
 * GET /result                   : show the user's full attempt history
 * GET /result?score=&total=     : show the most recent score plus the history
 */
@WebServlet("/result")
public class ResultServlet extends HttpServlet {

    private final AttemptDAO attemptDAO = new AttemptDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = SessionUtil.getLoggedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Optional: show the just-completed score at the top of the page
        String score = request.getParameter("score");
        String total = request.getParameter("total");
        if (score != null && total != null) {
            request.setAttribute("latestScore", score);
            request.setAttribute("latestTotal", total);
        }

        List<Attempt> attempts = attemptDAO.getAttemptsByUser(user.getUserId());
        request.setAttribute("attempts", attempts);

        request.getRequestDispatcher("/pages/result.jsp").forward(request, response);
    }
}
