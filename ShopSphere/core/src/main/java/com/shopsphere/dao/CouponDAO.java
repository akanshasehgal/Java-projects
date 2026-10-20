package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopsphere.model.Coupon;

public class CouponDAO {

    public Coupon findByCode(String code) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM coupons WHERE UPPER(code) = UPPER(?)")) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new DAOException("Could not load coupon", e);
        }
    }

    public List<Coupon> findAll() {
        List<Coupon> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM coupons ORDER BY coupon_id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Could not load coupons", e);
        }
        return list;
    }

    /** Active, unexpired coupons shown to customers at checkout. */
    public List<Coupon> findAvailable() {
        List<Coupon> list = new ArrayList<>();
        String sql = "SELECT * FROM coupons WHERE status = TRUE AND (expiry_date IS NULL OR expiry_date >= CURDATE()) "
                + "ORDER BY minimum_order";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Could not load coupons", e);
        }
        return list;
    }

    public void insert(Coupon c) {
        String sql = "INSERT INTO coupons (code, discount_type, discount_value, minimum_order, maximum_discount, "
                + "expiry_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCode().toUpperCase());
            ps.setString(2, c.getDiscountType());
            ps.setDouble(3, c.getDiscountValue());
            ps.setDouble(4, c.getMinimumOrder());
            ps.setDouble(5, c.getMaximumDiscount());
            ps.setDate(6, c.getExpiryDate());
            ps.setBoolean(7, c.isStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Could not save coupon", e);
        }
    }

    public void setStatus(int couponId, boolean active) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE coupons SET status = ? WHERE coupon_id = ?")) {
            ps.setBoolean(1, active);
            ps.setInt(2, couponId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Could not update coupon", e);
        }
    }

    public boolean hasUsed(int userId, int couponId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT 1 FROM coupon_usage WHERE user_id = ? AND coupon_id = ?")) {
            ps.setInt(1, userId);
            ps.setInt(2, couponId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DAOException("Could not check coupon usage", e);
        }
    }

    public void recordUsage(Connection con, int couponId, int userId, int orderId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO coupon_usage (coupon_id, user_id, order_id) VALUES (?, ?, ?)")) {
            ps.setInt(1, couponId);
            ps.setInt(2, userId);
            ps.setInt(3, orderId);
            ps.executeUpdate();
        }
    }

    private Coupon map(ResultSet rs) throws SQLException {
        Coupon c = new Coupon();
        c.setCouponId(rs.getInt("coupon_id"));
        c.setCode(rs.getString("code"));
        c.setDiscountType(rs.getString("discount_type"));
        c.setDiscountValue(rs.getDouble("discount_value"));
        c.setMinimumOrder(rs.getDouble("minimum_order"));
        c.setMaximumDiscount(rs.getDouble("maximum_discount"));
        c.setExpiryDate(rs.getDate("expiry_date"));
        c.setStatus(rs.getBoolean("status"));
        return c;
    }
}
