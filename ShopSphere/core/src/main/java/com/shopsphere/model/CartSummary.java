package com.shopsphere.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Totals for the cart / checkout page. Always calculated on the server,
 * never taken from the browser.
 */
public class CartSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final double FREE_SHIPPING_ABOVE = 499;
    public static final double SHIPPING_CHARGE = 49;

    private List<CartItem> items = new ArrayList<>();
    private double subtotal;
    private double discount;
    private double shipping;
    private Coupon coupon;
    private String couponError;

    public double getTotal() {
        return Math.round((subtotal - discount + shipping) * 100) / 100.0;
    }

    public int getItemCount() {
        int count = 0;
        for (CartItem i : items) count += i.getQuantity();
        return count;
    }

    public boolean isEmpty() { return items.isEmpty(); }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
    public double getShipping() { return shipping; }
    public void setShipping(double shipping) { this.shipping = shipping; }
    public Coupon getCoupon() { return coupon; }
    public void setCoupon(Coupon coupon) { this.coupon = coupon; }
    public String getCouponError() { return couponError; }
    public void setCouponError(String couponError) { this.couponError = couponError; }
}
