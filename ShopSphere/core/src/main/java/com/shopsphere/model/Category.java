package com.shopsphere.model;

import java.io.Serializable;

public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    private int categoryId;
    private String name;
    private String description;
    private boolean status = true;
    private int productCount;

    public Category() {
    }

    public Category(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
    public int getProductCount() { return productCount; }
    public void setProductCount(int productCount) { this.productCount = productCount; }

    /** Shown in Swing combo boxes. */
    @Override
    public String toString() {
        return name;
    }
}
