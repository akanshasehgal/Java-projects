package com.shopsphere.rmi.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;
import com.shopsphere.rmi.remote.InventoryService;

/** Remote object: implements the remote interface using the same DAO as the web store (RMI -> DAO -> MySQL). */
public class InventoryServiceImpl extends UnicastRemoteObject implements InventoryService {

    private static final long serialVersionUID = 1L;

    private final transient ProductDAO productDAO = new ProductDAO();

    public InventoryServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public Product getProduct(int id) throws RemoteException {
        log("getProduct(" + id + ")");
        return productDAO.findById(id);
    }

    @Override
    public int getStock(int productId) throws RemoteException {
        log("getStock(" + productId + ")");
        Product p = productDAO.findById(productId);
        return p == null ? -1 : p.getStock();
    }

    @Override
    public boolean updateStock(int productId, int quantity) throws RemoteException {
        log("updateStock(" + productId + ", " + quantity + ")");
        if (quantity < 0) return false;
        return productDAO.updateStock(productId, quantity);
    }

    @Override
    public List<Product> getLowStockProducts(int threshold) throws RemoteException {
        log("getLowStockProducts(" + threshold + ")");
        return new ArrayList<>(productDAO.findLowStock(threshold));
    }

    private void log(String call) {
        System.out.println("Remote call: " + call);
    }
}
