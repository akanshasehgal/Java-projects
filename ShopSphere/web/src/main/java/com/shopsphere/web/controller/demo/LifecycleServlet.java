package com.shopsphere.web.controller.demo;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Demonstrates the Servlet life cycle, ServletConfig and ServletContext.
 * Mapped in web.xml (with an init-param and load-on-startup) instead of an annotation.
 */
public class LifecycleServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final AtomicInteger requestCount = new AtomicInteger();
    private Date initTime;
    private String welcomeMessage;

    /** Called once by the container when the servlet is loaded. */
    @Override
    public void init() throws ServletException {
        initTime = new Date();
        ServletConfig config = getServletConfig();
        welcomeMessage = config.getInitParameter("welcomeMessage");     // servlet-specific configuration
        log("LifecycleServlet.init() called at " + initTime);
    }

    /** Called for every request; dispatches to doGet()/doPost(). */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        requestCount.incrementAndGet();
        super.service(req, res);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ServletContext context = getServletContext();                    // application-wide information
        req.setAttribute("servletName", getServletName());
        req.setAttribute("initTime", initTime);
        req.setAttribute("requestCount", requestCount.get());
        req.setAttribute("welcomeMessage", welcomeMessage);
        req.setAttribute("appName", context.getAttribute("appName"));
        req.setAttribute("startTime", context.getAttribute("startTime"));
        req.setAttribute("activeSessions", context.getAttribute("activeSessions"));
        req.setAttribute("supportEmail", context.getInitParameter("supportEmail"));
        req.setAttribute("serverInfo", context.getServerInfo());
        req.setAttribute("servletVersion", context.getMajorVersion() + "." + context.getMinorVersion());
        req.setAttribute("method", req.getMethod());
        req.setAttribute("protocol", req.getProtocol());
        req.setAttribute("userAgent", req.getHeader("User-Agent"));
        req.getRequestDispatcher("/WEB-INF/views/demo/lifecycle.jsp").forward(req, res);
    }

    /** Called once when the application is stopped or undeployed. */
    @Override
    public void destroy() {
        log("LifecycleServlet.destroy() called after " + requestCount.get() + " requests");
    }
}
