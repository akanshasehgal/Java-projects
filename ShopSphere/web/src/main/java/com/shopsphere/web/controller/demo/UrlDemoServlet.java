package com.shopsphere.web.controller.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * java.net URL / URLConnection demonstration (content and protocol handlers):
 * splits a URL into its parts and reads the response headers and the first part of the content.
 */
@WebServlet("/demo/url")
public class UrlDemoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int PREVIEW_CHARS = 600;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String base = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
        String address = req.getParameter("url");
        if (address == null || address.trim().isEmpty()) address = base + "/demo/feed.xml";
        req.setAttribute("address", address.trim());
        req.setAttribute("encodedExample", URLEncoder.encode("java books & laptops", StandardCharsets.UTF_8));

        try {
            URL url = new URI(address.trim()).toURL();
            Map<String, Object> parts = new LinkedHashMap<>();
            parts.put("Protocol", url.getProtocol());
            parts.put("Host", url.getHost());
            parts.put("Port", url.getPort() == -1 ? "(default " + url.getDefaultPort() + ")" : url.getPort());
            parts.put("Path", url.getPath());
            parts.put("Query", url.getQuery());
            parts.put("File", url.getFile());
            req.setAttribute("parts", parts);

            if (!"http".equals(url.getProtocol()) && !"https".equals(url.getProtocol())) {
                throw new IOException("Only http and https URLs are allowed in this demo.");
            }

            URLConnection connection = url.openConnection();          // protocol handler chosen from the URL scheme
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            Map<String, Object> info = new LinkedHashMap<>();
            if (connection instanceof HttpURLConnection) {
                HttpURLConnection http = (HttpURLConnection) connection;
                info.put("Response code", http.getResponseCode() + " " + http.getResponseMessage());
            }
            info.put("Content type", connection.getContentType());
            info.put("Content length", connection.getContentLengthLong());
            info.put("Content encoding", connection.getContentEncoding());
            info.put("Last modified", connection.getLastModified() == 0 ? null : new java.util.Date(connection.getLastModified()));
            info.put("Server", connection.getHeaderField("Server"));
            req.setAttribute("info", info);

            try (InputStream in = connection.getInputStream()) {
                byte[] bytes = in.readNBytes(PREVIEW_CHARS * 2);
                String preview = new String(bytes, StandardCharsets.UTF_8);
                req.setAttribute("preview", preview.length() > PREVIEW_CHARS ? preview.substring(0, PREVIEW_CHARS) + " ..." : preview);
            }
        } catch (Exception e) {
            req.setAttribute("error", e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/demo/url.jsp").forward(req, res);
    }
}
