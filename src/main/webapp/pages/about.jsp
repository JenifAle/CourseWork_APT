<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About - Online Quiz System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/pages/header.jsp"/>

<main class="container">
    <h2>About</h2>
    <div class="content-card">
        <p>
            The Online Quiz System is a web-based application built as part of the
            <strong>CS5054NP Advanced Programming and Technologies</strong> module.
        </p>
        <p>
            The system allows administrators to create categories, quizzes, questions, and
            multiple-choice options. Registered users can attempt quizzes, get instant scores,
            and review their attempt history.
        </p>
        <h3>Features</h3>
        <ul>
            <li>User registration and login with password hashing (Bcrypt)</li>
            <li>Role-based access control (admin and user)</li>
            <li>Quiz categories management</li>
            <li>Quiz, question, and option management (CRUD)</li>
            <li>Quiz attempt with automatic score calculation</li>
            <li>Personal attempt history for each user</li>
            <li>Admin view of all attempts</li>
        </ul>

        <h3>Technologies Used</h3>
        <ul>
            <li>Java EE / Jakarta EE — Servlets, JSP, JSTL</li>
            <li>JDBC API for MySQL database access</li>
            <li>MySQL (XAMPP) for the database</li>
            <li>Apache Tomcat as the web container</li>
            <li>Bcrypt for password hashing</li>
            <li>HTML5, CSS3 (Flexbox, Media Queries)</li>
        </ul>
    </div>
</main>

<jsp:include page="/pages/footer.jsp"/>
</body>
</html>
