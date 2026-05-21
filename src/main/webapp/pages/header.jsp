<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<a href="${pageContext.request.contextPath}/profile">Profile</a>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- 
    Shared header (Lecture 8 - JSP Templating with include).
    Shows different navigation links based on user role.
--%>
<header class="site-header">
    <div class="header-inner">
        <a class="brand" href="${pageContext.request.contextPath}/">QuizSystem</a>

        <nav class="main-nav">
            <c:choose>
                <c:when test="${empty sessionScope.loggedUser}">
                    <a href="${pageContext.request.contextPath}/pages/about.jsp">About</a>
                    <a href="${pageContext.request.contextPath}/pages/contact.jsp">Contact</a>
                    <a href="${pageContext.request.contextPath}/login">Login</a>
                    <a href="${pageContext.request.contextPath}/register">Register</a>
                </c:when>
                <c:when test="${sessionScope.loggedUser.role == 'admin'}">
                    <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
                    <a href="${pageContext.request.contextPath}/admin/categories">Categories</a>
                    <a href="${pageContext.request.contextPath}/admin/quizzes">Quizzes</a>
                    <a href="${pageContext.request.contextPath}/admin/results">Results</a>
                    <span class="user-name">Hi, ${sessionScope.loggedUser.fullName}</span>
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/home">Home</a>
                    <a href="${pageContext.request.contextPath}/result">My Results</a>
                    <a href="${pageContext.request.contextPath}/pages/about.jsp">About</a>
                    <a href="${pageContext.request.contextPath}/pages/contact.jsp">Contact</a>
                    <span class="user-name">Hi, ${sessionScope.loggedUser.fullName}</span>
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>
                </c:otherwise>
            </c:choose>
        </nav>
    </div>
</header>
