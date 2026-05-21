package com.quizsystem.controller;

import com.quizsystem.dao.AttemptDAO;
import com.quizsystem.dao.QuestionDAO;
import com.quizsystem.dao.QuizDAO;
import com.quizsystem.model.Question;
import com.quizsystem.model.Quiz;
import com.quizsystem.model.User;
import com.quizsystem.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles taking and submitting a quiz.
 *
 * GET  /attemptQuiz?quizId=N : show the quiz to the user
 * POST /attemptQuiz          : grade the quiz, save the attempt, redirect to result
 */
@WebServlet("/attemptQuiz")
public class AttemptQuizServlet extends HttpServlet {

    private final QuizDAO quizDAO         = new QuizDAO();
    private final QuestionDAO questionDAO = new QuestionDAO();
    private final AttemptDAO attemptDAO   = new AttemptDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String quizIdStr = request.getParameter("quizId");
        if (quizIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        int quizId = Integer.parseInt(quizIdStr);
        Quiz quiz = quizDAO.getQuizById(quizId);
        List<Question> questions = questionDAO.getQuestionsByQuiz(quizId);

        if (quiz == null || questions.isEmpty()) {
            request.getSession().setAttribute("error",
                    "This quiz is not available or has no questions yet.");
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        request.setAttribute("quiz", quiz);
        request.setAttribute("questions", questions);
        request.getRequestDispatcher("/pages/attemptQuiz.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        User user = SessionUtil.getLoggedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int quizId = Integer.parseInt(request.getParameter("quizId"));
        List<Question> questions = questionDAO.getQuestionsByQuiz(quizId);

        int score = 0;
        int totalMarks = 0;
        Map<Integer, Integer> answers = new HashMap<>();

        // For each question: read which option the user selected and compare it with the correct option
        for (Question q : questions) {
            totalMarks += q.getMarks();

            String selectedStr = request.getParameter("q_" + q.getQuestionId());
            int selectedOptionId = -1;
            if (selectedStr != null && !selectedStr.isEmpty()) {
                try {
                    selectedOptionId = Integer.parseInt(selectedStr);
                } catch (NumberFormatException ignored) { /* leave as -1 */ }
            }
            answers.put(q.getQuestionId(), selectedOptionId);

            int correctId = questionDAO.getCorrectOptionId(q.getQuestionId());
            if (selectedOptionId == correctId) {
                score += q.getMarks();
            }
        }

        int attemptId = attemptDAO.saveAttempt(user.getUserId(), quizId, score, totalMarks, answers);
        if (attemptId == -1) {
            request.getSession().setAttribute("error", "Could not save your attempt. Please try again.");
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // Redirect to result page (PRG pattern: avoids resubmission on refresh)
        response.sendRedirect(request.getContextPath() + "/result?attemptId=" + attemptId
                + "&score=" + score + "&total=" + totalMarks);
    }
}
