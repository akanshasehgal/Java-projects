package com.shopsphere.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.shopsphere.dao.AddressDAO;
import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.CouponDAO;
import com.shopsphere.dao.DAOException;
import com.shopsphere.dao.DBConnection;
import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.PaymentDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Address;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.CartSummary;
import com.shopsphere.model.Coupon;
import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;
import com.shopsphere.model.Payment;

/**
 * Checkout: coupon validation, totals and order placement.
 * All amounts are calculated here on the server.
 */
public class CheckoutService {

    public static final List<String> PAYMENT_METHODS = Arrays.asList("COD", "UPI", "CARD");

    private final CartDAO cartDAO = new CartDAO();
    private final CouponDAO couponDAO = new CouponDAO();
    private final AddressDAO addressDAO = new AddressDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    /** Builds the cart totals; an invalid coupon is reported in couponError instead of throwing. */
    public CartSummary summarize(int userId, String couponCode) {
        CartSummary s = new CartSummary();
        List<CartItem> items = cartDAO.findByUser(userId);
        s.setItems(items);

        double subtotal = 0;
        for (CartItem i : items) subtotal += i.getSubtotal();
        subtotal = Math.round(subtotal * 100) / 100.0;
        s.setSubtotal(subtotal);

        if (couponCode != null && !couponCode.trim().isEmpty() && !items.isEmpty()) {
            try {
                Coupon c = validateCoupon(couponCode, subtotal, userId);
                s.setCoupon(c);
                s.setDiscount(c.calculateDiscount(subtotal));
            } catch (ServiceException e) {
                s.setCouponError(e.getMessage());
            }
        }
        boolean freeShipping = items.isEmpty() || subtotal - s.getDiscount() >= CartSummary.FREE_SHIPPING_ABOVE;
        s.setShipping(freeShipping ? 0 : CartSummary.SHIPPING_CHARGE);
        return s;
    }

    public Coupon validateCoupon(String code, double subtotal, int userId) {
        Coupon c = couponDAO.findByCode(code.trim());
        if (c == null || !c.isStatus()) throw new ServiceException("Coupon code is not valid.");
        if (c.isExpired()) throw new ServiceException("This coupon has expired.");
        if (subtotal < c.getMinimumOrder())
            throw new ServiceException("Add items worth Rs." + Math.round(c.getMinimumOrder() - subtotal)
                    + " more to use " + c.getCode() + ".");
        if (couponDAO.hasUsed(userId, c.getCouponId()))
            throw new ServiceException("You have already used coupon " + c.getCode() + ".");
        return c;
    }

    public List<Coupon> getAvailableCoupons() {
        return couponDAO.findAvailable();
    }

    /**
     * Places the order in a single database transaction:
     * order + items + stock reduction + payment + coupon usage + clearing the cart.
     */
    public int placeOrder(int userId, int addressId, String couponCode, String paymentMethod) {
        if (!PAYMENT_METHODS.contains(paymentMethod)) throw new ServiceException("Please choose a payment method.");
        Address address = addressDAO.findByIdForUser(addressId, userId);
        if (address == null) throw new ServiceException("Please select a delivery address.");

        CartSummary s = summarize(userId, couponCode);
        if (s.isEmpty()) throw new ServiceException("Your cart is empty.");
        if (s.getCouponError() != null) throw new ServiceException(s.getCouponError());
        for (CartItem i : s.getItems()) {
            if (!i.getProduct().isStatus()) throw new ServiceException(i.getProduct().getName() + " is no longer available.");
            if (i.getQuantity() > i.getProduct().getStock())
                throw new ServiceException("Only " + i.getProduct().getStock() + " units of "
                        + i.getProduct().getName() + " are left. Please update your cart.");
        }

        boolean paidOnline = !"COD".equals(paymentMethod);
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            Order o = new Order();
            o.setUserId(userId);
            o.setAddressId(address.getAddressId());
            o.setShippingAddress(address.getFullAddress());
            o.setTotalAmount(s.getTotal());
            o.setDiscount(s.getDiscount());
            o.setPaymentMethod(paymentMethod);
            o.setPaymentStatus(paidOnline ? Payment.SUCCESS : Payment.PENDING);
            o.setOrderStatus(Order.PLACED);
            int orderId = orderDAO.insert(con, o);

            for (CartItem i : s.getItems()) {
                if (!productDAO.decreaseStock(con, i.getProduct().getProductId(), i.getQuantity()))
                    throw new ServiceException(i.getProduct().getName() + " just went out of stock.");
                orderDAO.insertItem(con, new OrderItem(orderId, i.getProduct().getProductId(),
                        i.getQuantity(), i.getProduct().getFinalPrice()));
            }

            // Mock payment gateway: online payments succeed immediately, COD is collected on delivery
            Payment p = new Payment();
            p.setOrderId(orderId);
            p.setPaymentMethod(paymentMethod);
            p.setAmount(s.getTotal());
            p.setPaymentStatus(o.getPaymentStatus());
            p.setTransactionReference(paidOnline
                    ? "TXN" + System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(100, 1000)
                    : null);
            paymentDAO.insert(con, p);

            if (s.getCoupon() != null) couponDAO.recordUsage(con, s.getCoupon().getCouponId(), userId, orderId);
            cartDAO.clear(con, userId);

            con.commit();
            return orderId;
        } catch (SQLException e) {
            rollback(con);
            throw new DAOException("Could not place order", e);
        } catch (RuntimeException e) {
            rollback(con);
            throw e;
        } finally {
            close(con);
        }
    }

    static void rollback(Connection con) {
        if (con == null) return;
        try {
            con.rollback();
        } catch (SQLException ignored) {
            // nothing more we can do
        }
    }

    static void close(Connection con) {
        if (con == null) return;
        try {
            con.setAutoCommit(true);
            con.close();
        } catch (SQLException ignored) {
            // connection already closed
        }
    }
}
