<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact - Online Quiz System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/pages/header.jsp"/>

<main class="container">
    <h2>Contact Us</h2>

    <div class="contact-grid">
        <div class="content-card">
            <h3>Get in Touch</h3>
            <p><strong>Address:</strong> Informatics College,  Pokhara, Matepani 12</p>
            <p><strong>Email:</strong> support@quizsystem.local</p>
            <p><strong>Phone:</strong> +977-9800000000</p>
        </div>

        <div class="content-card">
            <h3>Send us a message</h3>
            <form>
                <div class="form-group">
                    <label for="cName">Your Name</label>
                    <input type="text" id="cName" name="cName" required>
                </div>
                <div class="form-group">
                    <label for="cEmail">Your Email</label>
                    <input type="email" id="cEmail" name="cEmail" required>
                </div>
                <div class="form-group">
                    <label for="cMessage">Message</label>
                    <textarea id="cMessage" name="cMessage" rows="4" required></textarea>
                </div>
                <button type="submit" class="btn btn-primary">Send</button>
            </form>
        </div>
    </div>
</main>

<jsp:include page="/pages/footer.jsp"/>
</body>
</html>
