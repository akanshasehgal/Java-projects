package com.shopsphere.web.controller;

import java.io.IOException;

import com.shopsphere.service.ProductService;
import com.shopsphere.service.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/review")
public class ReviewServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final ProductService productService = new ProductService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int productId = intParam(req, "productId", 0);
        try {
            productService.addReview(currentUser(req).getUserId(), productId,
                    intParam(req, "rating", 0), req.getParameter("reviewText"));
            flash(req, "success", "Thanks for your review!");
        } catch (ServiceException e) {
            flash(req, "danger", e.getMessage());
        }
        redirect(req, res, "/product?id=" + productId + "#reviews");
    }
}
