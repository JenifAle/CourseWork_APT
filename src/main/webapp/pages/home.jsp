<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home - Online Quiz System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/pages/header.jsp"/>

<main class="container">
    <h2>Available Quizzes</h2>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-error">${sessionScope.error}</div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <c:choose>
        <c:when test="${empty quizzes}">
            <p class="empty-msg">No quizzes available yet. Please check back later.</p>
        </c:when>
        <c:otherwise>
            <div class="quiz-grid">
                <c:forEach var="q" items="${quizzes}">
                    <div class="quiz-card">
                        <h3>${q.title}</h3>
                        <p class="quiz-meta">Category: ${q.categoryName}</p>
                        <p class="quiz-meta">Time Limit: ${q.timeLimit} minutes</p>
                        <p>${q.description}</p>
                        <a class="btn btn-primary"
                           href="${pageContext.request.contextPath}/attemptQuiz?quizId=${q.quizId}">
                           Start Quiz
                        </a>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<jsp:include page="/pages/footer.jsp"/>
</body>
</html>
