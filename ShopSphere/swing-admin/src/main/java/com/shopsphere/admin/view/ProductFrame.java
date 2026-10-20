package com.shopsphere.admin.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.shopsphere.model.Category;

/** View: product list (JTable) and product form. ProductController handles all actions. */
public class ProductFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public final DefaultTableModel tableModel = Ui.readOnlyModel(
            "ID", "Name", "Brand", "Category", "MRP", "Disc %", "Price", "Stock", "Active");
    public final JTable table = Ui.table(tableModel);

    public final JTextField searchField = new JTextField(18);
    public final JButton searchButton = new JButton("Search");

    public final JLabel idLabel = new JLabel("New product");
    public final JTextField nameField = new JTextField(22);
    public final JTextField brandField = new JTextField(22);
    public final JComboBox<Category> categoryBox = new JComboBox<>();
    public final JTextField priceField = new JTextField(10);
    public final JTextField discountField = new JTextField("0", 10);
    public final JTextField stockField = new JTextField("0", 10);
    public final JTextArea descriptionArea = new JTextArea(4, 22);
    public final JCheckBox activeBox = new JCheckBox("Visible in store", true);

    public final JButton newButton = new JButton("New");
    public final JButton saveButton = Ui.primaryButton("Save");
    public final JButton toggleButton = new JButton("Hide / Show");

    public ProductFrame() {
        setTitle("ShopSphere Admin - Products");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel left = new JPanel(new BorderLayout(6, 6));
        left.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        search.add(Ui.title("Products"));
        search.add(searchField);
        search.add(searchButton);
        left.add(search, BorderLayout.NORTH);
        left.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 5, 10, 10), BorderFactory.createTitledBorder("Product details")));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;
        row = addRow(form, c, row, "ID", idLabel);
        row = addRow(form, c, row, "Name", nameField);
        row = addRow(form, c, row, "Brand", brandField);
        row = addRow(form, c, row, "Category", categoryBox);
        row = addRow(form, c, row, "MRP (Rs.)", priceField);
        row = addRow(form, c, row, "Discount %", discountField);
        row = addRow(form, c, row, "Stock", stockField);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        row = addRow(form, c, row, "Description", new JScrollPane(descriptionArea));
        row = addRow(form, c, row, "", activeBox);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(newButton);
        buttons.add(saveButton);
        buttons.add(toggleButton);
        c.gridx = 1; c.gridy = row;
        form.add(buttons, c);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, form);
        split.setResizeWeight(0.65);
        add(split);

        setSize(1150, 560);
        setLocationRelativeTo(null);
    }

    private int addRow(JPanel panel, GridBagConstraints c, int row, String label, JComponent field) {
        c.gridx = 0; c.gridy = row; c.weightx = 0;
        panel.add(new JLabel(label), c);
        c.gridx = 1; c.weightx = 1;
        panel.add(field, c);
        return row + 1;
    }
}
