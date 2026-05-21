<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Results - Online Quiz System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/pages/header.jsp"/>

<main class="container">
    <h2>My Results</h2>

    <c:if test="${not empty latestScore}">
        <div class="score-card">
            <h3>Latest Attempt</h3>
            <p class="score-big">${latestScore} / ${latestTotal}</p>
            <p>
                <c:choose>
                    <c:when test="${latestScore == latestTotal}">Excellent! Full marks!</c:when>
                    <c:when test="${latestScore >= (latestTotal / 2)}">Good job! Keep practicing.</c:when>
                    <c:otherwise>Keep practicing — you'll get there.</c:otherwise>
                </c:choose>
            </p>
        </div>
    </c:if>

    <h3>Attempt History</h3>
    <c:choose>
        <c:when test="${empty attempts}">
            <p class="empty-msg">You have not attempted any quizzes yet.</p>
        </c:when>
        <c:otherwise>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Quiz</th>
                        <th>Score</th>
                        <th>Total</th>
                        <th>Date</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="a" items="${attempts}" varStatus="s">
                        <tr>
                            <td>${s.index + 1}</td>
                            <td>${a.quizTitle}</td>
                            <td>${a.score}</td>
                            <td>${a.totalMarks}</td>
                            <td><fmt:formatDate value="${a.attemptDate}" pattern="yyyy-MM-dd HH:mm"/></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/home">Back to Home</a>
</main>

<jsp:include page="/pages/footer.jsp"/>
</body>
</html>
