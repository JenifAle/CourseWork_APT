<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Questions - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/pages/header.jsp"/>

<main class="container">
    <h2>Questions for: <em>${quiz.title}</em></h2>
    <p><a href="${pageContext.request.contextPath}/admin/quizzes">&larr; Back to Quizzes</a></p>

    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success">${sessionScope.success}</div>
        <c:remove var="success" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-error">${sessionScope.error}</div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <div class="content-card">
        <h3>Add New Question</h3>
        <form action="${pageContext.request.contextPath}/admin/questions" method="post">
            <input type="hidden" name="action" value="add">
            <input type="hidden" name="quizId" value="${quiz.quizId}">

            <div class="form-group">
                <label for="questionText">Question</label>
                <textarea id="questionText" name="questionText" rows="2" required></textarea>
            </div>

            <div class="form-group">
                <label for="marks">Marks</label>
                <input type="number" id="marks" name="marks" min="1" value="1" required>
            </div>

            <p>Enter four options and pick the correct one:</p>
            <c:forEach var="i" begin="1" end="4">
                <div class="form-group option-row">
                    <input type="radio" name="correctOption" value="${i}" required>
                    <input type="text" name="option${i}" placeholder="Option ${i}" required>
                </div>
            </c:forEach>

            <button type="submit" class="btn btn-primary">Add Question</button>
        </form>
    </div>

    <h3>Existing Questions</h3>
    <c:choose>
        <c:when test="${empty questions}">
            <p class="empty-msg">No questions yet for this quiz.</p>
        </c:when>
        <c:otherwise>
            <c:forEach var="q" items="${questions}" varStatus="qs">
                <div class="content-card">
                    <h4>Q${qs.index + 1}. ${q.questionText} <small>(${q.marks} marks)</small></h4>
                    <ul class="options-list">
                        <c:forEach var="opt" items="${q.options}">
                            <li class="${opt.correct ? 'correct-option' : ''}">
                                ${opt.optionText}
                                <c:if test="${opt.correct}"> &check;</c:if>
                            </li>
                        </c:forEach>
                    </ul>
                    <form action="${pageContext.request.contextPath}/admin/questions"
                          method="post" class="inline-form"
                          onsubmit="return confirm('Delete this question?');">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="quizId" value="${quiz.quizId}">
                        <input type="hidden" name="questionId" value="${q.questionId}">
                        <button type="submit" class="btn btn-small btn-danger">Delete</button>
                    </form>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</main>

<jsp:include page="/pages/footer.jsp"/>
</body>
</html>
