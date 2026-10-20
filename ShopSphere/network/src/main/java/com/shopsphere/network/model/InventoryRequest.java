package com.shopsphere.network.model;

import java.io.Serializable;

/** A command sent from the client to the server as a serialized object. */
public class InventoryRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String GET_PRODUCT = "GET_PRODUCT";
    public static final String GET_STOCK = "GET_STOCK";
    public static final String SEARCH_PRODUCT = "SEARCH_PRODUCT";
    public static final String QUIT = "QUIT";

    private final String command;
    private final String argument;

    public InventoryRequest(String command, String argument) {
        this.command = command;
        this.argument = argument;
    }

    public String getCommand() { return command; }
    public String getArgument() { return argument; }

    @Override
    public String toString() {
        return command + (argument == null ? "" : " " + argument);
    }
}
