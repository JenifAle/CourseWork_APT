<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- 
    Root entry point.
    If the user is logged in, redirect to the appropriate dashboard.
    Otherwise send them to the login page.
--%>
<%
    Object loggedUser = (session != null) ? session.getAttribute("loggedUser") : null;
    if (loggedUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
    } else {
        com.quizsystem.model.User u = (com.quizsystem.model.User) loggedUser;
        if ("admin".equalsIgnoreCase(u.getRole())) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
%>
