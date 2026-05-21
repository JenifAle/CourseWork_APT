package com.quizsystem.controller;

import com.quizsystem.dao.QuestionDAO;
import com.quizsystem.dao.QuizDAO;
import com.quizsystem.model.Option;
import com.quizsystem.model.Question;
import com.quizsystem.model.Quiz;
import com.quizsystem.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for managing the questions of a single quiz.
 *
 * Routes:
 *   GET  /admin/questions?quizId=N            : show questions for quiz N
 *   POST /admin/questions?action=add          : add question + 4 options
 *   POST /admin/questions?action=delete       : delete question
 */
@WebServlet("/admin/questions")
public class QuestionServlet extends HttpServlet {

    private final QuestionDAO questionDAO = new QuestionDAO();
    private final QuizDAO quizDAO         = new QuizDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String quizIdStr = request.getParameter("quizId");
        if (ValidationUtil.isNullOrEmpty(quizIdStr)) {
            response.sendRedirect(request.getContextPath() + "/admin/quizzes");
            return;
        }

        int quizId = Integer.parseInt(quizIdStr);
        Quiz quiz = quizDAO.getQuizById(quizId);
        if (quiz == null) {
            response.sendRedirect(request.getContextPath() + "/admin/quizzes");
            return;
        }

        List<Question> questions = questionDAO.getQuestionsByQuiz(quizId);
        request.setAttribute("quiz", quiz);
        request.setAttribute("questions", questions);
        request.getRequestDispatcher("/pages/admin/manageQuestions.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String action = request.getParameter("action");
        int quizId    = Integer.parseInt(request.getParameter("quizId"));

        try {
            if ("add".equals(action)) {
                handleAdd(request, quizId);
            } else if ("delete".equals(action)) {
                int qid = Integer.parseInt(request.getParameter("questionId"));
                if (questionDAO.deleteQuestion(qid)) {
                    request.getSession().setAttribute("success", "Question deleted.");
                } else {
                    request.getSession().setAttribute("error", "Failed to delete question.");
                }
            }
        } catch (Exception ex) {
            request.getSession().setAttribute("error", "Operation failed: " + ex.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/questions?quizId=" + quizId);
    }

    private void handleAdd(HttpServletRequest request, int quizId) {
        String questionText = request.getParameter("questionText");
        String marksStr     = request.getParameter("marks");

        if (ValidationUtil.isNullOrEmpty(questionText)) {
            request.getSession().setAttribute("error", "Question text is required.");
            return;
        }

        int marks = ValidationUtil.isNullOrEmpty(marksStr) ? 1 : Integer.parseInt(marksStr);

        // Read 4 options (option1..option4) and the correct option (1..4)
        String correctStr = request.getParameter("correctOption");
        if (ValidationUtil.isNullOrEmpty(correctStr)) {
            request.getSession().setAttribute("error", "Please select the correct option.");
            return;
        }
        int correct = Integer.parseInt(correctStr);

        List<Option> options = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            String text = request.getParameter("option" + i);
            if (ValidationUtil.isNullOrEmpty(text)) {
                request.getSession().setAttribute("error", "All four options are required.");
                return;
            }
            Option o = new Option();
            o.setOptionText(text.trim());
            o.setCorrect(i == correct);
            options.add(o);
        }

        Question q = new Question();
        q.setQuizId(quizId);
        q.setQuestionText(questionText.trim());
        q.setMarks(marks);
        q.setOptions(options);

        if (questionDAO.insertQuestionWithOptions(q) > 0) {
            request.getSession().setAttribute("success", "Question added successfully.");
        } else {
            request.getSession().setAttribute("error", "Failed to add question.");
        }
    }
}
