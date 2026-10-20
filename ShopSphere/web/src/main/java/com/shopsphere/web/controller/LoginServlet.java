package com.shopsphere.web.controller;

import java.io.IOException;

import com.shopsphere.model.User;
import com.shopsphere.service.AuthService;
import com.shopsphere.service.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/** Login for both customers and admins (admins are sent to the admin dashboard). */
@WebServlet("/login")
public class LoginServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    private static final String REMEMBER_COOKIE = "rememberEmail";

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (currentUser(req) != null) {
            redirect(req, res, "/home");
            return;
        }
        render(req, res, "login");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String email = req.getParameter("email");
        try {
            User user = authService.login(email, req.getParameter("password"));

            // new session id after login prevents session fixation
            if (req.getSession(false) != null) req.changeSessionId();
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());
            refreshCartCount(req, user.getUserId());

            rememberEmail(req, res, "on".equals(req.getParameter("remember")) ? user.getEmail() : null);
            flash(req, "success", "Welcome back, " + user.getName() + "!");

            if (user.isAdmin()) redirect(req, res, "/admin/dashboard");
            else redirect(req, res, safeReturnPath(req, "redirect", "/home"));
        } catch (ServiceException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("email", email);
            render(req, res, "login");
        }
    }

    /** Convenience cookie that pre-fills the email field. The password is never stored in a cookie. */
    private void rememberEmail(HttpServletRequest req, HttpServletResponse res, String email) {
        Cookie cookie = new Cookie(REMEMBER_COOKIE, email == null ? "" : email);
        cookie.setMaxAge(email == null ? 0 : 30 * 24 * 60 * 60);
        cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
        cookie.setHttpOnly(true);
        res.addCookie(cookie);
    }
}
