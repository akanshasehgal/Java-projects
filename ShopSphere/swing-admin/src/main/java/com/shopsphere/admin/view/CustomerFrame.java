package com.shopsphere.admin.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.shopsphere.model.User;
import com.shopsphere.service.AdminService;

/** Customer list with block / unblock. */
public class CustomerFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final AdminService adminService = new AdminService();
    private final DefaultTableModel model = Ui.readOnlyModel("ID", "Name", "Email", "Mobile", "Role", "Status", "Joined");
    private final JTable table = Ui.table(model);
    private final User admin;

    public CustomerFrame(User admin) {
        this.admin = admin;
        setTitle("ShopSphere Admin - Customers");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        top.add(Ui.title("Customers"), BorderLayout.WEST);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton block = new JButton("Block");
        JButton unblock = new JButton("Unblock");
        JButton refresh = new JButton("Refresh");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(refresh);
        buttons.add(unblock);
        buttons.add(block);
        add(buttons, BorderLayout.SOUTH);

        block.addActionListener(e -> setBlocked(true));
        unblock.addActionListener(e -> setBlocked(false));
        refresh.addActionListener(e -> load());

        setSize(820, 450);
        setLocationRelativeTo(null);
        load();
    }

    private void load() {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy");
            model.setRowCount(0);
            for (User u : adminService.getUsers()) {
                model.addRow(new Object[]{u.getUserId(), u.getName(), u.getEmail(), u.getMobile(), u.getRole(),
                        u.getStatus(), u.getCreatedAt() == null ? "" : fmt.format(u.getCreatedAt())});
            }
        } catch (RuntimeException e) {
            Ui.error(this, e);
        }
    }

    private void setBlocked(boolean blocked) {
        int row = table.getSelectedRow();
        if (row < 0) {
            Ui.info(this, "Select a user first.");
            return;
        }
        int id = (Integer) model.getValueAt(table.convertRowIndexToModel(row), 0);
        try {
            adminService.setUserBlocked(id, blocked, admin.getUserId());
            load();
        } catch (RuntimeException e) {
            Ui.error(this, e);
        }
    }
}
