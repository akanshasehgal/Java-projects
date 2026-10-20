package com.shopsphere.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PLACED = "PLACED";
    public static final String CONFIRMED = "CONFIRMED";
    public static final String SHIPPED = "SHIPPED";
    public static final String DELIVERED = "DELIVERED";
    public static final String CANCELLED = "CANCELLED";

    /** Order status flow, used for the tracking timeline. */
    public static final List<String> STATUS_FLOW = Arrays.asList(PLACED, CONFIRMED, SHIPPED, DELIVERED);
    public static final List<String> ALL_STATUSES = Arrays.asList(PLACED, CONFIRMED, SHIPPED, DELIVERED, CANCELLED);

    private int orderId;
    private int userId;
    private String customerName;
    private Integer addressId;
    private String shippingAddress;
    private double totalAmount;
    private double discount;
    private String paymentMethod;
    private String paymentStatus;
    private String orderStatus = PLACED;
    private Timestamp createdAt;
    private List<OrderItem> items = new ArrayList<>();

    public boolean isCancellable() {
        return PLACED.equals(orderStatus) || CONFIRMED.equals(orderStatus);
    }

    /** Position of the current status in STATUS_FLOW (-1 when cancelled). */
    public int getStatusStep() {
        return STATUS_FLOW.indexOf(orderStatus);
    }

    public int getItemCount() {
        int count = 0;
        for (OrderItem i : items) count += i.getQuantity();
        return count;
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public Integer getAddressId() { return addressId; }
    public void setAddressId(Integer addressId) { this.addressId = addressId; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}
