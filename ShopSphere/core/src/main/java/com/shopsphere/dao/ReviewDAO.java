package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopsphere.model.Review;

public class ReviewDAO {

    public List<Review> findByProduct(int productId) {
        String sql = "SELECT r.*, u.name AS user_name FROM reviews r JOIN users u ON r.user_id = u.user_id "
                + "WHERE r.product_id = ? ORDER BY r.created_at DESC";
        List<Review> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review r = new Review();
                    r.setReviewId(rs.getInt("review_id"));
                    r.setUserId(rs.getInt("user_id"));
                    r.setUserName(rs.getString("user_name"));
                    r.setProductId(rs.getInt("product_id"));
                    r.setRating(rs.getInt("rating"));
                    r.setReviewText(rs.getString("review_text"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(r);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Could not load reviews", e);
        }
        return list;
    }

    /** One review per user per product: updates the existing review or inserts a new one. */
    public void save(Review r) {
        try (Connection con = DBConnection.getConnection()) {
            int updated;
            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE reviews SET rating = ?, review_text = ?, created_at = CURRENT_TIMESTAMP "
                            + "WHERE user_id = ? AND product_id = ?")) {
                ps.setInt(1, r.getRating());
                ps.setString(2, r.getReviewText());
                ps.setInt(3, r.getUserId());
                ps.setInt(4, r.getProductId());
                updated = ps.executeUpdate();
            }
            if (updated == 0) {
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO reviews (user_id, product_id, rating, review_text) VALUES (?, ?, ?, ?)")) {
                    ps.setInt(1, r.getUserId());
                    ps.setInt(2, r.getProductId());
                    ps.setInt(3, r.getRating());
                    ps.setString(4, r.getReviewText());
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Could not save review", e);
        }
    }
}
