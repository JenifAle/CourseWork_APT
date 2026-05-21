package com.quizsystem.controller;

import com.quizsystem.dao.CategoryDAO;
import com.quizsystem.model.Category;
import com.quizsystem.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * CRUD controller for categories.
 *
 * Routes:
 *   GET  /admin/categories                : list all
 *   POST /admin/categories?action=add     : add new
 *   POST /admin/categories?action=update  : update
 *   POST /admin/categories?action=delete  : delete
 */
@WebServlet("/admin/categories")
public class CategoryServlet extends HttpServlet {

    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("categories", categoryDAO.getAllCategories());
        request.getRequestDispatcher("/pages/admin/manageCategories.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String action = request.getParameter("action");
        if (action == null) action = "add";

        try {
            switch (action) {
                case "add":    handleAdd(request);    break;
                case "update": handleUpdate(request); break;
                case "delete": handleDelete(request); break;
                default:       request.getSession().setAttribute("error", "Unknown action.");
            }
        } catch (Exception ex) {
            request.getSession().setAttribute("error", "Operation failed: " + ex.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/categories");
    }

    private void handleAdd(HttpServletRequest request) {
        String name = request.getParameter("categoryName");
        String desc = request.getParameter("description");

        if (ValidationUtil.isNullOrEmpty(name)) {
            request.getSession().setAttribute("error", "Category name is required.");
            return;
        }

        Category c = new Category(name.trim(), desc != null ? desc.trim() : "");
        if (categoryDAO.insertCategory(c)) {
            request.getSession().setAttribute("success", "Category added successfully.");
        } else {
            request.getSession().setAttribute("error",
                    "Failed to add category. The name may already exist.");
        }
    }

    private void handleUpdate(HttpServletRequest request) {
        int id      = Integer.parseInt(request.getParameter("categoryId"));
        String name = request.getParameter("categoryName");
        String desc = request.getParameter("description");

        if (ValidationUtil.isNullOrEmpty(name)) {
            request.getSession().setAttribute("error", "Category name is required.");
            return;
        }

        Category c = new Category();
        c.setCategoryId(id);
        c.setCategoryName(name.trim());
        c.setDescription(desc != null ? desc.trim() : "");

        if (categoryDAO.updateCategory(c)) {
            request.getSession().setAttribute("success", "Category updated successfully.");
        } else {
            request.getSession().setAttribute("error", "Failed to update category.");
        }
    }

    private void handleDelete(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("categoryId"));
        if (categoryDAO.deleteCategory(id)) {
            request.getSession().setAttribute("success", "Category deleted successfully.");
        } else {
            request.getSession().setAttribute("error", "Failed to delete category.");
        }
    }
}
