package com.shopsphere.web.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Decides the interface language for each request (session, then "lang" cookie, then English)
 * and exposes it as the request attribute "lang" used by the JSTL fmt tags.
 */
public class LocaleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String lang = null;

        HttpSession session = req.getSession(false);
        if (session != null) lang = (String) session.getAttribute("lang");
        if (lang == null && req.getCookies() != null) {
            for (Cookie c : req.getCookies()) {
                if ("lang".equals(c.getName())) lang = c.getValue();
            }
        }
        req.setAttribute("lang", "hi".equals(lang) ? "hi" : "en");
        chain.doFilter(request, response);
    }
}
