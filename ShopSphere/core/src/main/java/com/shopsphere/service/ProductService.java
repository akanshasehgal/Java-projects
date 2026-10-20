package com.shopsphere.service;

import java.util.List;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.dao.ReviewDAO;
import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.model.Review;

/** Catalogue rules: products, categories and reviews. */
public class ProductService {

    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();

    // ------------------------------------------------------------ customer side

    public List<Product> search(String keyword, Integer categoryId, Double minPrice, Double maxPrice, String sort) {
        return productDAO.search(keyword, categoryId, minPrice, maxPrice, sort, true);
    }

    /** Returns an active product, or null if it does not exist / was removed. */
    public Product getActiveProduct(int productId) {
        Product p = productDAO.findById(productId);
        return p != null && p.isStatus() ? p : null;
    }

    public Product getProduct(int productId) {
        return productDAO.findById(productId);
    }

    public List<Product> getLatest(int limit) { return productDAO.findLatest(limit); }
    public List<Product> getTopDeals(int limit) { return productDAO.findTopDeals(limit); }
    public List<Product> getByIds(List<Integer> ids) { return productDAO.findByIds(ids); }
    public List<Category> getActiveCategories() { return categoryDAO.findActive(); }
    public List<Review> getReviews(int productId) { return reviewDAO.findByProduct(productId); }

    public void addReview(int userId, int productId, int rating, String text) {
        if (getActiveProduct(productId) == null) throw new ServiceException("Product not found.");
        if (rating < 1 || rating > 5) throw new ServiceException("Please choose a rating from 1 to 5 stars.");
        text = AuthService.trim(text);
        if (text.length() > 1000) throw new ServiceException("Review is too long (max 1000 characters).");
        Review r = new Review();
        r.setUserId(userId);
        r.setProductId(productId);
        r.setRating(rating);
        r.setReviewText(text);
        reviewDAO.save(r);
    }

    // ------------------------------------------------------------ admin side

    public List<Product> getAllProducts() { return productDAO.findAll(); }

    public List<Product> searchAll(String keyword) {
        return productDAO.search(keyword, null, null, null, "name", false);
    }

    public List<Product> getLowStock(int threshold) { return productDAO.findLowStock(threshold); }
    public List<Category> getAllCategories() { return categoryDAO.findAll(); }

    /** Inserts a new product (id 0) or updates an existing one. */
    public void saveProduct(Product p) {
        if (AuthService.trim(p.getName()).isEmpty()) throw new ServiceException("Product name is required.");
        if (p.getPrice() <= 0) throw new ServiceException("Price must be greater than 0.");
        if (p.getDiscount() < 0 || p.getDiscount() > 90) throw new ServiceException("Discount must be between 0 and 90%.");
        if (p.getStock() < 0) throw new ServiceException("Stock cannot be negative.");
        p.setName(p.getName().trim());
        if (p.getProductId() == 0) p.setProductId(productDAO.insert(p));
        else productDAO.update(p);
    }

    public void setProductStatus(int productId, boolean active) {
        productDAO.setStatus(productId, active);
    }

    public void updateStock(int productId, int stock) {
        if (stock < 0) throw new ServiceException("Stock cannot be negative.");
        if (!productDAO.updateStock(productId, stock)) throw new ServiceException("Product not found.");
    }

    public void saveCategory(Category c) {
        if (AuthService.trim(c.getName()).isEmpty()) throw new ServiceException("Category name is required.");
        c.setName(c.getName().trim());
        if (c.getCategoryId() == 0) categoryDAO.insert(c);
        else categoryDAO.update(c);
    }

    public void setCategoryStatus(int categoryId, boolean active) {
        categoryDAO.setStatus(categoryId, active);
    }

    public void deleteCategory(int categoryId) {
        categoryDAO.delete(categoryId);
    }
}
