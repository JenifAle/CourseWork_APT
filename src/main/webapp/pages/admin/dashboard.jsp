<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Online Quiz System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/pages/header.jsp"/>

<main class="container">
    <h2>Admin Dashboard</h2>
    <p>Welcome, ${sessionScope.loggedUser.fullName}!</p>

    <div class="stats-grid">
        <div class="stat-card">
            <h3>Total Categories</h3>
            <p class="stat-num">${totalCategories}</p>
        </div>
        <div class="stat-card">
            <h3>Total Quizzes</h3>
            <p class="stat-num">${totalQuizzes}</p>
        </div>
    </div>

    <div class="action-grid">
        <a class="action-card" href="${pageContext.request.contextPath}/admin/categories">
            <h3>Manage Categories</h3>
            <p>Add, edit, and delete quiz categories.</p>
        </a>
        <a class="action-card" href="${pageContext.request.contextPath}/admin/quizzes">
            <h3>Manage Quizzes</h3>
            <p>Create and manage quizzes and their questions.</p>
        </a>
        <a class="action-card" href="${pageContext.request.contextPath}/admin/results">
            <h3>View All Results</h3>
            <p>See attempts made by all users.</p>
        </a>
    </div>
</main>

<jsp:include page="/pages/footer.jsp"/>
</body>
</html>
