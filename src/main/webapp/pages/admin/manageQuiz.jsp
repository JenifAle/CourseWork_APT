<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Quizzes - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/pages/header.jsp"/>

<main class="container">
    <h2>Manage Quizzes</h2>

    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success">${sessionScope.success}</div>
        <c:remove var="success" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-error">${sessionScope.error}</div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <c:if test="${empty categories}">
        <div class="alert alert-error">
            You need to create at least one category before adding a quiz.
            <a href="${pageContext.request.contextPath}/admin/categories">Add a category</a>
        </div>
    </c:if>

    <c:if test="${not empty categories}">
        <div class="content-card">
            <h3>Add New Quiz</h3>
            <form action="${pageContext.request.contextPath}/admin/quizzes" method="post">
                <input type="hidden" name="action" value="add">
                <div class="form-group">
                    <label for="title">Title</label>
                    <input type="text" id="title" name="title" required>
                </div>
                <div class="form-group">
                    <label for="description">Description</label>
                    <input type="text" id="description" name="description">
                </div>
                <div class="form-group">
                    <label for="categoryId">Category</label>
                    <select id="categoryId" name="categoryId" required>
                        <c:forEach var="c" items="${categories}">
                            <option value="${c.categoryId}">${c.categoryName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="timeLimit">Time Limit (minutes)</label>
                    <input type="number" id="timeLimit" name="timeLimit" min="1" value="10" required>
                </div>
                <button type="submit" class="btn btn-primary">Add Quiz</button>
            </form>
        </div>
    </c:if>

    <h3>Existing Quizzes</h3>
    <c:choose>
        <c:when test="${empty quizzes}">
            <p class="empty-msg">No quizzes yet.</p>
        </c:when>
        <c:otherwise>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>ID</th><th>Title</th><th>Category</th>
                        <th>Time</th><th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="q" items="${quizzes}">
                        <tr>
                            <td>${q.quizId}</td>
                            <td>${q.title}</td>
                            <td>${q.categoryName}</td>
                            <td>${q.timeLimit} min</td>
                            <td>
                                <a class="btn btn-small"
                                   href="${pageContext.request.contextPath}/admin/questions?quizId=${q.quizId}">
                                   Questions
                                </a>
                                <form action="${pageContext.request.contextPath}/admin/quizzes"
                                      method="post" class="inline-form"
                                      onsubmit="return confirm('Delete this quiz and all its questions?');">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="quizId" value="${q.quizId}">
                                    <button type="submit" class="btn btn-small btn-danger">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</main>

<jsp:include page="/pages/footer.jsp"/>
</body>
</html>
