package com.shopsphere.web.filter;

import java.io.IOException;

import com.shopsphere.model.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/** Protects /admin/* : only users with the ADMIN role may continue. */
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("user");

        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login?redirect=%2Fadmin%2Fdashboard");
        } else if (!user.isAdmin()) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: administrators only.");
        } else {
            chain.doFilter(request, response);
        }
    }
}
