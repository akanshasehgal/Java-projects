package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;

public class OrderDAO {

    private static final String SELECT =
            "SELECT o.*, u.name AS customer_name FROM orders o JOIN users u ON o.user_id = u.user_id ";

    /** Inserts the order inside the caller's transaction and returns the new order id. */
    public int insert(Connection con, Order o) throws SQLException {
        String sql = "INSERT INTO orders (user_id, address_id, shipping_address, total_amount, discount, "
                + "payment_method, payment_status, order_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, o.getUserId());
            if (o.getAddressId() != null) ps.setInt(2, o.getAddressId());
            else ps.setNull(2, Types.INTEGER);
            ps.setString(3, o.getShippingAddress());
            ps.setDouble(4, o.getTotalAmount());
            ps.setDouble(5, o.getDiscount());
            ps.setString(6, o.getPaymentMethod());
            ps.setString(7, o.getPaymentStatus());
            ps.setString(8, o.getOrderStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    public void insertItem(Connection con, OrderItem item) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getPrice());
            ps.executeUpdate();
        }
    }

    public List<Order> findByUser(int userId) {
        return query(SELECT + "WHERE o.user_id = ? ORDER BY o.created_at DESC, o.order_id DESC", true, userId);
    }

    public Order findById(int orderId) {
        List<Order> list = query(SELECT + "WHERE o.order_id = ?", true, orderId);
        return list.isEmpty() ? null : list.get(0);
    }

    /** All orders, optionally filtered by status (admin). */
    public List<Order> findAll(String status) {
        if (status == null || status.isEmpty()) {
            return query(SELECT + "ORDER BY o.created_at DESC, o.order_id DESC", true);
        }
        return query(SELECT + "WHERE o.order_status = ? ORDER BY o.created_at DESC, o.order_id DESC", true, status);
    }

    public List<Order> findRecent(int limit) {
        return query(SELECT + "ORDER BY o.created_at DESC, o.order_id DESC LIMIT ?", false, limit);
    }

    public void updateStatus(Connection con, int orderId, String status) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("UPDATE orders SET order_status = ? WHERE order_id = ?")) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }

    public void updatePaymentStatus(Connection con, int orderId, String status) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("UPDATE orders SET payment_status = ? WHERE order_id = ?")) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }

    public int count(String status) {
        String sql = status == null ? "SELECT COUNT(*) FROM orders" : "SELECT COUNT(*) FROM orders WHERE order_status = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (status != null) ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DAOException("Could not count orders", e);
        }
    }

    /** Sum of all orders that were not cancelled. */
    public double totalSales() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM orders WHERE order_status <> 'CANCELLED'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getDouble(1);
        } catch (SQLException e) {
            throw new DAOException("Could not calculate sales", e);
        }
    }

    private List<Order> query(String sql, boolean withItems, Object... params) {
        List<Order> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            if (withItems) {
                for (Order o : list) o.setItems(loadItems(con, o.getOrderId()));
            }
        } catch (SQLException e) {
            throw new DAOException("Could not load orders", e);
        }
        return list;
    }

    private List<OrderItem> loadItems(Connection con, int orderId) throws SQLException {
        String sql = "SELECT oi.*, p.name AS product_name FROM order_items oi "
                + "JOIN products p ON oi.product_id = p.product_id WHERE oi.order_id = ? ORDER BY oi.order_item_id";
        List<OrderItem> items = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem i = new OrderItem();
                    i.setOrderItemId(rs.getInt("order_item_id"));
                    i.setOrderId(rs.getInt("order_id"));
                    i.setProductId(rs.getInt("product_id"));
                    i.setProductName(rs.getString("product_name"));
                    i.setQuantity(rs.getInt("quantity"));
                    i.setPrice(rs.getDouble("price"));
                    items.add(i);
                }
            }
        }
        return items;
    }

    private Order map(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setOrderId(rs.getInt("order_id"));
        o.setUserId(rs.getInt("user_id"));
        o.setCustomerName(rs.getString("customer_name"));
        int addressId = rs.getInt("address_id");
        o.setAddressId(rs.wasNull() ? null : addressId);
        o.setShippingAddress(rs.getString("shipping_address"));
        o.setTotalAmount(rs.getDouble("total_amount"));
        o.setDiscount(rs.getDouble("discount"));
        o.setPaymentMethod(rs.getString("payment_method"));
        o.setPaymentStatus(rs.getString("payment_status"));
        o.setOrderStatus(rs.getString("order_status"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        return o;
    }
}
