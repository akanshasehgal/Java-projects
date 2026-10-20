package com.shopsphere.web.controller;

import java.io.IOException;

import com.shopsphere.service.ProductService;
import com.shopsphere.web.util.RecentlyViewed;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/home")
public class HomeServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute("categories", productService.getActiveCategories());
        req.setAttribute("deals", productService.getTopDeals(4));
        req.setAttribute("latest", productService.getLatest(8));
        req.setAttribute("recent", productService.getByIds(RecentlyViewed.read(req)));
        render(req, res, "home");
    }
}
