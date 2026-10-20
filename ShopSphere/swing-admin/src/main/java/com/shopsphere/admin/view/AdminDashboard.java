package com.shopsphere.admin.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.shopsphere.admin.controller.LoginController;
import com.shopsphere.admin.controller.ProductController;
import com.shopsphere.model.DashboardStats;
import com.shopsphere.model.User;
import com.shopsphere.service.AdminService;

/** Main admin window: statistics plus buttons that open each management screen. */
public class AdminDashboard extends JFrame {

    private static final long serialVersionUID = 1L;

    private final AdminService adminService = new AdminService();
    private final JLabel[] statValues = new JLabel[6];
    private static final String[] STAT_NAMES = {"Total sales", "Orders", "To process", "Customers", "Products", "Low stock"};

    public AdminDashboard(User admin) {
        setTitle("ShopSphere Admin - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setJMenuBar(buildMenu());

        JLabel welcome = new JLabel("Welcome, " + admin.getName());
        welcome.setFont(welcome.getFont().deriveFont(Font.BOLD, 20f));
        welcome.setBorder(BorderFactory.createEmptyBorder(14, 16, 0, 16));
        add(welcome, BorderLayout.NORTH);

        JPanel stats = new JPanel(new GridLayout(2, 3, 12, 12));
        stats.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        Color[] colors = {new Color(22, 163, 74), Ui.BRAND, new Color(37, 99, 235),
                new Color(124, 58, 237), new Color(217, 119, 6), new Color(220, 38, 38)};
        for (int i = 0; i < STAT_NAMES.length; i++) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(colors[i]);
            card.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
            JLabel name = new JLabel(STAT_NAMES[i]);
            name.setForeground(Color.WHITE);
            statValues[i] = new JLabel("-");
            statValues[i].setForeground(Color.WHITE);
            statValues[i].setFont(statValues[i].getFont().deriveFont(Font.BOLD, 24f));
            card.add(name, BorderLayout.NORTH);
            card.add(statValues[i], BorderLayout.CENTER);
            stats.add(card);
        }
        add(stats, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1, 6, 8, 8));
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));
        JButton products = Ui.primaryButton("Products");
        JButton categories = new JButton("Categories");
        JButton customers = new JButton("Customers");
        JButton orders = new JButton("Orders");
        JButton inventory = new JButton("Inventory");
        JButton refresh = new JButton("Refresh");
        for (JButton b : new JButton[]{products, categories, customers, orders, inventory, refresh}) buttons.add(b);
        add(buttons, BorderLayout.SOUTH);

        products.addActionListener(e -> {
            ProductFrame frame = new ProductFrame();
            new ProductController(frame);
            frame.setVisible(true);
        });
        categories.addActionListener(e -> new CategoryFrame().setVisible(true));
        customers.addActionListener(e -> new CustomerFrame(admin).setVisible(true));
        orders.addActionListener(e -> new OrderFrame().setVisible(true));
        inventory.addActionListener(e -> new InventoryFrame().setVisible(true));
        refresh.addActionListener(e -> loadStats());

        setSize(760, 380);
        setLocationRelativeTo(null);
        loadStats();
    }

    public void loadStats() {
        try {
            DashboardStats s = adminService.getStats();
            statValues[0].setText(Ui.money(s.getTotalSales()));
            statValues[1].setText(String.valueOf(s.getTotalOrders()));
            statValues[2].setText(String.valueOf(s.getPendingOrders()));
            statValues[3].setText(String.valueOf(s.getTotalCustomers()));
            statValues[4].setText(String.valueOf(s.getTotalProducts()));
            statValues[5].setText(String.valueOf(s.getLowStockCount()));
        } catch (RuntimeException e) {
            Ui.error(this, e);
        }
    }

    private JMenuBar buildMenu() {
        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenuItem logout = new JMenuItem("Logout");
        JMenuItem exit = new JMenuItem("Exit");
        logout.addActionListener(e -> {
            for (Window w : Window.getWindows()) w.dispose();
            LoginFrame login = new LoginFrame();
            new LoginController(login);
            login.setVisible(true);
        });
        exit.addActionListener(e -> System.exit(0));
        file.add(logout);
        file.addSeparator();
        file.add(exit);
        bar.add(file);

        // Pluggable Look and Feel: switch the appearance of every open window at runtime
        JMenu look = new JMenu("Look & Feel");
        ButtonGroup group = new ButtonGroup();
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(info.getName());
            item.setSelected(info.getClassName().equals(UIManager.getLookAndFeel().getClass().getName()));
            item.addActionListener(e -> applyLookAndFeel(info.getClassName()));
            group.add(item);
            look.add(item);
        }
        bar.add(look);
        return bar;
    }

    private void applyLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            for (Window w : Window.getWindows()) SwingUtilities.updateComponentTreeUI(w);
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }
}
