package com.shopsphere.network.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.shopsphere.model.Product;

/** The server's answer. Product objects travel inside it thanks to Java serialization. */
public class InventoryResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final String message;
    private Product product;
    private ArrayList<Product> products = new ArrayList<>();
    private int stock = -1;

    public InventoryResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = new ArrayList<>(products); }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
