package com.shopsphere.dao;

/** Unchecked wrapper for SQLException so callers are not forced to catch it everywhere. */
public class DAOException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
