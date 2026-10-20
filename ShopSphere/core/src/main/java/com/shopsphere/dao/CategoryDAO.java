package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopsphere.model.Category;

public class CategoryDAO {

    private static final String SELECT =
            "SELECT c.*, (SELECT COUNT(*) FROM products p WHERE p.category_id = c.category_id AND p.status = TRUE) "
                    + "AS product_count FROM categories c ";

    public List<Category> findAll() {
        return query(SELECT + "ORDER BY c.name");
    }

    public List<Category> findActive() {
        return query(SELECT + "WHERE c.status = TRUE ORDER BY c.name");
    }

    public Category findById(int categoryId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT + "WHERE c.category_id = ?")) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new DAOException("Could not load category", e);
        }
    }

    public void insert(Category c) {
        update("INSERT INTO categories (name, description, status) VALUES (?, ?, ?)",
                c.getName(), c.getDescription(), c.isStatus());
    }

    public void update(Category c) {
        update("UPDATE categories SET name = ?, description = ?, status = ? WHERE category_id = ?",
                c.getName(), c.getDescription(), c.isStatus(), c.getCategoryId());
    }

    public void setStatus(int categoryId, boolean active) {
        update("UPDATE categories SET status = ? WHERE category_id = ?", active, categoryId);
    }

    /** Products in a deleted category become uncategorised (ON DELETE SET NULL). */
    public void delete(int categoryId) {
        update("DELETE FROM categories WHERE category_id = ?", categoryId);
    }

    private List<Category> query(String sql) {
        List<Category> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new DAOException("Could not load categories", e);
        }
        return list;
    }

    private void update(String sql, Object... params) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Could not save category", e);
        }
    }

    private Category map(ResultSet rs) throws SQLException {
        Category c = new Category();
        c.setCategoryId(rs.getInt("category_id"));
        c.setName(rs.getString("name"));
        c.setDescription(rs.getString("description"));
        c.setStatus(rs.getBoolean("status"));
        c.setProductCount(rs.getInt("product_count"));
        return c;
    }
}
