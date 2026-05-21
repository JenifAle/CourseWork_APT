<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile - Online Quiz System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/pages/header.jsp"/>

<main class="container">

    <h2>My Profile</h2>
    <p>Manage your account details and view your quiz history.</p>

    <%-- Flash alerts --%>
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success">${sessionScope.success}</div>
        <c:remove var="success" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-error">${sessionScope.error}</div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <div class="profile-wrapper">

        <!-- LEFT — Avatar card -->
        <aside class="profile-avatar-card">

            <div class="profile-avatar">
                ${fn:substring(sessionScope.loggedUser.fullName, 0, 1)}
            </div>

            <div class="profile-name">${sessionScope.loggedUser.fullName}</div>
            <div class="profile-email">${sessionScope.loggedUser.email}</div>

            <c:choose>
                <c:when test="${sessionScope.loggedUser.role eq 'admin'}">
                    <span class="profile-role-badge admin">&#9670; Admin</span>
                </c:when>
                <c:otherwise>
                    <span class="profile-role-badge">&#9670; Student</span>
                </c:otherwise>
            </c:choose>

            <div class="profile-mini-stats">
                <div class="profile-mini-stat">
                    <span class="msv">${totalAttempts != null ? totalAttempts : 0}</span>
                    <span class="msl">Quizzes Taken</span>
                </div>
                <div class="profile-mini-stat">
                    <span class="msv">${bestScore != null ? bestScore : 0}%</span>
                    <span class="msl">Best Score</span>
                </div>
            </div>

        </aside>

        <!-- RIGHT — Tabs + panels -->
        <div class="profile-content">

            <nav class="profile-tabs">
                <a href="${pageContext.request.contextPath}/profile?tab=info"
                   class="profile-tab ${(param.tab eq 'info' || empty param.tab) ? 'active' : ''}">
                    Account Info
                </a>
                <a href="${pageContext.request.contextPath}/profile?tab=edit"
                   class="profile-tab ${param.tab eq 'edit' ? 'active' : ''}">
                    Edit Profile
                </a>
                <a href="${pageContext.request.contextPath}/profile?tab=password"
                   class="profile-tab ${param.tab eq 'password' ? 'active' : ''}">
                    Change Password
                </a>
                <a href="${pageContext.request.contextPath}/profile?tab=history"
                   class="profile-tab ${param.tab eq 'history' ? 'active' : ''}">
                    Quiz History
                </a>
            </nav>

            <!-- TAB: Account Info -->
            <c:if test="${param.tab eq 'info' || empty param.tab}">
                <div class="profile-info-card">
                    <div class="profile-section-title">Personal Details</div>
                    <div class="profile-field">
                        <span class="profile-field-label">Full Name</span>
                        <span class="profile-field-value">${sessionScope.loggedUser.fullName}</span>
                    </div>
                    <div class="profile-field">
                        <span class="profile-field-label">Email</span>
                        <span class="profile-field-value">${sessionScope.loggedUser.email}</span>
                    </div>
                    <div class="profile-field">
                        <span class="profile-field-label">Phone</span>
                        <span class="profile-field-value">${sessionScope.loggedUser.phone}</span>
                    </div>
                    <div class="profile-field">
                        <span class="profile-field-label">Role</span>
                        <span class="profile-field-value">${sessionScope.loggedUser.role}</span>
                    </div>
                    <div class="profile-field">
                        <span class="profile-field-label">Member Since</span>
                        <span class="profile-field-value">${sessionScope.loggedUser.createdAt}</span>
                    </div>
                </div>
            </c:if>

            <!-- TAB: Edit Profile -->
            <c:if test="${param.tab eq 'edit'}">
                <div class="profile-info-card">
                    <div class="profile-section-title">Edit Details</div>
                    <form class="profile-form"
                          action="${pageContext.request.contextPath}/profile"
                          method="post">
                        <input type="hidden" name="action" value="updateProfile"/>
                        <div class="form-row">
                            <div class="form-group">
                                <label for="fullName">Full Name</label>
                                <input type="text" id="fullName" name="fullName"
                                       value="${sessionScope.loggedUser.fullName}" required/>
                            </div>
                            <div class="form-group">
                                <label for="phone">Phone</label>
                                <input type="text" id="phone" name="phone"
                                       value="${sessionScope.loggedUser.phone}" required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="email">Email Address</label>
                            <input type="email" id="email" name="email"
                                   value="${sessionScope.loggedUser.email}" required/>
                        </div>
                        <button type="submit" class="btn btn-primary">Save Changes</button>
                    </form>
                </div>
            </c:if>

            <!-- TAB: Change Password -->
            <c:if test="${param.tab eq 'password'}">
                <div class="profile-info-card">
                    <div class="profile-section-title">Change Password</div>
                    <form class="profile-form"
                          action="${pageContext.request.contextPath}/profile"
                          method="post">
                        <input type="hidden" name="action" value="changePassword"/>
                        <div class="form-group">
                            <label for="currentPassword">Current Password</label>
                            <input type="password" id="currentPassword"
                                   name="currentPassword"
                                   placeholder="Enter your current password" required/>
                        </div>
                        <div class="form-row">
                            <div class="form-group">
                                <label for="newPassword">New Password</label>
                                <input type="password" id="newPassword"
                                       name="newPassword"
                                       placeholder="Minimum 8 characters" required/>
                            </div>
                            <div class="form-group">
                                <label for="confirmPassword">Confirm Password</label>
                                <input type="password" id="confirmPassword"
                                       name="confirmPassword"
                                       placeholder="Repeat new password" required/>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary">Update Password</button>
                    </form>
                </div>
            </c:if>

            <!-- TAB: Quiz History -->
            <c:if test="${param.tab eq 'history'}">
                <div class="profile-info-card">
                    <div class="profile-section-title">Quiz Attempts</div>
                    <c:choose>
                        <c:when test="${empty attempts}">
                            <p style="color:var(--text-muted);font-size:14px;text-align:center;padding:40px 0;">
                                You haven't attempted any quizzes yet.
                                <br><br>
                                <a href="${pageContext.request.contextPath}/home"
                                   class="btn btn-primary">Browse Quizzes</a>
                            </p>
                        </c:when>
                        <c:otherwise>
                            <table class="profile-history-table">
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Quiz</th>
                                    <th>Category</th>
                                    <th>Score</th>
                                    <th>Date</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="a" items="${attempts}" varStatus="s">
                                    <tr>
                                        <td style="color:var(--text-muted)">${s.index + 1}</td>
                                        <td style="font-weight:600;color:var(--white)">${a.quizTitle}</td>
                                        <td>${a.categoryName}</td>
                                        <td>
                                                <span class="score-pill ${a.percentage >= 70 ? 'high' : a.percentage >= 40 ? 'mid' : 'low'}">
                                                    ${a.score}/${a.totalMarks} &nbsp;(${a.percentage}%)
                                                </span>
                                        </td>
                                        <td style="color:var(--text-muted);font-size:13px">${a.attemptDate}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>

        </div><%-- end .profile-content --%>
    </div><%-- end .profile-wrapper --%>

</main>

<jsp:include page="/pages/footer.jsp"/>
</body>
</html>
