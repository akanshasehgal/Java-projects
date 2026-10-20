package com.shopsphere.web.controller;

import java.io.IOException;

import com.shopsphere.model.User;
import com.shopsphere.service.AuthService;
import com.shopsphere.service.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/register")
public class RegisterServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        render(req, res, "register");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            User user = authService.register(
                    req.getParameter("name"),
                    req.getParameter("email"),
                    req.getParameter("password"),
                    req.getParameter("confirmPassword"),
                    req.getParameter("mobile"));

            if (req.getSession(false) != null) req.changeSessionId();
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());
            session.setAttribute("cartCount", 0);
            flash(req, "success", "Account created! Welcome to ShopSphere, " + user.getName() + ".");
            redirect(req, res, "/home");
        } catch (ServiceException e) {
            req.setAttribute("error", e.getMessage());
            render(req, res, "register");
        }
    }
}
