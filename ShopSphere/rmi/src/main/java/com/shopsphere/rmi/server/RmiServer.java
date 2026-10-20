package com.shopsphere.rmi.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.shopsphere.rmi.remote.InventoryService;

/**
 * Starts the RMI registry (port 1099) inside this JVM and registers the
 * remote inventory service under the name "InventoryService".
 *
 *   RMI Registry  <--  Naming.rebind("rmi://localhost:1099/InventoryService", impl)
 */
public class RmiServer {

    public static final int REGISTRY_PORT = 1099;

    // Static references stop the registry and the remote object from being garbage-collected
    private static Registry registry;
    private static InventoryServiceImpl service;

    public static void main(String[] args) throws Exception {
        try {
            registry = LocateRegistry.createRegistry(REGISTRY_PORT);
            System.out.println("RMI registry created on port " + REGISTRY_PORT);
        } catch (RemoteException e) {
            registry = LocateRegistry.getRegistry(REGISTRY_PORT);
            registry.list();      // throws if nothing is listening
            System.out.println("Using the RMI registry that is already running on port " + REGISTRY_PORT);
        }

        service = new InventoryServiceImpl();
        String url = "rmi://localhost:" + REGISTRY_PORT + "/" + InventoryService.NAME;
        Naming.rebind(url, service);

        System.out.println("InventoryService bound as " + url);
        System.out.println("Server is ready. Waiting for remote calls... (Ctrl+C to stop)");
    }
}
