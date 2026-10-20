package com.shopsphere.model;

import java.io.Serializable;

public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private int cartId;
    private int userId;
    private Product product;
    private int quantity;

    public double getSubtotal() {
        return Math.round(product.getFinalPrice() * quantity * 100) / 100.0;
    }

    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
