package com.quizsystem.controller;

import com.quizsystem.dao.UserDAO;
import com.quizsystem.model.User;
import com.quizsystem.util.PasswordUtil;
import com.quizsystem.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet for handling user registration.
 *
 * GET  /register : show the registration form
 * POST /register : validate input, hash password, insert user
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName        = request.getParameter("fullName");
        String email           = request.getParameter("email");
        String phone           = request.getParameter("phone");
        String password        = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // ---- Server-side validation ----
        if (!ValidationUtil.isValidName(fullName)) {
            forwardWithError(request, response, "Full name must contain only letters (3–50 chars).");
            return;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            forwardWithError(request, response, "Please enter a valid email address.");
            return;
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            forwardWithError(request, response, "Phone number must be exactly 10 digits.");
            return;
        }
        if (!ValidationUtil.isValidPassword(password)) {
            forwardWithError(request, response, "Password must be at least 6 characters and contain a letter and a digit.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            forwardWithError(request, response, "Passwords do not match.");
            return;
        }

        UserDAO userDAO = new UserDAO();
        if (userDAO.emailExists(email)) {
            forwardWithError(request, response, "An account with this email already exists.");
            return;
        }

        // ---- Hash password and insert user (Lecture 5: Bcrypt) ----
        String hashed = PasswordUtil.hashPassword(password);
        User user = new User(fullName.trim(), email.trim(), phone.trim(), hashed, "user");

        if (userDAO.insertUser(user)) {
            request.setAttribute("success", "Registration successful! Please log in.");
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
        } else {
            forwardWithError(request, response, "Registration failed. Please try again.");
        }
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response,
                                  String message) throws ServletException, IOException {
        request.setAttribute("error", message);
        request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
    }
}
