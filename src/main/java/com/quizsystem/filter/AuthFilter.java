package com.quizsystem.filter;

import com.quizsystem.util.SessionUtil;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Authentication filter that protects the application's pages.
 *
 * Reference: Lecture 7 (State Management and Middleware/Filter).
 *
 * Flow:
 *  - Public URLs (login, register, css, images): always allowed
 *  - All other URLs: require a valid session
 *  - Pages under /pages/admin/ : require admin role
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest)  req;
        HttpServletResponse response = (HttpServletResponse) res;

        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        String path = uri.substring(contextPath.length());

        // Allow static resources, the login/register pages, and the registration/login servlets
        if (isPublicResource(path)) {
            chain.doFilter(req, res);
            return;
        }

        // Check session for logged-in user
        boolean loggedIn = SessionUtil.isLoggedIn(request);

        if (!loggedIn) {
            response.sendRedirect(contextPath + "/login");
            return;
        }

        // For admin-only pages, also check the role
        if (path.startsWith("/pages/admin/") || path.startsWith("/admin/")) {
            if (!SessionUtil.isAdmin(request)) {
                response.sendRedirect(contextPath + "/home");
                return;
            }
        }

        chain.doFilter(req, res);
    }

    /** Returns true if the path is publicly accessible without login. */
    private boolean isPublicResource(String path) {
        return path.equals("/")
            || path.equals("/index.jsp")
            || path.equals("/login")
            || path.equals("/register")
            || path.startsWith("/pages/login.jsp")
            || path.startsWith("/pages/register.jsp")
            || path.startsWith("/pages/about.jsp")
            || path.startsWith("/pages/contact.jsp")
            || path.startsWith("/css/")
            || path.startsWith("/images/");
    }
}
