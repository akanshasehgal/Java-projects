package com.shopsphere.web.util;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Keeps the ids of recently viewed products in a cookie, e.g. "7-2-15".
 * Only non-sensitive data is ever stored in cookies.
 */
public final class RecentlyViewed {

    public static final String COOKIE = "recentlyViewed";
    private static final int MAX = 6;

    private RecentlyViewed() {
    }

    public static List<Integer> read(HttpServletRequest req) {
        List<Integer> ids = new ArrayList<>();
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return ids;
        for (Cookie c : cookies) {
            if (!COOKIE.equals(c.getName())) continue;
            for (String part : c.getValue().split("-")) {
                try {
                    ids.add(Integer.parseInt(part));
                } catch (NumberFormatException ignored) {
                    // skip tampered values
                }
            }
        }
        return ids;
    }

    public static void add(HttpServletRequest req, HttpServletResponse res, int productId) {
        List<Integer> ids = read(req);
        ids.remove(Integer.valueOf(productId));
        ids.add(0, productId);
        while (ids.size() > MAX) ids.remove(ids.size() - 1);

        StringBuilder value = new StringBuilder();
        for (Integer id : ids) value.append(value.length() == 0 ? "" : "-").append(id);

        Cookie cookie = new Cookie(COOKIE, value.toString());
        cookie.setMaxAge(30 * 24 * 60 * 60);
        cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
        cookie.setHttpOnly(true);
        res.addCookie(cookie);
    }
}
