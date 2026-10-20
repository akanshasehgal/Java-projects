package com.shopsphere.service;

import java.util.List;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.dao.WishlistDAO;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Product;
import com.shopsphere.model.WishlistItem;

/** Shopping cart and wishlist rules. */
public class CartService {

    public static final int MAX_QUANTITY = 10;

    private final CartDAO cartDAO = new CartDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final WishlistDAO wishlistDAO = new WishlistDAO();

    public List<CartItem> getCart(int userId) {
        return cartDAO.findByUser(userId);
    }

    public int getItemCount(int userId) {
        return cartDAO.countItems(userId);
    }

    public void add(int userId, int productId, int quantity) {
        Product p = productDAO.findById(productId);
        if (p == null || !p.isStatus()) throw new ServiceException("This product is no longer available.");
        if (quantity < 1) quantity = 1;
        int newQuantity = cartDAO.getQuantity(userId, productId) + quantity;
        checkStock(p, newQuantity);
        cartDAO.addOrIncrement(userId, productId, quantity);
    }

    /** Sets a new quantity; 0 removes the item. */
    public void update(int userId, int productId, int quantity) {
        if (quantity <= 0) {
            cartDAO.remove(userId, productId);
            return;
        }
        Product p = productDAO.findById(productId);
        if (p == null) throw new ServiceException("Product not found.");
        checkStock(p, quantity);
        cartDAO.updateQuantity(userId, productId, quantity);
    }

    public void remove(int userId, int productId) {
        cartDAO.remove(userId, productId);
    }

    private void checkStock(Product p, int quantity) {
        if (!p.isInStock()) throw new ServiceException(p.getName() + " is out of stock.");
        if (quantity > p.getStock()) throw new ServiceException("Only " + p.getStock() + " units of " + p.getName() + " are left.");
        if (quantity > MAX_QUANTITY) throw new ServiceException("You can buy at most " + MAX_QUANTITY + " units of one product.");
    }

    // ------------------------------------------------------------ wishlist

    public List<WishlistItem> getWishlist(int userId) { return wishlistDAO.findByUser(userId); }
    public boolean isInWishlist(int userId, int productId) { return wishlistDAO.contains(userId, productId); }
    public void addToWishlist(int userId, int productId) { wishlistDAO.add(userId, productId); }
    public void removeFromWishlist(int userId, int productId) { wishlistDAO.remove(userId, productId); }

    public void moveToCart(int userId, int productId) {
        add(userId, productId, 1);
        wishlistDAO.remove(userId, productId);
    }
}
