package com.shopsphere.web.controller;

import java.io.IOException;

import com.shopsphere.model.Address;
import com.shopsphere.model.User;
import com.shopsphere.service.AddressService;
import com.shopsphere.service.AuthService;
import com.shopsphere.service.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Profile details, password change and address book. */
@WebServlet("/profile")
public class ProfileServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final AuthService authService = new AuthService();
    private final AddressService addressService = new AddressService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute("addresses", addressService.getAddresses(currentUser(req).getUserId()));
        req.setAttribute("addressTypes", AddressService.TYPES);
        render(req, res, "profile");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User user = currentUser(req);
        String action = req.getParameter("action");
        try {
            if ("update".equals(action)) {
                User updated = authService.updateProfile(user.getUserId(), req.getParameter("name"), req.getParameter("mobile"));
                req.getSession().setAttribute("user", updated);
                flash(req, "success", "Profile updated.");
            } else if ("changePassword".equals(action)) {
                authService.changePassword(user.getUserId(), req.getParameter("currentPassword"),
                        req.getParameter("newPassword"), req.getParameter("confirmPassword"));
                flash(req, "success", "Password changed successfully.");
            } else if ("addAddress".equals(action)) {
                Address a = new Address();
                a.setUserId(user.getUserId());
                a.setAddressLine(req.getParameter("addressLine"));
                a.setCity(req.getParameter("city"));
                a.setState(req.getParameter("state"));
                a.setPincode(req.getParameter("pincode"));
                a.setAddressType(req.getParameter("addressType"));
                addressService.addAddress(a);
                flash(req, "success", "Address added.");
            } else if ("deleteAddress".equals(action)) {
                addressService.deleteAddress(intParam(req, "addressId", 0), user.getUserId());
                flash(req, "info", "Address removed.");
            }
        } catch (ServiceException e) {
            flash(req, "danger", e.getMessage());
        }
        redirect(req, res, "/profile");
    }
}
