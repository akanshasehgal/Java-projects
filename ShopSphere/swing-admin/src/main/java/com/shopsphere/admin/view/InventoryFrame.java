package com.shopsphere.admin.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;

/** Low-stock view with quick restocking. */
public class InventoryFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final ProductService productService = new ProductService();
    private final DefaultTableModel model = Ui.readOnlyModel("ID", "Product", "Brand", "Category", "Stock");
    private final JTable table = Ui.table(model);
    private final JSpinner thresholdSpinner = new JSpinner(new SpinnerNumberModel(Product.LOW_STOCK_LIMIT, 0, 1000, 1));
    private final JSpinner newStockSpinner = new JSpinner(new SpinnerNumberModel(25, 0, 100000, 5));

    public InventoryFrame() {
        setTitle("ShopSphere Admin - Inventory");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        top.add(Ui.title("Low stock"));
        top.add(new JLabel("Show products with stock at most"));
        top.add(thresholdSpinner);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton update = Ui.primaryButton("Set stock");
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(new JLabel("New stock for selected product:"));
        bottom.add(newStockSpinner);
        bottom.add(update);
        add(bottom, BorderLayout.SOUTH);

        thresholdSpinner.addChangeListener(e -> load());
        update.addActionListener(e -> updateStock());

        setSize(720, 420);
        setLocationRelativeTo(null);
        load();
    }

    private void load() {
        try {
            model.setRowCount(0);
            for (Product p : productService.getLowStock((Integer) thresholdSpinner.getValue())) {
                model.addRow(new Object[]{p.getProductId(), p.getName(), p.getBrand(), p.getCategoryName(), p.getStock()});
            }
        } catch (RuntimeException e) {
            Ui.error(this, e);
        }
    }

    private void updateStock() {
        int row = table.getSelectedRow();
        if (row < 0) {
            Ui.info(this, "Select a product first.");
            return;
        }
        int id = (Integer) model.getValueAt(table.convertRowIndexToModel(row), 0);
        try {
            productService.updateStock(id, (Integer) newStockSpinner.getValue());
            load();
        } catch (RuntimeException e) {
            Ui.error(this, e);
        }
    }
}
