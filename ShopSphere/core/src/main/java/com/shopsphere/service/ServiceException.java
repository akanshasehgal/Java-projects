package com.shopsphere.service;

/** A business-rule failure whose message can be shown directly to the user. */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ServiceException(String message) {
        super(message);
    }
}
