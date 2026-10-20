package com.shopsphere.web.controller.admin;

import java.io.IOException;

import com.shopsphere.service.AdminService;
import com.shopsphere.service.ServiceException;
import com.shopsphere.web.controller.BaseServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/users")
public class AdminUserServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute("users", adminService.getUsers());
        render(req, res, "admin/users");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            boolean block = "true".equals(req.getParameter("block"));
            adminService.setUserBlocked(intParam(req, "id", 0), block, currentUser(req).getUserId());
            flash(req, "info", block ? "Customer blocked." : "Customer unblocked.");
        } catch (ServiceException e) {
            flash(req, "danger", e.getMessage());
        }
        redirect(req, res, "/admin/users");
    }
}
