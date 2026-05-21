-- =====================================================================
-- Online Quiz System - Database Schema
-- Module: CS5054NP Advanced Programming and Technologies
-- Database: MySQL (XAMPP)
-- =====================================================================

DROP DATABASE IF EXISTS quiz_system;
CREATE DATABASE quiz_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE quiz_system;

-- ---------------------------------------------------------------------
-- Table 1: users
-- Stores both admin and normal users (role-based access).
-- ---------------------------------------------------------------------
CREATE TABLE users (
    user_id      INT AUTO_INCREMENT PRIMARY KEY,
    full_name    VARCHAR(100)        NOT NULL,
    email        VARCHAR(100) UNIQUE NOT NULL,
    phone        VARCHAR(15)         NOT NULL,
    password     VARCHAR(100)        NOT NULL,   -- stores Bcrypt hash (60 chars)
    role         VARCHAR(10)         NOT NULL DEFAULT 'user', -- 'admin' or 'user'
    created_at   TIMESTAMP           DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------
-- Table 2: categories
-- Quiz categories such as Java, Math, General Knowledge.
-- ---------------------------------------------------------------------
CREATE TABLE categories (
    category_id   INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    description   VARCHAR(255),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------
-- Table 3: quizzes
-- A quiz belongs to one category and is created by one admin user.
-- ---------------------------------------------------------------------
CREATE TABLE quizzes (
    quiz_id      INT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(150) NOT NULL,
    description  VARCHAR(255),
    category_id  INT NOT NULL,
    time_limit   INT NOT NULL DEFAULT 10,    -- minutes
    created_by   INT NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_quiz_category FOREIGN KEY (category_id)
        REFERENCES categories(category_id) ON DELETE CASCADE,
    CONSTRAINT fk_quiz_user FOREIGN KEY (created_by)
        REFERENCES users(user_id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- Table 4: questions
-- Each question belongs to exactly one quiz.
-- ---------------------------------------------------------------------
CREATE TABLE questions (
    question_id   INT AUTO_INCREMENT PRIMARY KEY,
    quiz_id       INT          NOT NULL,
    question_text VARCHAR(500) NOT NULL,
    marks         INT          NOT NULL DEFAULT 1,
    CONSTRAINT fk_question_quiz FOREIGN KEY (quiz_id)
        REFERENCES quizzes(quiz_id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- Table 5: options
-- Multiple-choice options. Exactly one is_correct per question (handled in app).
-- ---------------------------------------------------------------------
CREATE TABLE options (
    option_id    INT AUTO_INCREMENT PRIMARY KEY,
    question_id  INT          NOT NULL,
    option_text  VARCHAR(255) NOT NULL,
    is_correct   BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_option_question FOREIGN KEY (question_id)
        REFERENCES questions(question_id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- Table 6: attempts
-- One row per quiz attempt by a user (overall score).
-- ---------------------------------------------------------------------
CREATE TABLE attempts (
    attempt_id   INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT NOT NULL,
    quiz_id      INT NOT NULL,
    score        INT NOT NULL DEFAULT 0,
    total_marks  INT NOT NULL DEFAULT 0,
    attempt_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_attempt_user FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_attempt_quiz FOREIGN KEY (quiz_id)
        REFERENCES quizzes(quiz_id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- Table 7: answers
-- One row per question answered in an attempt (which option was chosen).
-- ---------------------------------------------------------------------
CREATE TABLE answers (
    answer_id          INT AUTO_INCREMENT PRIMARY KEY,
    attempt_id         INT NOT NULL,
    question_id        INT NOT NULL,
    selected_option_id INT,
    CONSTRAINT fk_answer_attempt FOREIGN KEY (attempt_id)
        REFERENCES attempts(attempt_id) ON DELETE CASCADE,
    CONSTRAINT fk_answer_question FOREIGN KEY (question_id)
        REFERENCES questions(question_id) ON DELETE CASCADE,
    CONSTRAINT fk_answer_option FOREIGN KEY (selected_option_id)
        REFERENCES options(option_id) ON DELETE SET NULL
);

-- =====================================================================
-- Sample Data
-- =====================================================================

-- Default admin (login: admin@quiz.com / Admin@123)
-- The password below is the Bcrypt hash of 'Admin@123'.
INSERT INTO users (full_name, email, phone, password, role) VALUES
('System Admin', 'admin@quiz.com', '9800000000',
 '$2a$10$H4X1PWcPnDcXxKqVv1mY1uAUaWv4pZgUmAjqkWqFhTMQEmoMrQ6tu', 'admin');

-- Sample categories
INSERT INTO categories (category_name, description) VALUES
('Java Programming', 'Quizzes related to Java basics and OOP'),
('General Knowledge', 'Common knowledge questions'),
('Mathematics', 'Math problem-solving quizzes');

-- Sample quiz
INSERT INTO quizzes (title, description, category_id, time_limit, created_by) VALUES
('Java Basics Quiz', 'Test your Java fundamentals', 1, 10, 1);

-- Sample questions
INSERT INTO questions (quiz_id, question_text, marks) VALUES
(1, 'Which keyword is used to define a class in Java?', 1),
(1, 'Which method is the entry point of a Java program?', 1);

-- Sample options
-- Question 1
INSERT INTO options (question_id, option_text, is_correct) VALUES
(1, 'class',     TRUE),
(1, 'Class',     FALSE),
(1, 'define',    FALSE),
(1, 'object',    FALSE);

-- Question 2
INSERT INTO options (question_id, option_text, is_correct) VALUES
(2, 'start()',          FALSE),
(2, 'main()',           TRUE),
(2, 'run()',            FALSE),
(2, 'execute()',        FALSE);

-- =====================================================================
-- End of Schema
-- =====================================================================
