package com.shopsphere.admin.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;
import com.shopsphere.service.OrderService;

/** Order management: filter by status, update status, double-click to see the items. */
public class OrderFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final OrderService orderService = new OrderService();
    private final DefaultTableModel model = Ui.readOnlyModel("Order", "Customer", "Date", "Items", "Total", "Payment", "Pay status", "Status");
    private final JTable table = Ui.table(model);
    private final JComboBox<String> filterBox = new JComboBox<>();
    private final JComboBox<String> statusBox = new JComboBox<>(Order.ALL_STATUSES.toArray(new String[0]));

    public OrderFrame() {
        setTitle("ShopSphere Admin - Orders");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        filterBox.addItem("ALL");
        for (String s : Order.ALL_STATUSES) filterBox.addItem(s);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        top.add(Ui.title("Orders"));
        top.add(new JLabel("Show:"));
        top.add(filterBox);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton items = new JButton("View items");
        JButton update = Ui.primaryButton("Update status");
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(items);
        bottom.add(new JLabel("New status:"));
        bottom.add(statusBox);
        bottom.add(update);
        add(bottom, BorderLayout.SOUTH);

        filterBox.addActionListener(e -> load());
        items.addActionListener(e -> showItems());
        update.addActionListener(e -> updateStatus());
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) showItems();
            }
        });

        setSize(980, 480);
        setLocationRelativeTo(null);
        load();
    }

    private void load() {
        try {
            String filter = (String) filterBox.getSelectedItem();
            SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy HH:mm");
            model.setRowCount(0);
            for (Order o : orderService.getAllOrders("ALL".equals(filter) ? null : filter)) {
                model.addRow(new Object[]{o.getOrderId(), o.getCustomerName(), fmt.format(o.getCreatedAt()),
                        o.getItemCount(), Ui.money(o.getTotalAmount()), o.getPaymentMethod(), o.getPaymentStatus(),
                        o.getOrderStatus()});
            }
        } catch (RuntimeException e) {
            Ui.error(this, e);
        }
    }

    private Integer selectedOrderId() {
        int row = table.getSelectedRow();
        if (row < 0) {
            Ui.info(this, "Select an order first.");
            return null;
        }
        return (Integer) model.getValueAt(table.convertRowIndexToModel(row), 0);
    }

    private void showItems() {
        Integer id = selectedOrderId();
        if (id == null) return;
        try {
            Order o = orderService.getOrder(id);
            StringBuilder sb = new StringBuilder("Order #" + id + " - " + o.getCustomerName() + "\n\n");
            for (OrderItem i : o.getItems()) {
                sb.append(i.getQuantity()).append(" x ").append(i.getProductName())
                        .append("  @ ").append(Ui.money(i.getPrice())).append('\n');
            }
            if (o.getDiscount() > 0) sb.append("\nDiscount: ").append(Ui.money(o.getDiscount()));
            sb.append("\nTotal: ").append(Ui.money(o.getTotalAmount()));
            sb.append("\n\nShip to: ").append(o.getShippingAddress());
            Ui.info(this, sb.toString());
        } catch (RuntimeException e) {
            Ui.error(this, e);
        }
    }

    private void updateStatus() {
        Integer id = selectedOrderId();
        if (id == null) return;
        try {
            orderService.updateStatus(id, (String) statusBox.getSelectedItem());
            load();
        } catch (RuntimeException e) {
            Ui.error(this, e);
        }
    }
}
