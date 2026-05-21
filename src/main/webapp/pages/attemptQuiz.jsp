<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${quiz.title} - Online Quiz System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/pages/header.jsp"/>

<main class="container">
    <div class="quiz-attempt-header">
        <h2>${quiz.title}</h2>
        <p>${quiz.description}</p>
        <p class="quiz-meta">Time Limit: ${quiz.timeLimit} minutes</p>
    </div>

    <form action="${pageContext.request.contextPath}/attemptQuiz" method="post" class="quiz-form">
        <input type="hidden" name="quizId" value="${quiz.quizId}">

        <c:forEach var="q" items="${questions}" varStatus="qs">
            <div class="question-block">
                <h3>Q${qs.index + 1}. ${q.questionText}
                    <span class="marks">(${q.marks} mark<c:if test="${q.marks > 1}">s</c:if>)</span>
                </h3>

                <c:forEach var="opt" items="${q.options}">
                    <label class="option-label">
                        <input type="radio"
                               name="q_${q.questionId}"
                               value="${opt.optionId}" required>
                        ${opt.optionText}
                    </label>
                </c:forEach>
            </div>
        </c:forEach>

        <button type="submit" class="btn btn-primary">Submit Quiz</button>
        <a class="btn btn-secondary"
           href="${pageContext.request.contextPath}/home">Cancel</a>
    </form>
</main>

<jsp:include page="/pages/footer.jsp"/>
</body>
</html>
