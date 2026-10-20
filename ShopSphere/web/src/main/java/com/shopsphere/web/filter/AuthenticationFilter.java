package com.shopsphere.web.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Protects customer pages (cart, checkout, orders, wishlist, profile, reviews).
 * Not logged in?  ->  redirect to the login page, then come back afterwards.
 */
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        if (session != null && session.getAttribute("user") != null) {
            chain.doFilter(request, response);
            return;
        }

        String target = "/home";
        if ("GET".equals(req.getMethod())) {
            target = req.getRequestURI().substring(req.getContextPath().length());
            if (req.getQueryString() != null) target += "?" + req.getQueryString();
        }
        req.getSession().setAttribute("flashType", "warning");
        req.getSession().setAttribute("flashMessage", "Please log in to continue.");
        res.sendRedirect(req.getContextPath() + "/login?redirect=" + URLEncoder.encode(target, StandardCharsets.UTF_8));
    }
}
