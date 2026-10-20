package com.shopsphere.web.controller;

import java.io.IOException;

import com.shopsphere.model.Order;
import com.shopsphere.model.User;
import com.shopsphere.service.OrderService;
import com.shopsphere.service.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** /orders = history, /order?id=N = details and tracking, /order-success?id=N = confirmation. */
@WebServlet({"/orders", "/order", "/order-success"})
public class OrderServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User user = currentUser(req);
        if ("/orders".equals(req.getServletPath())) {
            req.setAttribute("orders", orderService.getOrdersForUser(user.getUserId()));
            render(req, res, "orders");
            return;
        }
        Order order = orderService.getOrderForUser(intParam(req, "id", 0), user.getUserId());
        if (order == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
            return;
        }
        req.setAttribute("order", order);
        req.setAttribute("payment", orderService.getPayment(order.getOrderId()));
        req.setAttribute("statusFlow", Order.STATUS_FLOW);
        render(req, res, "/order-success".equals(req.getServletPath()) ? "order-success" : "order-details");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int orderId = intParam(req, "id", 0);
        if ("cancel".equals(req.getParameter("action"))) {
            try {
                orderService.cancelByCustomer(orderId, currentUser(req).getUserId());
                flash(req, "info", "Order #" + orderId + " has been cancelled.");
            } catch (ServiceException e) {
                flash(req, "danger", e.getMessage());
            }
        }
        redirect(req, res, "/order?id=" + orderId);
    }
}
