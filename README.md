# Online Quiz System

A web-based Online Quiz System built for the **CS5054NP – Advanced Programming and Technologies** module coursework.

Users can register, log in, take quizzes, and view their scores. Admins can create categories, quizzes, questions, and view all attempts.

---

## Technologies Used

| Layer | Technology |
|---|---|
| Backend | Java 17, Jakarta Servlets, JSP, JSTL |
| Database access | JDBC |
| Database | MySQL (XAMPP) |
| Web server | Apache Tomcat 10 (via Smart Tomcat plugin in IntelliJ) |
| Build tool | Maven |
| Password hashing | jBCrypt |
| Frontend | HTML5, CSS3 (Flexbox + media queries — no Bootstrap) |
| IDE | IntelliJ IDEA |
| Version Control | Git + GitHub |

---

## Project Structure

```
OnlineQuizSystem/
├── pom.xml                       (Maven dependencies)
├── README.md
├── database/
│   └── quiz_system.sql           (full database schema + sample data)
└── src/main/
    ├── java/com/quizsystem/
    │   ├── controller/           (Servlets - the C in MVC)
    │   │   ├── LoginServlet.java
    │   │   ├── RegisterServlet.java
    │   │   ├── LogoutServlet.java
    │   │   ├── HomeServlet.java
    │   │   ├── AdminDashboardServlet.java
    │   │   ├── CategoryServlet.java
    │   │   ├── QuizServlet.java
    │   │   ├── QuestionServlet.java
    │   │   ├── AttemptQuizServlet.java
    │   │   ├── ResultServlet.java
    │   │   └── AdminViewResultsServlet.java
    │   ├── model/                (POJOs - the M in MVC)
    │   │   ├── User.java
    │   │   ├── Category.java
    │   │   ├── Quiz.java
    │   │   ├── Question.java
    │   │   ├── Option.java
    │   │   └── Attempt.java
    │   ├── dao/                  (Data Access Objects)
    │   │   ├── UserDAO.java
    │   │   ├── CategoryDAO.java
    │   │   ├── QuizDAO.java
    │   │   ├── QuestionDAO.java
    │   │   └── AttemptDAO.java
    │   ├── util/                 (Utility classes)
    │   │   ├── DbConfig.java
    │   │   ├── PasswordUtil.java     (Bcrypt hashing - Lecture 5)
    │   │   ├── ValidationUtil.java
    │   │   ├── SessionUtil.java      (Session - Lecture 7)
    │   │   └── CookieUtil.java       (Cookies - Lecture 7)
    │   └── filter/               (Servlet Filter)
    │       └── AuthFilter.java       (Auth filter - Lecture 7)
    └── webapp/                   (View - the V in MVC)
        ├── WEB-INF/
        │   └── web.xml               (deployment descriptor)
        ├── index.jsp
        ├── pages/
        │   ├── header.jsp            (shared header)
        │   ├── footer.jsp            (shared footer)
        │   ├── login.jsp
        │   ├── register.jsp
        │   ├── home.jsp
        │   ├── attemptQuiz.jsp
        │   ├── result.jsp
        │   ├── about.jsp
        │   ├── contact.jsp
        │   ├── error.jsp
        │   └── admin/
        │       ├── dashboard.jsp
        │       ├── manageCategories.jsp
        │       ├── manageQuiz.jsp
        │       ├── manageQuestions.jsp
        │       └── viewResults.jsp
        ├── css/
        │   └── style.css
        └── images/
```

---

## Database Tables (7)

| Table | Purpose |
|---|---|
| `users` | Stores admin and user accounts (passwords are Bcrypt-hashed) |
| `categories` | Quiz categories (e.g., Java, Math) |
| `quizzes` | Quizzes belonging to a category, created by an admin |
| `questions` | Questions belonging to a quiz |
| `options` | Multiple-choice options for each question |
| `attempts` | A user's attempt of a quiz (with score) |
| `answers` | Each answer chosen during an attempt |

---

## Setup Instructions

### Step 0 — Configure Database (Optional .env file)

The project is pre-configured for **XAMPP defaults** (user: `root`, password: empty).

**If your XAMPP setup is different**, you can customize it using `.env`:

1. Copy the template: `cp .env.example .env`
2. Edit `.env` with your actual credentials:
   ```
   DB_URL=jdbc:mysql://localhost:3306/quiz_system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   DB_USERNAME=your_db_user
   DB_PASSWORD=your_db_password
   ```
3. Save the file. The application will read these on startup.

**Important:** Never commit `.env` to Git — it contains passwords! The `.env.example` template is safe to commit for other developers to copy.

### Step 1 — Install Required Software

