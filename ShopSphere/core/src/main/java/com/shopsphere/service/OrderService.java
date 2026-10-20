package com.shopsphere.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.shopsphere.dao.DAOException;
import com.shopsphere.dao.DBConnection;
import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.PaymentDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;
import com.shopsphere.model.Payment;

/** Order history, tracking and status changes. */
public class OrderService {

    private final OrderDAO orderDAO = new OrderDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    public List<Order> getOrdersForUser(int userId) {
        return orderDAO.findByUser(userId);
    }

    /** Returns the order only if it belongs to the user (prevents viewing other people's orders). */
    public Order getOrderForUser(int orderId, int userId) {
        Order o = orderDAO.findById(orderId);
        return o != null && o.getUserId() == userId ? o : null;
    }

    public Order getOrder(int orderId) { return orderDAO.findById(orderId); }
    public Payment getPayment(int orderId) { return paymentDAO.findByOrder(orderId); }
    public List<Order> getAllOrders(String status) { return orderDAO.findAll(status); }
    public List<Order> getRecentOrders(int limit) { return orderDAO.findRecent(limit); }

    public void cancelByCustomer(int orderId, int userId) {
        Order o = getOrderForUser(orderId, userId);
        if (o == null) throw new ServiceException("Order not found.");
        if (!o.isCancellable()) throw new ServiceException("This order can no longer be cancelled.");
        changeStatus(o, Order.CANCELLED);
    }

    /** Admin status update. Cancelling puts the stock back; delivering a COD order marks it paid. */
    public void updateStatus(int orderId, String newStatus) {
        if (!Order.ALL_STATUSES.contains(newStatus)) throw new ServiceException("Unknown order status.");
        Order o = orderDAO.findById(orderId);
        if (o == null) throw new ServiceException("Order not found.");
        if (Order.CANCELLED.equals(o.getOrderStatus()))
            throw new ServiceException("A cancelled order cannot be changed.");
        changeStatus(o, newStatus);
    }

    private void changeStatus(Order o, String newStatus) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            orderDAO.updateStatus(con, o.getOrderId(), newStatus);

            if (Order.CANCELLED.equals(newStatus)) {
                for (OrderItem i : o.getItems()) productDAO.increaseStock(con, i.getProductId(), i.getQuantity());
                String payment = Payment.SUCCESS.equals(o.getPaymentStatus()) ? Payment.REFUNDED : Payment.FAILED;
                orderDAO.updatePaymentStatus(con, o.getOrderId(), payment);
                paymentDAO.updateStatusByOrder(con, o.getOrderId(), payment);
            } else if (Order.DELIVERED.equals(newStatus) && Payment.PENDING.equals(o.getPaymentStatus())) {
                orderDAO.updatePaymentStatus(con, o.getOrderId(), Payment.SUCCESS);
                paymentDAO.updateStatusByOrder(con, o.getOrderId(), Payment.SUCCESS);
            }
            con.commit();
        } catch (SQLException e) {
            CheckoutService.rollback(con);
            throw new DAOException("Could not update order", e);
        } finally {
            CheckoutService.close(con);
        }
    }
}
