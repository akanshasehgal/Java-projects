package com.shopsphere.admin.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.shopsphere.model.Category;
import com.shopsphere.service.ProductService;

/** Category management: JTable + form with JRadioButtons for the status. */
public class CategoryFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final ProductService productService = new ProductService();
    private final DefaultTableModel model = Ui.readOnlyModel("ID", "Name", "Description", "Products", "Active");
    private final JTable table = Ui.table(model);
    private final JTextField nameField = new JTextField(20);
    private final JTextField descriptionField = new JTextField(20);
    private final JRadioButton activeRadio = new JRadioButton("Active", true);
    private final JRadioButton inactiveRadio = new JRadioButton("Inactive");
    private List<Category> categories;
    private int selectedId;

    public CategoryFrame() {
        setTitle("ShopSphere Admin - Categories");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        top.add(Ui.title("Categories"), BorderLayout.WEST);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        ButtonGroup group = new ButtonGroup();
        group.add(activeRadio);
        group.add(inactiveRadio);

        JPanel form = new JPanel(new GridLayout(4, 2, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Category details"));
        form.add(new JLabel("Name"));
        form.add(nameField);
        form.add(new JLabel("Description"));
        form.add(descriptionField);
        form.add(new JLabel("Status"));
        JPanel radios = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        radios.add(activeRadio);
        radios.add(inactiveRadio);
        form.add(radios);

        JButton newButton = new JButton("New");
        JButton saveButton = Ui.primaryButton("Save");
        JButton deleteButton = new JButton("Delete");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        buttons.add(newButton);
        buttons.add(saveButton);
        buttons.add(deleteButton);
        form.add(new JLabel());
        form.add(buttons);

        JPanel south = new JPanel(new BorderLayout());
        south.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        south.add(form);
        add(south, BorderLayout.SOUTH);

        newButton.addActionListener(e -> clear());
        saveButton.addActionListener(e -> save());
        deleteButton.addActionListener(e -> delete());
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) showSelected();
        });

        setSize(720, 480);
        setLocationRelativeTo(null);
        load();
    }

    private void load() {
        try {
            categories = productService.getAllCategories();
            model.setRowCount(0);
            for (Category c : categories) {
                model.addRow(new Object[]{c.getCategoryId(), c.getName(), c.getDescription(), c.getProductCount(),
                        c.isStatus() ? "Yes" : "No"});
            }
        } catch (RuntimeException e) {
            Ui.error(this, e);
        }
    }

    private void showSelected() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        selectedId = (Integer) model.getValueAt(table.convertRowIndexToModel(row), 0);
        for (Category c : categories) {
            if (c.getCategoryId() == selectedId) {
                nameField.setText(c.getName());
                descriptionField.setText(c.getDescription());
                activeRadio.setSelected(c.isStatus());
                inactiveRadio.setSelected(!c.isStatus());
            }
        }
    }

    private void clear() {
        selectedId = 0;
        table.clearSelection();
        nameField.setText("");
        descriptionField.setText("");
        activeRadio.setSelected(true);
    }

    private void save() {
        try {
            Category c = new Category();
            c.setCategoryId(selectedId);
            c.setName(nameField.getText());
            c.setDescription(descriptionField.getText().trim());
            c.setStatus(activeRadio.isSelected());
            productService.saveCategory(c);
            load();
            clear();
        } catch (RuntimeException e) {
            Ui.error(this, e);
        }
    }

    private void delete() {
        if (selectedId == 0) {
            Ui.info(this, "Select a category first.");
            return;
        }
        if (!Ui.confirm(this, "Delete this category? Its products will become uncategorised.")) return;
        try {
            productService.deleteCategory(selectedId);
            load();
            clear();
        } catch (RuntimeException e) {
            Ui.error(this, e);
        }
    }
}
