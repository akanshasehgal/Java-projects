package com.shopsphere.web.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Switches the interface language (Internationalization): /language?lang=en or /language?lang=hi */
@WebServlet("/language")
public class LanguageServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String lang = "hi".equals(req.getParameter("lang")) ? "hi" : "en";
        req.getSession().setAttribute("lang", lang);

        Cookie cookie = new Cookie("lang", lang);
        cookie.setMaxAge(365 * 24 * 60 * 60);
        cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
        res.addCookie(cookie);

        res.sendRedirect(backTo(req));
    }

    /** Goes back to the page the user was on, using only the path of the Referer header. */
    private String backTo(HttpServletRequest req) {
        String referer = req.getHeader("Referer");
        String home = req.getContextPath() + "/home";
        if (referer == null) return home;
        try {
            URI uri = new URI(referer);
            String path = uri.getRawPath();
            if (path == null || !path.startsWith(req.getContextPath() + "/") || path.startsWith("//")) return home;
            return uri.getRawQuery() == null ? path : path + "?" + uri.getRawQuery();
        } catch (URISyntaxException e) {
            return home;
        }
    }
}
