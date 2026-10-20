package com.shopsphere.web.controller;

import java.io.IOException;

import com.shopsphere.model.Address;
import com.shopsphere.model.CartSummary;
import com.shopsphere.model.User;
import com.shopsphere.service.AddressService;
import com.shopsphere.service.CheckoutService;
import com.shopsphere.service.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/** Address, coupon and (mock) payment, then order placement. */
@WebServlet("/checkout")
public class CheckoutServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final CheckoutService checkoutService = new CheckoutService();
    private final AddressService addressService = new AddressService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User user = currentUser(req);
        HttpSession session = req.getSession();
        CartSummary summary = checkoutService.summarize(user.getUserId(), (String) session.getAttribute("couponCode"));
        if (summary.isEmpty()) {
            flash(req, "warning", "Your cart is empty. Add some products first.");
            redirect(req, res, "/products");
            return;
        }
        if (summary.getCouponError() != null) session.removeAttribute("couponCode");

        req.setAttribute("summary", summary);
        req.setAttribute("addresses", addressService.getAddresses(user.getUserId()));
        req.setAttribute("coupons", checkoutService.getAvailableCoupons());
        req.setAttribute("paymentMethods", CheckoutService.PAYMENT_METHODS);
        render(req, res, "checkout");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User user = currentUser(req);
        HttpSession session = req.getSession();
        String action = req.getParameter("action");
        try {
            switch (action == null ? "" : action) {
                case "applyCoupon": {
                    String code = req.getParameter("couponCode");
                    CartSummary s = checkoutService.summarize(user.getUserId(), code);
                    if (s.getCouponError() != null) throw new ServiceException(s.getCouponError());
                    if (s.getCoupon() == null) throw new ServiceException("Please enter a coupon code.");
                    session.setAttribute("couponCode", s.getCoupon().getCode());
                    flash(req, "success", "Coupon " + s.getCoupon().getCode() + " applied. You save Rs." + s.getDiscount());
                    break;
                }
                case "removeCoupon":
                    session.removeAttribute("couponCode");
                    break;
                case "addAddress": {
                    Address a = new Address();
                    a.setUserId(user.getUserId());
                    a.setAddressLine(req.getParameter("addressLine"));
                    a.setCity(req.getParameter("city"));
                    a.setState(req.getParameter("state"));
                    a.setPincode(req.getParameter("pincode"));
                    a.setAddressType(req.getParameter("addressType"));
                    addressService.addAddress(a);
                    flash(req, "success", "Address added.");
                    break;
                }
                case "placeOrder": {
                    int orderId = checkoutService.placeOrder(user.getUserId(), intParam(req, "addressId", 0),
                            (String) session.getAttribute("couponCode"), req.getParameter("paymentMethod"));
                    session.removeAttribute("couponCode");
                    refreshCartCount(req, user.getUserId());
                    redirect(req, res, "/order-success?id=" + orderId);
                    return;
                }
                default:
                    break;
            }
        } catch (ServiceException e) {
            flash(req, "danger", e.getMessage());
        }
        redirect(req, res, "/checkout");
    }
}
