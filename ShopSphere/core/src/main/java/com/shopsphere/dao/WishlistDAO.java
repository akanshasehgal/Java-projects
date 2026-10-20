package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopsphere.model.WishlistItem;

public class WishlistDAO {

    public List<WishlistItem> findByUser(int userId) {
        String sql = "SELECT w.wishlist_id, w.user_id, w.created_at AS added_at, p.*, c.name AS category_name, "
                + "0 AS avg_rating, 0 AS review_count "
                + "FROM wishlist w JOIN products p ON w.product_id = p.product_id "
                + "LEFT JOIN categories c ON p.category_id = c.category_id "
                + "WHERE w.user_id = ? ORDER BY w.created_at DESC";
        List<WishlistItem> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    WishlistItem w = new WishlistItem();
                    w.setWishlistId(rs.getInt("wishlist_id"));
                    w.setUserId(rs.getInt("user_id"));
                    w.setCreatedAt(rs.getTimestamp("added_at"));
                    w.setProduct(ProductDAO.map(rs));
                    list.add(w);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Could not load wishlist", e);
        }
        return list;
    }

    public void add(int userId, int productId) {
        execute("INSERT IGNORE INTO wishlist (user_id, product_id) VALUES (?, ?)", userId, productId);
    }

    public void remove(int userId, int productId) {
        execute("DELETE FROM wishlist WHERE user_id = ? AND product_id = ?", userId, productId);
    }

    public boolean contains(int userId, int productId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT 1 FROM wishlist WHERE user_id = ? AND product_id = ?")) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DAOException("Could not read wishlist", e);
        }
    }

    private void execute(String sql, Object... params) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Could not update wishlist", e);
        }
    }
}
