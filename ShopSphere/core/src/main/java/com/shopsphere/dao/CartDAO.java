package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopsphere.model.CartItem;
import com.shopsphere.model.Product;

public class CartDAO {

    private static final String SELECT =
            "SELECT ct.cart_id, ct.user_id, ct.quantity, p.*, c.name AS category_name, "
                    + "0 AS avg_rating, 0 AS review_count "
                    + "FROM cart ct JOIN products p ON ct.product_id = p.product_id "
                    + "LEFT JOIN categories c ON p.category_id = c.category_id ";

    public List<CartItem> findByUser(int userId) {
        List<CartItem> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT + "WHERE ct.user_id = ? ORDER BY ct.cart_id")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setCartId(rs.getInt("cart_id"));
                    item.setUserId(rs.getInt("user_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    Product p = ProductDAO.map(rs);
                    item.setProduct(p);
                    list.add(item);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Could not load cart", e);
        }
        return list;
    }

    public int getQuantity(int userId, int productId) {
        String sql = "SELECT quantity FROM cart WHERE user_id = ? AND product_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new DAOException("Could not read cart", e);
        }
    }

    /** Adds the product or increases its quantity if it is already in the cart. */
    public void addOrIncrement(int userId, int productId, int quantity) {
        try (Connection con = DBConnection.getConnection()) {
            int updated;
            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE cart SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?")) {
                ps.setInt(1, quantity);
                ps.setInt(2, userId);
                ps.setInt(3, productId);
                updated = ps.executeUpdate();
            }
            if (updated == 0) {
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?)")) {
                    ps.setInt(1, userId);
                    ps.setInt(2, productId);
                    ps.setInt(3, quantity);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Could not add to cart", e);
        }
    }

    public void updateQuantity(int userId, int productId, int quantity) {
        execute("UPDATE cart SET quantity = ? WHERE user_id = ? AND product_id = ?", quantity, userId, productId);
    }

    public void remove(int userId, int productId) {
        execute("DELETE FROM cart WHERE user_id = ? AND product_id = ?", userId, productId);
    }

    /** Total number of units in the cart (shown on the navbar badge). */
    public int countItems(int userId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COALESCE(SUM(quantity), 0) FROM cart WHERE user_id = ?")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DAOException("Could not count cart items", e);
        }
    }

    /** Empties the cart inside the checkout transaction. */
    public void clear(Connection con, int userId) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM cart WHERE user_id = ?")) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    private void execute(String sql, Object... params) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Could not update cart", e);
        }
    }
}
