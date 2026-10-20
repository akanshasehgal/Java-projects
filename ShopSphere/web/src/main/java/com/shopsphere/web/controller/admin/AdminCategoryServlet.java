package com.shopsphere.web.controller.admin;

import java.io.IOException;

import com.shopsphere.model.Category;
import com.shopsphere.service.ProductService;
import com.shopsphere.service.ServiceException;
import com.shopsphere.web.controller.BaseServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/categories")
public class AdminCategoryServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int editId = intParam(req, "edit", 0);
        Category editing = null;
        for (Category c : productService.getAllCategories()) {
            if (c.getCategoryId() == editId) editing = c;
        }
        req.setAttribute("categories", productService.getAllCategories());
        req.setAttribute("editing", editing);
        render(req, res, "admin/categories");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        int id = intParam(req, "id", 0);
        try {
            if ("save".equals(action)) {
                Category c = new Category();
                c.setCategoryId(id);
                c.setName(req.getParameter("name"));
                c.setDescription(req.getParameter("description"));
                c.setStatus(!"false".equals(req.getParameter("status")));
                productService.saveCategory(c);
                flash(req, "success", "Category saved.");
            } else if ("toggle".equals(action)) {
                productService.setCategoryStatus(id, "true".equals(req.getParameter("active")));
                flash(req, "info", "Category status updated.");
            } else if ("delete".equals(action)) {
                productService.deleteCategory(id);
                flash(req, "info", "Category deleted. Its products are now uncategorised.");
            }
        } catch (ServiceException e) {
            flash(req, "danger", e.getMessage());
        }
        redirect(req, res, "/admin/categories");
    }
}
