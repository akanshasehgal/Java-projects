package com.shopsphere.web.controller;

import java.io.IOException;

import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import com.shopsphere.service.CheckoutService;
import com.shopsphere.service.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Shopping cart. Protected by AuthenticationFilter. */
@WebServlet("/cart")
public class CartServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final CartService cartService = new CartService();
    private final CheckoutService checkoutService = new CheckoutService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User user = currentUser(req);
        String coupon = (String) req.getSession().getAttribute("couponCode");
        req.setAttribute("summary", checkoutService.summarize(user.getUserId(), coupon));
        refreshCartCount(req, user.getUserId());
        render(req, res, "cart");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User user = currentUser(req);
        int productId = intParam(req, "productId", 0);
        String action = req.getParameter("action");
        String next = "/cart";
        try {
            if ("add".equals(action)) {
                cartService.add(user.getUserId(), productId, intParam(req, "quantity", 1));
                if (req.getParameter("buyNow") != null) {
                    next = "/checkout";
                } else {
                    flash(req, "success", "Added to your cart.");
                    next = safeReturnPath(req, "returnTo", "/cart");
                }
            } else if ("update".equals(action)) {
                cartService.update(user.getUserId(), productId, intParam(req, "quantity", 1));
            } else if ("remove".equals(action)) {
                cartService.remove(user.getUserId(), productId);
                flash(req, "info", "Item removed from your cart.");
            }
        } catch (ServiceException e) {
            flash(req, "danger", e.getMessage());
            if ("add".equals(action)) next = safeReturnPath(req, "returnTo", "/cart");
        }
        refreshCartCount(req, user.getUserId());
        redirect(req, res, next);
    }
}
