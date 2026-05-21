package com.quizsystem.util;

import com.quizsystem.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Utility class for session management.
 *
 * If you already have SessionUtil.java, ADD the setSuccess() and setError()
 * helper methods below to your existing file. Everything else may already exist.
 */
public class SessionUtil {

    // ── Store the logged-in user in session ─────────────────────────────

    public static void setLoggedUser(HttpServletRequest request, User user) {
        request.getSession().setAttribute("loggedUser", user);
    }

    public static User getLoggedUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("loggedUser");
    }

    // ── Auth checks ─────────────────────────────────────────────────────

    public static boolean isLoggedIn(HttpServletRequest request) {
        return getLoggedUser(request) != null;
    }

    public static boolean isAdmin(HttpServletRequest request) {
        User user = getLoggedUser(request);
        return user != null && "admin".equalsIgnoreCase(user.getRole());
    }

    // ── Invalidate session on logout ────────────────────────────────────

    public static void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    // ── Flash messages (stored in session, read once in JSP) ────────────

    /**
     * Store a success message to display after redirect.
     * Read in JSP as: ${sessionScope.success}
     */
    public static void setSuccess(HttpServletRequest request, String message) {
        request.getSession().setAttribute("success", message);
    }

    /**
     * Store an error message to display after redirect.
     * Read in JSP as: ${sessionScope.error}
     */
    public static void setError(HttpServletRequest request, String message) {
        request.getSession().setAttribute("error", message);
    }
}
