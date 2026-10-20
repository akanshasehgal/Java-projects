package com.shopsphere.web.listener;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

import com.shopsphere.dao.DBConnection;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/** Application start-up and shut-down events. */
@WebListener
public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext ctx = event.getServletContext();
        String appName = ctx.getInitParameter("appName");
        ctx.setAttribute("appName", appName == null ? "ShopSphere" : appName);
        ctx.setAttribute("startTime", new Date());
        ctx.setAttribute("activeSessions", new AtomicInteger());

        try (Connection con = DBConnection.getConnection()) {
            ctx.log("ShopSphere started - connected to " + con.getMetaData().getURL());
        } catch (SQLException e) {
            ctx.log("ShopSphere started, but the database is NOT reachable: " + e.getMessage()
                    + "  (check core/src/main/resources/db.properties)");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // Deregister JDBC drivers loaded by this web application to avoid memory leaks on redeploy
        ClassLoader webappLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == webappLoader) {
                try {
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException ignored) {
                    // shutting down anyway
                }
            }
        }
        event.getServletContext().log("ShopSphere stopped");
    }
}
