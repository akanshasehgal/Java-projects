package com.shopsphere.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.shopsphere.model.Product;
import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import com.shopsphere.service.ProductService;
import com.shopsphere.web.util.RecentlyViewed;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** /products = listing with search, filter and sort.  /product?id=N = product details. */
@WebServlet({"/products", "/product"})
public class ProductServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;

    private final ProductService productService = new ProductService();
    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if ("/product".equals(req.getServletPath())) {
            showDetails(req, res);
        } else {
            showListing(req, res);
        }
    }

    private void showListing(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String q = req.getParameter("q");
        Integer category = optionalInt(req, "category");
        Double min = optionalDouble(req, "min");
        Double max = optionalDouble(req, "max");
        String sort = req.getParameter("sort");

        req.setAttribute("products", productService.search(q, category, min, max, sort));
        req.setAttribute("categories", productService.getActiveCategories());
        req.setAttribute("selectedCategory", category);
        render(req, res, "products");
    }

    private void showDetails(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Product product = productService.getActiveProduct(intParam(req, "id", 0));
        if (product == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
            return;
        }
        RecentlyViewed.add(req, res, product.getProductId());

        List<Product> related = new ArrayList<>();
        for (Product p : productService.search(null, product.getCategoryId(), null, null, "rating")) {
            if (p.getProductId() != product.getProductId() && related.size() < 4) related.add(p);
        }

        User user = currentUser(req);
        req.setAttribute("product", product);
        req.setAttribute("reviews", productService.getReviews(product.getProductId()));
        req.setAttribute("related", related);
        req.setAttribute("inWishlist", user != null && cartService.isInWishlist(user.getUserId(), product.getProductId()));
        render(req, res, "product-details");
    }
}
