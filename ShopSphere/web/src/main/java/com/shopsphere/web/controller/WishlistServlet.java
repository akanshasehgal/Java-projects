package com.shopsphere.web.controller;

import java.io.IOException;

import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import com.shopsphere.service.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/wishlist")
public class WishlistServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute("items", cartService.getWishlist(currentUser(req).getUserId()));
        render(req, res, "wishlist");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User user = currentUser(req);
        int productId = intParam(req, "productId", 0);
        String action = req.getParameter("action");
        try {
            if ("add".equals(action)) {
                cartService.addToWishlist(user.getUserId(), productId);
                flash(req, "success", "Saved to your wishlist.");
            } else if ("remove".equals(action)) {
                cartService.removeFromWishlist(user.getUserId(), productId);
                flash(req, "info", "Removed from your wishlist.");
            } else if ("move".equals(action)) {
                cartService.moveToCart(user.getUserId(), productId);
                refreshCartCount(req, user.getUserId());
                flash(req, "success", "Moved to your cart.");
            }
        } catch (ServiceException e) {
            flash(req, "danger", e.getMessage());
        }
        redirect(req, res, safeReturnPath(req, "returnTo", "/wishlist"));
    }
}
