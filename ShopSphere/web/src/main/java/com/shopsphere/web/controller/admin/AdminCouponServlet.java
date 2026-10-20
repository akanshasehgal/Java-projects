package com.shopsphere.web.controller.admin;

import java.io.IOException;
import java.sql.Date;

import com.shopsphere.model.Coupon;
import com.shopsphere.service.AdminService;
import com.shopsphere.service.ServiceException;
import com.shopsphere.web.controller.BaseServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/coupons")
public class AdminCouponServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute("coupons", adminService.getCoupons());
        render(req, res, "admin/coupons");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("add".equals(action)) {
                Coupon c = new Coupon();
                c.setCode(req.getParameter("code"));
                c.setDiscountType(Coupon.FLAT.equals(req.getParameter("discountType")) ? Coupon.FLAT : Coupon.PERCENT);
                c.setDiscountValue(doubleParam(req, "discountValue"));
                c.setMinimumOrder(doubleParam(req, "minimumOrder"));
                c.setMaximumDiscount(doubleParam(req, "maximumDiscount"));
                String expiry = req.getParameter("expiryDate");
                if (expiry != null && !expiry.isEmpty()) c.setExpiryDate(Date.valueOf(expiry));
                adminService.addCoupon(c);
                flash(req, "success", "Coupon " + c.getCode() + " created.");
            } else if ("toggle".equals(action)) {
                adminService.setCouponActive(intParam(req, "id", 0), "true".equals(req.getParameter("active")));
                flash(req, "info", "Coupon status updated.");
            }
        } catch (ServiceException e) {
            flash(req, "danger", e.getMessage());
        } catch (IllegalArgumentException e) {
            flash(req, "danger", "Please enter a valid expiry date.");
        }
        redirect(req, res, "/admin/coupons");
    }
}
