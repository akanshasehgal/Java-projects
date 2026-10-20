package com.shopsphere.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shopsphere.model.Product;

public class ProductDAO {

    /** Products joined with their category name and review statistics. */
    private static final String BASE_SELECT =
            "SELECT p.*, c.name AS category_name, "
                    + "COALESCE(r.avg_rating, 0) AS avg_rating, COALESCE(r.review_count, 0) AS review_count "
                    + "FROM products p "
                    + "LEFT JOIN categories c ON p.category_id = c.category_id "
                    + "LEFT JOIN (SELECT product_id, AVG(rating) AS avg_rating, COUNT(*) AS review_count "
                    + "           FROM reviews GROUP BY product_id) r ON r.product_id = p.product_id ";

    private static final String FINAL_PRICE = "(p.price * (100 - p.discount) / 100)";

    private static final Map<String, String> SORTS = new HashMap<>();

    static {
        SORTS.put("price_asc", FINAL_PRICE + " ASC");
        SORTS.put("price_desc", FINAL_PRICE + " DESC");
        SORTS.put("rating", "avg_rating DESC, review_count DESC");
        SORTS.put("discount", "p.discount DESC");
        SORTS.put("name", "p.name ASC");
        SORTS.put("newest", "p.created_at DESC, p.product_id DESC");
    }

    /**
     * Search, filter and sort products. Any filter may be null.
     * User input is only ever passed as PreparedStatement parameters.
     */
    public List<Product> search(String keyword, Integer categoryId, Double minPrice, Double maxPrice,
                                String sort, boolean activeOnly) {
        StringBuilder sql = new StringBuilder(BASE_SELECT).append("WHERE 1 = 1 ");
        List<Object> params = new ArrayList<>();

        if (activeOnly) sql.append("AND p.status = TRUE ");
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (p.name LIKE ? OR p.brand LIKE ? OR p.description LIKE ? OR c.name LIKE ?) ");
            String like = "%" + keyword.trim() + "%";
            for (int i = 0; i < 4; i++) params.add(like);
        }
        if (categoryId != null) {
            sql.append("AND p.category_id = ? ");
            params.add(categoryId);
        }
        if (minPrice != null) {
            sql.append("AND ").append(FINAL_PRICE).append(" >= ? ");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append("AND ").append(FINAL_PRICE).append(" <= ? ");
            params.add(maxPrice);
        }
        String order = SORTS.containsKey(sort) ? SORTS.get(sort) : SORTS.get("newest");
        sql.append("ORDER BY ").append(order);
        return query(sql.toString(), params.toArray());
    }

    public List<Product> findAll() {
        return query(BASE_SELECT + "ORDER BY p.product_id");
    }

    public Product findById(int productId) {
        List<Product> list = query(BASE_SELECT + "WHERE p.product_id = ?", productId);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Product> findLatest(int limit) {
        return query(BASE_SELECT + "WHERE p.status = TRUE ORDER BY p.created_at DESC, p.product_id DESC LIMIT ?", limit);
    }

    public List<Product> findTopDeals(int limit) {
        return query(BASE_SELECT + "WHERE p.status = TRUE AND p.stock > 0 ORDER BY p.discount DESC LIMIT ?", limit);
    }

    public List<Product> findLowStock(int threshold) {
        return query(BASE_SELECT + "WHERE p.status = TRUE AND p.stock <= ? ORDER BY p.stock, p.name", threshold);
    }

    /** Loads products for the given ids, keeping the order of the ids (used for "recently viewed"). */
    public List<Product> findByIds(List<Integer> ids) {
        List<Product> result = new ArrayList<>();
        if (ids.isEmpty()) return result;
        StringBuilder in = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) in.append(i == 0 ? "?" : ", ?");
        List<Product> found = query(BASE_SELECT + "WHERE p.status = TRUE AND p.product_id IN (" + in + ")",
                ids.toArray());
        for (Integer id : ids) {
            for (Product p : found) {
                if (p.getProductId() == id) result.add(p);
            }
        }
        return result;
    }

    public int countActive() {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM products WHERE status = TRUE");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new DAOException("Could not count products", e);
        }
    }

    public int insert(Product p) {
        String sql = "INSERT INTO products (category_id, name, brand, description, price, discount, stock, image_url, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(ps, p);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            throw new DAOException("Could not add product", e);
        }
    }

    public void update(Product p) {
        String sql = "UPDATE products SET category_id = ?, name = ?, brand = ?, description = ?, price = ?, "
                + "discount = ?, stock = ?, image_url = ?, status = ? WHERE product_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            bind(ps, p);
            ps.setInt(10, p.getProductId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Could not update product", e);
        }
    }

    /** Products are never hard-deleted because old orders refer to them. */
    public void setStatus(int productId, boolean active) {
        executeUpdate("UPDATE products SET status = ? WHERE product_id = ?", active, productId);
    }

    public boolean updateStock(int productId, int stock) {
        return executeUpdate("UPDATE products SET stock = ? WHERE product_id = ?", stock, productId) > 0;
    }

    /** Decreases stock only if enough is available. Runs inside the caller's transaction. */
    public boolean decreaseStock(Connection con, int productId, int quantity) throws SQLException {
        String sql = "UPDATE products SET stock = stock - ? WHERE product_id = ? AND stock >= ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            return ps.executeUpdate() == 1;
        }
    }

    public void increaseStock(Connection con, int productId, int quantity) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("UPDATE products SET stock = stock + ? WHERE product_id = ?")) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    private void bind(PreparedStatement ps, Product p) throws SQLException {
        if (p.getCategoryId() > 0) ps.setInt(1, p.getCategoryId());
        else ps.setNull(1, java.sql.Types.INTEGER);
        ps.setString(2, p.getName());
        ps.setString(3, p.getBrand());
        ps.setString(4, p.getDescription());
        ps.setDouble(5, p.getPrice());
        ps.setDouble(6, p.getDiscount());
        ps.setInt(7, p.getStock());
        ps.setString(8, p.getImageUrl());
        ps.setBoolean(9, p.isStatus());
    }

    private int executeUpdate(String sql, Object... params) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Could not update product", e);
        }
    }

    private List<Product> query(String sql, Object... params) {
        List<Product> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Could not load products", e);
        }
        return list;
    }

    static Product map(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductId(rs.getInt("product_id"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setCategoryName(rs.getString("category_name"));
        p.setName(rs.getString("name"));
        p.setBrand(rs.getString("brand"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getDouble("price"));
        p.setDiscount(rs.getDouble("discount"));
        p.setStock(rs.getInt("stock"));
        p.setImageUrl(rs.getString("image_url"));
        p.setStatus(rs.getBoolean("status"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setAvgRating(Math.round(rs.getDouble("avg_rating") * 10) / 10.0);
        p.setReviewCount(rs.getInt("review_count"));
        return p;
    }
}
