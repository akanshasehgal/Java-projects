package com.shopsphere.admin.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.shopsphere.service.ServiceException;

/** Small helpers shared by all Swing screens. */
public final class Ui {

    public static final Color BRAND = new Color(79, 70, 229);
    public static final Color BRAND_DARK = new Color(55, 48, 163);

    private static final NumberFormat RUPEES = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-IN"));

    private Ui() {
    }

    /** A table model whose cells cannot be edited by typing. */
    public static DefaultTableModel readOnlyModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public static JTable table(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setReorderingAllowed(false);
        return table;
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 18f));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        return label;
    }

    public static JButton primaryButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(BRAND);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        return b;
    }

    public static String money(double amount) {
        return RUPEES.format(amount);
    }

    public static void info(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "ShopSphere", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "ShopSphere", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION;
    }

    /** Business errors are shown as-is; database errors also explain how to fix the usual cause. */
    public static void error(Component parent, Exception e) {
        String message;
        if (e instanceof ServiceException) {
            message = e.getMessage();
        } else {
            Throwable root = e;
            while (root.getCause() != null) root = root.getCause();
            message = "Database error: " + root.getMessage()
                    + "\n\nIs MySQL running and are the settings in db.properties correct?";
        }
        JOptionPane.showMessageDialog(parent, message, "ShopSphere", JOptionPane.ERROR_MESSAGE);
    }
}
