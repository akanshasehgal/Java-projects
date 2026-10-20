package com.shopsphere.web.controller;

import java.io.IOException;

import com.shopsphere.model.User;
import com.shopsphere.service.CartService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Common helpers for all ShopSphere controllers:
 * rendering JSP views, redirects, flash messages and request parameters.
 */
public abstract class BaseServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected static final String VIEWS = "/WEB-INF/views/";

    /** Forwards to /WEB-INF/views/{view}.jsp (views are not directly reachable from the browser). */
    protected void render(HttpServletRequest req, HttpServletResponse res, String view)
            throws ServletException, IOException {
        req.getRequestDispatcher(VIEWS + view + ".jsp").forward(req, res);
    }

    protected void redirect(HttpServletRequest req, HttpServletResponse res, String path) throws IOException {
        res.sendRedirect(req.getContextPath() + path);
    }

    protected User currentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (User) session.getAttribute("user");
    }

    /** A one-time message shown on the next page (Post/Redirect/Get pattern). */
    protected void flash(HttpServletRequest req, String type, String message) {
        HttpSession session = req.getSession();
        session.setAttribute("flashType", type);
        session.setAttribute("flashMessage", message);
    }

    protected void refreshCartCount(HttpServletRequest req, int userId) {
        req.getSession().setAttribute("cartCount", new CartService().getItemCount(userId));
    }

    protected int intParam(HttpServletRequest req, String name, int defaultValue) {
        try {
            return Integer.parseInt(req.getParameter(name).trim());
        } catch (RuntimeException e) {
            return defaultValue;
        }
    }

    protected Integer optionalInt(HttpServletRequest req, String name) {
        try {
            return Integer.valueOf(req.getParameter(name).trim());
        } catch (RuntimeException e) {
            return null;
        }
    }

    protected Double optionalDouble(HttpServletRequest req, String name) {
        try {
            return Double.valueOf(req.getParameter(name).trim());
        } catch (RuntimeException e) {
            return null;
        }
    }

    protected double doubleParam(HttpServletRequest req, String name) {
        Double d = optionalDouble(req, name);
        return d == null ? 0 : d;
    }

    /** A local path to return to after an action, e.g. "/product?id=3". Rejects external URLs. */
    protected String safeReturnPath(HttpServletRequest req, String paramName, String fallback) {
        String path = req.getParameter(paramName);
        if (path == null || !path.startsWith("/") || path.startsWith("//") || path.contains("\\")) return fallback;
        return path;
    }
}
