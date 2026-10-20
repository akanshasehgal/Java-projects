package com.shopsphere.web.controller.admin;

import java.io.IOException;

import com.shopsphere.model.Order;
import com.shopsphere.service.OrderService;
import com.shopsphere.service.ServiceException;
import com.shopsphere.web.controller.BaseServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/orders")
public class AdminOrderServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute("statuses", Order.ALL_STATUSES);
        Integer id = optionalInt(req, "id");
        if (id != null) {
            Order order = orderService.getOrder(id);
            if (order == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
                return;
            }
            req.setAttribute("order", order);
            req.setAttribute("payment", orderService.getPayment(id));
            req.setAttribute("statusFlow", Order.STATUS_FLOW);
            render(req, res, "admin/order-details");
            return;
        }
        req.setAttribute("orders", orderService.getAllOrders(req.getParameter("status")));
        render(req, res, "admin/orders");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int id = intParam(req, "id", 0);
        try {
            orderService.updateStatus(id, req.getParameter("status"));
            flash(req, "success", "Order #" + id + " is now " + req.getParameter("status") + ".");
        } catch (ServiceException e) {
            flash(req, "danger", e.getMessage());
        }
        redirect(req, res, "/admin/orders?id=" + id);
    }
}