1. **Java JDK 17** — https://adoptium.net/
2. **IntelliJ IDEA** (Community Edition is fine) — https://www.jetbrains.com/idea/
3. **XAMPP** (for MySQL) — https://www.apachefriends.org/
4. **Apache Tomcat 10** — https://tomcat.apache.org/download-10.cgi (extract to e.g. `C:\apache-tomcat-10`)
5. **Git** — https://git-scm.com/

### Step 2 — Set up the Database (XAMPP MySQL)

1. Open **XAMPP Control Panel** and start **Apache** and **MySQL**.
2. Open `http://localhost/phpmyadmin` in the browser.
3. Click on the **Import** tab.
4. Choose the file `database/quiz_system.sql` from this project.
5. Click **Go** — this creates the `quiz_system` database with all 7 tables and sample data.

> Default DB connection details (in `DbConfig.java`):
> - URL: `jdbc:mysql://localhost:3306/quiz_system`
> - Username: `root`
> - Password: `(empty)`
>
> If your XAMPP setup is different, edit `src/main/java/com/quizsystem/util/DbConfig.java`.

### Step 3 — Open the Project in IntelliJ IDEA

1. Open IntelliJ → **File → Open** → select the `OnlineQuizSystem` folder.
2. Choose **Open as Project** if prompted.
3. IntelliJ will detect the `pom.xml` and download Maven dependencies automatically.
4. Wait for the indexing to complete.

### Step 4 — Install the Smart Tomcat plugin

1. In IntelliJ go to **File → Settings → Plugins → Marketplace**.
2. Search for **Smart Tomcat** and click **Install**.
3. Restart IntelliJ when prompted.

### Step 5 — Configure Smart Tomcat

1. Go to **Run → Edit Configurations…**
2. Click the **+** icon → choose **Smart Tomcat**.
3. Fill in the configuration:
   - **Name:** `OnlineQuizSystem`
   - **Tomcat Server:** click **Configure…** and add the path where you extracted Tomcat (e.g. `C:\apache-tomcat-10`)
   - **Deployment Directory:** `src/main/webapp`
   - **Context Path:** `/OnlineQuizSystem`
   - **Server Port:** `8080`
4. Click **Apply** and **OK**.

### Step 6 — Run the project

1. Click the **green Run ▶** button at the top of IntelliJ.
2. Open a browser and go to:
   `http://localhost:8080/OnlineQuizSystem/`
3. You should see the login page.

### Step 7 — Default Login Credentials

A default admin account is created by the SQL script:

| Email | Password | Role |
|---|---|---|
| `admin@quiz.com` | `Admin@123` | admin |

Use this to log in and start adding categories, quizzes, and questions.

Regular users can register through the **Register** link on the login page.

---

## GitHub Setup

### First-time setup

```bash
cd OnlineQuizSystem
git init
git add .
git commit -m "Initial commit: Online Quiz System"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/OnlineQuizSystem.git
git push -u origin main
```

### Recommended `.gitignore`

The project includes a `.gitignore` file that excludes:
- IntelliJ files (`.idea/`, `*.iml`)
- Maven build folder (`target/`)
- Operating system files (`.DS_Store`, `Thumbs.db`)

### Daily workflow

```bash
git status
git add .
git commit -m "Describe what you changed"
git push
```

---

## How the System Works (MVC Architecture)

1. **User → Browser** sends a request (e.g., clicks "Login").
2. **Servlet (Controller)** receives the request, reads form data.
3. **DAO** uses JDBC to talk to MySQL and returns data as **POJO objects (Model)**.
4. **Servlet** sets attributes on the request and forwards to a **JSP (View)**.
5. **JSP** renders HTML using JSTL tags and sends the page back to the browser.

The `AuthFilter` runs before every request to enforce login and role-based access.

---

## Marking Criteria Coverage

| Marks Component | How it's covered |
|---|---|
| **Database (5)** | 7 normalised tables in 3NF, proper PK/FK, sample data |
| **User Portal (10)** | Register, login, view quizzes, attempt, view own results |
| **Admin Dashboard (10)** | Dashboard with counts, full CRUD on categories/quizzes/questions, view all results |
| **Authentication & Authorization (5)** | Bcrypt hashing, role-based filter, session, remember-me cookie |
| **MVC Architecture (5)** | `controller/`, `model/`, `dao/`, `util/`, JSP views |
| **User Interface (5)** | Responsive CSS with Flexbox + media queries (no Bootstrap) |
| **Additional Pages (5)** | About, Contact, error pages |
| **Validation & Exception Handling (5)** | `ValidationUtil`, try-catch in DAOs, `web.xml` error pages |

---

## License

Academic coursework project — for educational use only.
#   A P T _ C o u r s e W o r k  
 