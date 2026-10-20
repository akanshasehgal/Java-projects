package com.shopsphere.rmi.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.shopsphere.model.Product;

/**
 * Remote interface of the ShopSphere inventory service.
 * Every method can be called from another JVM through RMI.
 */
public interface InventoryService extends Remote {

    String NAME = "InventoryService";

    Product getProduct(int id) throws RemoteException;

    int getStock(int productId) throws RemoteException;

    /** Sets the stock of a product to the given quantity (restocking). */
    boolean updateStock(int productId, int quantity) throws RemoteException;

    List<Product> getLowStockProducts(int threshold) throws RemoteException;
}
