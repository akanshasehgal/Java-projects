package com.shopsphere.web.listener;

import java.util.concurrent.atomic.AtomicInteger;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

/** Counts active sessions (shown on the Servlet life-cycle demo page). */
@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        counter(event).incrementAndGet();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        counter(event).decrementAndGet();
    }

    private AtomicInteger counter(HttpSessionEvent event) {
        Object counter = event.getSession().getServletContext().getAttribute("activeSessions");
        return counter instanceof AtomicInteger ? (AtomicInteger) counter : new AtomicInteger();
    }
}
