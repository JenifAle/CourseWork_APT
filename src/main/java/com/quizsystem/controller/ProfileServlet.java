package com.quizsystem.controller;

import com.quizsystem.dao.AttemptDAO;
import com.quizsystem.dao.UserDAO;
import com.quizsystem.model.Attempt;
import com.quizsystem.model.User;
import com.quizsystem.util.PasswordUtil;
import com.quizsystem.util.SessionUtil;
import com.quizsystem.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for the user profile page.
 *
 * GET  /profile          → show Account Info tab
 * GET  /profile?tab=edit → show Edit Profile tab
 * GET  /profile?tab=password → show Change Password tab
 * GET  /profile?tab=history  → show Quiz History tab
 *
 * POST /profile (action=updateProfile)  → save name/email/phone
 * POST /profile (action=changePassword) → change password
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Must be logged in
        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = SessionUtil.getLoggedUser(request);

        // Load attempt history when history tab is requested
        String tab = request.getParameter("tab");
        if ("history".equals(tab)) {
            AttemptDAO attemptDAO = new AttemptDAO();
            List<Attempt> attempts = attemptDAO.getAttemptsByUser(user.getUserId());
            request.setAttribute("attempts", attempts);

            // Best score percentage across all attempts
            int best = 0;
            for (Attempt a : attempts) {
                if (a.getTotalMarks() > 0) {
                    int pct = a.getScore() * 100 / a.getTotalMarks();
                    if (pct > best) best = pct;
                }
            }
            request.setAttribute("totalAttempts", attempts.size());
            request.setAttribute("bestScore", best);
        } else {
            // Still show mini-stats on other tabs
            AttemptDAO attemptDAO = new AttemptDAO();
            List<Attempt> attempts = attemptDAO.getAttemptsByUser(user.getUserId());
            int best = 0;
            for (Attempt a : attempts) {
                if (a.getTotalMarks() > 0) {
                    int pct = a.getScore() * 100 / a.getTotalMarks();
                    if (pct > best) best = pct;
                }
            }
            request.setAttribute("totalAttempts", attempts.size());
            request.setAttribute("bestScore", best);
        }

        request.getRequestDispatcher("/pages/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SessionUtil.isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        if ("updateProfile".equals(action)) {
            handleUpdateProfile(request, response);
        } else if ("changePassword".equals(action)) {
            handleChangePassword(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/profile");
        }
    }

    // ------------------------------------------------------------------
    // Update name / email / phone
    // ------------------------------------------------------------------
    private void handleUpdateProfile(HttpServletRequest request,
                                     HttpServletResponse response)
            throws IOException {

        String fullName = request.getParameter("fullName");
        String email    = request.getParameter("email");
        String phone    = request.getParameter("phone");

        if (ValidationUtil.isNullOrEmpty(fullName)
                || ValidationUtil.isNullOrEmpty(email)
                || ValidationUtil.isNullOrEmpty(phone)) {
            SessionUtil.setError(request, "All fields are required.");
            response.sendRedirect(request.getContextPath() + "/profile?tab=edit");
            return;
        }

        User user = SessionUtil.getLoggedUser(request);
        user.setFullName(fullName.trim());
        user.setEmail(email.trim());
        user.setPhone(phone.trim());

        UserDAO userDAO = new UserDAO();
        boolean updated = userDAO.updateProfile(user);

        if (updated) {
            // Refresh session with latest data
            SessionUtil.setLoggedUser(request, user);
            SessionUtil.setSuccess(request, "Profile updated successfully.");
        } else {
            SessionUtil.setError(request, "Could not update profile. Email may already be in use.");
        }

        response.sendRedirect(request.getContextPath() + "/profile?tab=edit");
    }

    // ------------------------------------------------------------------
    // Change password
    // ------------------------------------------------------------------
    private void handleChangePassword(HttpServletRequest request,
                                      HttpServletResponse response)
            throws IOException {

        String currentPassword  = request.getParameter("currentPassword");
        String newPassword      = request.getParameter("newPassword");
        String confirmPassword  = request.getParameter("confirmPassword");

        if (ValidationUtil.isNullOrEmpty(currentPassword)
                || ValidationUtil.isNullOrEmpty(newPassword)
                || ValidationUtil.isNullOrEmpty(confirmPassword)) {
            SessionUtil.setError(request, "All password fields are required.");
            response.sendRedirect(request.getContextPath() + "/profile?tab=password");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            SessionUtil.setError(request, "New passwords do not match.");
            response.sendRedirect(request.getContextPath() + "/profile?tab=password");
            return;
        }

        if (newPassword.length() < 8) {
            SessionUtil.setError(request, "New password must be at least 8 characters.");
            response.sendRedirect(request.getContextPath() + "/profile?tab=password");
            return;
        }

        User user = SessionUtil.getLoggedUser(request);

        // Verify current password against stored hash
        if (!PasswordUtil.checkPassword(currentPassword, user.getPassword())) {
            SessionUtil.setError(request, "Current password is incorrect.");
            response.sendRedirect(request.getContextPath() + "/profile?tab=password");
            return;
        }

        String newHash = PasswordUtil.hashPassword(newPassword);
        UserDAO userDAO = new UserDAO();
        boolean updated = userDAO.updatePassword(user.getUserId(), newHash);

        if (updated) {
            // Update session so the new hash is reflected immediately
            user.setPassword(newHash);
            SessionUtil.setLoggedUser(request, user);
            SessionUtil.setSuccess(request, "Password changed successfully.");
        } else {
            SessionUtil.setError(request, "Could not update password. Please try again.");
        }

        response.sendRedirect(request.getContextPath() + "/profile?tab=password");
    }
}
