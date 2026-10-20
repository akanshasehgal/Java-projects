package com.shopsphere.service;

import java.util.List;

import com.shopsphere.dao.CouponDAO;
import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.Coupon;
import com.shopsphere.model.DashboardStats;
import com.shopsphere.model.Order;
import com.shopsphere.model.Product;
import com.shopsphere.model.User;

/** Dashboard numbers, customer management and coupons. */
public class AdminService {

    private final ProductDAO productDAO = new ProductDAO();
    private final UserDAO userDAO = new UserDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final CouponDAO couponDAO = new CouponDAO();

    public DashboardStats getStats() {
        DashboardStats s = new DashboardStats();
        s.setTotalProducts(productDAO.countActive());
        s.setTotalCustomers(userDAO.countByRole(User.ROLE_CUSTOMER));
        s.setTotalOrders(orderDAO.count(null));
        s.setPendingOrders(orderDAO.count(Order.PLACED) + orderDAO.count(Order.CONFIRMED));
        s.setLowStockCount(productDAO.findLowStock(Product.LOW_STOCK_LIMIT).size());
        s.setTotalSales(orderDAO.totalSales());
        return s;
    }

    public List<User> getUsers() {
        return userDAO.findAll();
    }

    public void setUserBlocked(int userId, boolean blocked, int currentAdminId) {
        if (userId == currentAdminId) throw new ServiceException("You cannot block your own account.");
        userDAO.updateStatus(userId, blocked ? User.STATUS_BLOCKED : User.STATUS_ACTIVE);
    }

    public List<Coupon> getCoupons() {
        return couponDAO.findAll();
    }

    public void addCoupon(Coupon c) {
        String code = AuthService.trim(c.getCode()).toUpperCase();
        if (!code.matches("[A-Z0-9]{3,20}")) throw new ServiceException("Code must be 3-20 letters/digits.");
        if (couponDAO.findByCode(code) != null) throw new ServiceException("Coupon " + code + " already exists.");
        if (c.getDiscountValue() <= 0) throw new ServiceException("Discount value must be greater than 0.");
        if (Coupon.PERCENT.equals(c.getDiscountType()) && c.getDiscountValue() > 90)
            throw new ServiceException("Percentage discount cannot exceed 90%.");
        if (c.getMinimumOrder() < 0 || c.getMaximumDiscount() < 0) throw new ServiceException("Amounts cannot be negative.");
        c.setCode(code);
        couponDAO.insert(c);
    }

    public void setCouponActive(int couponId, boolean active) {
        couponDAO.setStatus(couponId, active);
    }
}
