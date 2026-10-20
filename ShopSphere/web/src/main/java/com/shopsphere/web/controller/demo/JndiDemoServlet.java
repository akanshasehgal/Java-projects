package com.shopsphere.web.controller.demo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JNDI demonstration: looks up the connection pool defined in META-INF/context.xml
 * and an environment entry defined in web.xml.
 */
@WebServlet("/demo/jndi")
public class JndiDemoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            Context ctx = new InitialContext();
            Context env = (Context) ctx.lookup("java:comp/env");

            req.setAttribute("appVersion", env.lookup("appVersion"));

            DataSource ds = (DataSource) env.lookup("jdbc/ShopSphereDB");
            req.setAttribute("dataSourceClass", ds.getClass().getName());
            try (Connection con = ds.getConnection();
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM products")) {
                rs.next();
                req.setAttribute("productCount", rs.getInt(1));
                req.setAttribute("databaseProduct", con.getMetaData().getDatabaseProductName() + " "
                        + con.getMetaData().getDatabaseProductVersion());
            }
        } catch (NamingException | SQLException e) {
            req.setAttribute("error", e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/demo/jndi.jsp").forward(req, res);
    }
}
