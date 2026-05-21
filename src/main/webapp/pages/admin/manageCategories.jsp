<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Categories - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/pages/header.jsp"/>

<main class="container">
    <h2>Manage Categories</h2>

    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success">${sessionScope.success}</div>
        <c:remove var="success" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-error">${sessionScope.error}</div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <div class="content-card">
        <h3>Add New Category</h3>
        <form action="${pageContext.request.contextPath}/admin/categories" method="post">
            <input type="hidden" name="action" value="add">
            <div class="form-group">
                <label for="categoryName">Category Name</label>
                <input type="text" id="categoryName" name="categoryName" required>
            </div>
            <div class="form-group">
                <label for="description">Description</label>
                <input type="text" id="description" name="description">
            </div>
            <button type="submit" class="btn btn-primary">Add Category</button>
        </form>
    </div>

    <h3>Existing Categories</h3>
    <c:choose>
        <c:when test="${empty categories}">
            <p class="empty-msg">No categories yet. Add one above to get started.</p>
        </c:when>
        <c:otherwise>
            <table class="data-table">
                <thead>
                    <tr><th>ID</th><th>Name</th><th>Description</th><th>Actions</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="c" items="${categories}">
                        <tr>
                            <td>${c.categoryId}</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/admin/categories"
                                      method="post" class="inline-form">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="categoryId" value="${c.categoryId}">
                                    <input type="text" name="categoryName" value="${c.categoryName}" required>
                            </td>
                            <td>
                                    <input type="text" name="description" value="${c.description}">
                            </td>
                            <td>
                                    <button type="submit" class="btn btn-small">Update</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/admin/categories"
                                      method="post" class="inline-form"
                                      onsubmit="return confirm('Delete this category? Related quizzes will also be deleted.');">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="categoryId" value="${c.categoryId}">
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
