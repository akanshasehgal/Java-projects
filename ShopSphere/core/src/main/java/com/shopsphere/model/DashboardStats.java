package com.shopsphere.model;

import java.io.Serializable;

/** Numbers shown on the admin dashboard (web and Swing). */
public class DashboardStats implements Serializable {

    private static final long serialVersionUID = 1L;

    private int totalProducts;
    private int totalCustomers;
    private int totalOrders;
    private int pendingOrders;
    private int lowStockCount;
    private double totalSales;

    public int getTotalProducts() { return totalProducts; }
    public void setTotalProducts(int totalProducts) { this.totalProducts = totalProducts; }
    public int getTotalCustomers() { return totalCustomers; }
    public void setTotalCustomers(int totalCustomers) { this.totalCustomers = totalCustomers; }
    public int getTotalOrders() { return totalOrders; }
    public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }
    public int getPendingOrders() { return pendingOrders; }
    public void setPendingOrders(int pendingOrders) { this.pendingOrders = pendingOrders; }
    public int getLowStockCount() { return lowStockCount; }
    public void setLowStockCount(int lowStockCount) { this.lowStockCount = lowStockCount; }
    public double getTotalSales() { return totalSales; }
    public void setTotalSales(double totalSales) { this.totalSales = totalSales; }
}
