package com.shopsphere.web.controller.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** XML product feed, read by the JSTL XML demo page (demo/jstl-xml.jsp). */
@WebServlet("/demo/feed.xml")
public class ProductFeedServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/xml;charset=UTF-8");
        PrintWriter out = res.getWriter();
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        out.println("<products store=\"ShopSphere\">");
        for (Product p : productService.getLatest(10)) {
            out.printf(Locale.ROOT, "  <product id=\"%d\" inStock=\"%b\">%n", p.getProductId(), p.isInStock());
            out.println("    <name>" + escape(p.getName()) + "</name>");
            out.println("    <brand>" + escape(p.getBrand()) + "</brand>");
            out.println("    <category>" + escape(p.getCategoryName()) + "</category>");
            out.printf(Locale.ROOT, "    <price>%.2f</price>%n", p.getFinalPrice());
            out.printf(Locale.ROOT, "    <discount>%.0f</discount>%n", p.getDiscount());
            out.println("  </product>");
        }
        out.println("</products>");
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&apos;");
    }
}
