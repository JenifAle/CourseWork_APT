package com.quizsystem.controller;

import com.quizsystem.dao.UserDAO;
import com.quizsystem.model.User;
import com.quizsystem.util.CookieUtil;
import com.quizsystem.util.PasswordUtil;
import com.quizsystem.util.SessionUtil;
import com.quizsystem.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet for handling user login.
 *
 * GET  /login : show login form
 * POST /login : verify credentials, create session, redirect by role
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // If already logged in, send straight to home
        if (SessionUtil.isLoggedIn(request)) {
            redirectByRole(request, response);
            return;
        }
        request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email      = request.getParameter("email");
        String password   = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        if (ValidationUtil.isNullOrEmpty(email) || ValidationUtil.isNullOrEmpty(password)) {
            forwardWithError(request, response, "Please enter both email and password.");
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.findByEmail(email.trim());

        if (user == null || !PasswordUtil.checkPassword(password, user.getPassword())) {
            forwardWithError(request, response, "Invalid email or password.");
            return;
        }

        // Create session and store the user (Lecture 7)
        SessionUtil.setLoggedUser(request, user);

        // "Remember me" cookie stores the email for 7 days
        if ("on".equals(rememberMe)) {
            CookieUtil.addCookie(response, "rememberEmail", user.getEmail(), 60 * 60 * 24 * 7);
        } else {
            CookieUtil.deleteCookie(response, "rememberEmail");
        }

        redirectByRole(request, response);
    }

    private void redirectByRole(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (SessionUtil.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response,
                                  String message) throws ServletException, IOException {
        request.setAttribute("error", message);
        request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
    }
}
