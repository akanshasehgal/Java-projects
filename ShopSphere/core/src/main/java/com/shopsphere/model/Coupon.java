package com.shopsphere.model;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String PERCENT = "PERCENT";
    public static final String FLAT = "FLAT";

    private int couponId;
    private String code;
    private String discountType = PERCENT;
    private double discountValue;
    private double minimumOrder;
    private double maximumDiscount;     // 0 = no cap
    private Date expiryDate;
    private boolean status = true;

    public boolean isExpired() {
        return expiryDate != null && expiryDate.toLocalDate().isBefore(LocalDate.now());
    }

    /** Discount this coupon gives on the given subtotal (0 if it does not apply). */
    public double calculateDiscount(double subtotal) {
        if (!status || isExpired() || subtotal < minimumOrder) return 0;
        double d;
        if (PERCENT.equals(discountType)) {
            d = subtotal * discountValue / 100;
            if (maximumDiscount > 0) d = Math.min(d, maximumDiscount);
        } else {
            d = Math.min(discountValue, subtotal);
        }
        return Math.round(d * 100) / 100.0;
    }

    /** Short human-readable description, e.g. "10% off (max Rs.500) on orders above Rs.999". */
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        if (PERCENT.equals(discountType)) {
            sb.append(trim(discountValue)).append("% off");
            if (maximumDiscount > 0) sb.append(" (max Rs.").append(trim(maximumDiscount)).append(")");
        } else {
            sb.append("Flat Rs.").append(trim(discountValue)).append(" off");
        }
        if (minimumOrder > 0) sb.append(" on orders above Rs.").append(trim(minimumOrder));
        return sb.toString();
    }

    private static String trim(double v) {
        return v == Math.floor(v) ? String.valueOf((long) v) : String.valueOf(v);
    }

    public int getCouponId() { return couponId; }
    public void setCouponId(int couponId) { this.couponId = couponId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }
    public double getDiscountValue() { return discountValue; }
    public void setDiscountValue(double discountValue) { this.discountValue = discountValue; }
    public double getMinimumOrder() { return minimumOrder; }
    public void setMinimumOrder(double minimumOrder) { this.minimumOrder = minimumOrder; }
    public double getMaximumDiscount() { return maximumDiscount; }
    public void setMaximumDiscount(double maximumDiscount) { this.maximumDiscount = maximumDiscount; }
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}
