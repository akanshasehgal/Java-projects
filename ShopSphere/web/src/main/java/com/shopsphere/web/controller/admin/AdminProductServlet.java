package com.shopsphere.web.controller.admin;

import java.io.IOException;

import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;
import com.shopsphere.service.ServiceException;
import com.shopsphere.web.controller.BaseServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Product CRUD and inventory. */
@WebServlet("/admin/products")
public class AdminProductServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("new".equals(action) || "edit".equals(action)) {
            Product p = "edit".equals(action) ? productService.getProduct(intParam(req, "id", 0)) : new Product();
            if (p == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                return;
            }
            showForm(req, res, p, null);
            return;
        }
        String q = req.getParameter("q");
        req.setAttribute("products", q == null || q.trim().isEmpty()
                ? productService.getAllProducts() : productService.searchAll(q));
        render(req, res, "admin/products");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        int id = intParam(req, "id", 0);

        if ("save".equals(action)) {
            Product p = new Product();
            p.setProductId(id);
            p.setCategoryId(intParam(req, "categoryId", 0));
            p.setName(req.getParameter("name"));
            p.setBrand(req.getParameter("brand"));
            p.setDescription(req.getParameter("description"));
            p.setPrice(doubleParam(req, "price"));
            p.setDiscount(doubleParam(req, "discount"));
            p.setStock(intParam(req, "stock", 0));
            String image = req.getParameter("imageUrl");
            p.setImageUrl(image == null || image.trim().isEmpty() ? null : image.trim());
            p.setStatus(req.getParameter("status") != null);
            try {
                productService.saveProduct(p);
                flash(req, "success", "Product \"" + p.getName() + "\" saved.");
                redirect(req, res, "/admin/products");
            } catch (ServiceException e) {
                showForm(req, res, p, e.getMessage());
            }
            return;
        }

        try {
            if ("toggle".equals(action)) {
                boolean active = "true".equals(req.getParameter("active"));
                productService.setProductStatus(id, active);
                flash(req, "info", active ? "Product activated." : "Product hidden from the store.");
            } else if ("stock".equals(action)) {
                productService.updateStock(id, intParam(req, "stock", -1));
                flash(req, "success", "Stock updated.");
            }
        } catch (ServiceException e) {
            flash(req, "danger", e.getMessage());
        }
        redirect(req, res, safeReturnPath(req, "returnTo", "/admin/products"));
    }

    private void showForm(HttpServletRequest req, HttpServletResponse res, Product p, String error)
            throws ServletException, IOException {
        req.setAttribute("product", p);
        req.setAttribute("categories", productService.getAllCategories());
        req.setAttribute("error", error);
        render(req, res, "admin/product-form");
    }
}
