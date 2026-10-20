package com.shopsphere.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * A product in the store. Serializable so it can be sent over
 * sockets (network module) and RMI (rmi module).
 */
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int LOW_STOCK_LIMIT = 5;

    private int productId;
    private int categoryId;
    private String categoryName;
    private String name;
    private String brand;
    private String description;
    private double price;
    private double discount;        // percentage, e.g. 15 = 15% off
    private int stock;
    private String imageUrl;
    private boolean status = true;
    private Timestamp createdAt;
    private double avgRating;
    private int reviewCount;

    /** Selling price after discount, rounded to 2 decimals. */
    public double getFinalPrice() {
        return Math.round(price * (100 - discount)) / 100.0;
    }

    public boolean isInStock() { return stock > 0; }
    public boolean isLowStock() { return stock > 0 && stock <= LOW_STOCK_LIMIT; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public double getAvgRating() { return avgRating; }
    public void setAvgRating(double avgRating) { this.avgRating = avgRating; }
    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    @Override
    public String toString() {
        return String.format("#%d %s (%s) - Rs.%.2f, stock %d", productId, name, brand, getFinalPrice(), stock);
    }
}
