package com.shopsphere.admin.controller;

import java.util.List;

import com.shopsphere.admin.view.ProductFrame;
import com.shopsphere.admin.view.Ui;
import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.service.ProductService;
import com.shopsphere.service.ServiceException;

/** Controller for ProductFrame: loads the table, fills the form and saves changes through the service layer. */
public class ProductController {

    private final ProductFrame view;
    private final ProductService productService = new ProductService();
    private List<Product> products;
    private Product selected;

    public ProductController(ProductFrame view) {
        this.view = view;

        view.searchButton.addActionListener(e -> loadProducts());
        view.searchField.addActionListener(e -> loadProducts());
        view.newButton.addActionListener(e -> clearForm());
        view.saveButton.addActionListener(e -> save());
        view.toggleButton.addActionListener(e -> toggleVisibility());
        view.table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) showSelected();
        });

        loadCategories();
        loadProducts();
    }

    private void loadCategories() {
        try {
            view.categoryBox.removeAllItems();
            view.categoryBox.addItem(new Category(0, "- None -"));
            for (Category c : productService.getAllCategories()) view.categoryBox.addItem(c);
        } catch (RuntimeException e) {
            Ui.error(view, e);
        }
    }

    private void loadProducts() {
        try {
            String q = view.searchField.getText().trim();
            products = q.isEmpty() ? productService.getAllProducts() : productService.searchAll(q);
            view.tableModel.setRowCount(0);
            for (Product p : products) {
                view.tableModel.addRow(new Object[]{
                        p.getProductId(), p.getName(), p.getBrand(), p.getCategoryName(),
                        p.getPrice(), p.getDiscount(), p.getFinalPrice(), p.getStock(), p.isStatus() ? "Yes" : "No"});
            }
        } catch (RuntimeException e) {
            Ui.error(view, e);
        }
    }

    private void showSelected() {
        int row = view.table.getSelectedRow();
        if (row < 0) return;
        int id = (Integer) view.tableModel.getValueAt(view.table.convertRowIndexToModel(row), 0);
        for (Product p : products) {
            if (p.getProductId() == id) selected = p;
        }
        if (selected == null) return;

        view.idLabel.setText("#" + selected.getProductId());
        view.nameField.setText(selected.getName());
        view.brandField.setText(selected.getBrand());
        view.priceField.setText(String.valueOf(selected.getPrice()));
        view.discountField.setText(String.valueOf(selected.getDiscount()));
        view.stockField.setText(String.valueOf(selected.getStock()));
        view.descriptionArea.setText(selected.getDescription());
        view.activeBox.setSelected(selected.isStatus());
        for (int i = 0; i < view.categoryBox.getItemCount(); i++) {
            if (view.categoryBox.getItemAt(i).getCategoryId() == selected.getCategoryId()) view.categoryBox.setSelectedIndex(i);
        }
    }

    private void clearForm() {
        selected = null;
        view.table.clearSelection();
        view.idLabel.setText("New product");
        view.nameField.setText("");
        view.brandField.setText("");
        view.priceField.setText("");
        view.discountField.setText("0");
        view.stockField.setText("0");
        view.descriptionArea.setText("");
        view.activeBox.setSelected(true);
        view.categoryBox.setSelectedIndex(0);
        view.nameField.requestFocus();
    }

    private void save() {
        try {
            Product p = new Product();
            p.setProductId(selected == null ? 0 : selected.getProductId());
            p.setImageUrl(selected == null ? null : selected.getImageUrl());
            p.setName(view.nameField.getText());
            p.setBrand(view.brandField.getText().trim());
            Category category = (Category) view.categoryBox.getSelectedItem();
            p.setCategoryId(category == null ? 0 : category.getCategoryId());
            p.setPrice(parseDouble(view.priceField.getText(), "MRP"));
            p.setDiscount(parseDouble(view.discountField.getText(), "Discount"));
            p.setStock((int) parseDouble(view.stockField.getText(), "Stock"));
            p.setDescription(view.descriptionArea.getText().trim());
            p.setStatus(view.activeBox.isSelected());

            productService.saveProduct(p);
            Ui.info(view, "Product \"" + p.getName() + "\" saved.");
            loadProducts();
            clearForm();
        } catch (RuntimeException e) {
            Ui.error(view, e);
        }
    }

    private void toggleVisibility() {
        if (selected == null) {
            Ui.info(view, "Select a product in the table first.");
            return;
        }
        try {
            productService.setProductStatus(selected.getProductId(), !selected.isStatus());
            loadProducts();
            clearForm();
        } catch (RuntimeException e) {
            Ui.error(view, e);
        }
    }

    private double parseDouble(String text, String field) {
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            throw new ServiceException(field + " must be a number.");
        }
    }
}
