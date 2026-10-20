package com.shopsphere.web.controller.admin;

import java.io.IOException;

import com.shopsphere.model.Product;
import com.shopsphere.service.AdminService;
import com.shopsphere.service.OrderService;
import com.shopsphere.service.ProductService;
import com.shopsphere.web.controller.BaseServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Admin pages are protected by AdminFilter (/admin/*). */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final AdminService adminService = new AdminService();
    private final OrderService orderService = new OrderService();
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute("stats", adminService.getStats());
        req.setAttribute("recentOrders", orderService.getRecentOrders(6));
        req.setAttribute("lowStock", productService.getLowStock(Product.LOW_STOCK_LIMIT));
        render(req, res, "admin/dashboard");
    }
}
