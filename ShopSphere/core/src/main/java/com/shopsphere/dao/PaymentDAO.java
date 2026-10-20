package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.shopsphere.model.Payment;

public class PaymentDAO {

    public void insert(Connection con, Payment p) throws SQLException {
        String sql = "INSERT INTO payments (order_id, payment_method, transaction_reference, amount, payment_status) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getOrderId());
            ps.setString(2, p.getPaymentMethod());
            ps.setString(3, p.getTransactionReference());
            ps.setDouble(4, p.getAmount());
            ps.setString(5, p.getPaymentStatus());
            ps.executeUpdate();
        }
    }

    public Payment findByOrder(int orderId) {
        String sql = "SELECT * FROM payments WHERE order_id = ? ORDER BY payment_id DESC LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Payment p = new Payment();
                p.setPaymentId(rs.getInt("payment_id"));
                p.setOrderId(rs.getInt("order_id"));
                p.setPaymentMethod(rs.getString("payment_method"));
                p.setTransactionReference(rs.getString("transaction_reference"));
                p.setAmount(rs.getDouble("amount"));
                p.setPaymentStatus(rs.getString("payment_status"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                return p;
            }
        } catch (SQLException e) {
            throw new DAOException("Could not load payment", e);
        }
    }

    public void updateStatusByOrder(Connection con, int orderId, String status) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("UPDATE payments SET payment_status = ? WHERE order_id = ?")) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }
}
