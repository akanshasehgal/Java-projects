package com.shopsphere.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class WishlistItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private int wishlistId;
    private int userId;
    private Product product;
    private Timestamp createdAt;

    public int getWishlistId() { return wishlistId; }
    public void setWishlistId(int wishlistId) { this.wishlistId = wishlistId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
