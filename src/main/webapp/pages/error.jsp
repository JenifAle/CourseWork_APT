<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Online Quiz System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/pages/header.jsp"/>

<main class="container">
    <div class="content-card error-card">
        <h2>Oops! Something went wrong</h2>
        <p>We could not complete your request. Please try again or return to the home page.</p>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/">Go to Home</a>
    </div>
</main>

<jsp:include page="/pages/footer.jsp"/>
</body>
</html>
