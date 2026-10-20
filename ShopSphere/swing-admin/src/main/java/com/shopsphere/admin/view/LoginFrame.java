package com.shopsphere.admin.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/** View: admin login window. The LoginController attaches the behaviour. */
public class LoginFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public final JTextField emailField = new JTextField("admin@shopsphere.com", 22);
    public final JPasswordField passwordField = new JPasswordField(22);
    public final JCheckBox showPassword = new JCheckBox("Show password");
    public final JButton loginButton = Ui.primaryButton("Login");
    public final JLabel statusLabel = new JLabel(" ");

    private final char defaultEcho = passwordField.getEchoChar();

    public LoginFrame() {
        setTitle("ShopSphere Admin - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("ShopSphere Admin", SwingConstants.CENTER);
        header.setOpaque(true);
        header.setBackground(Ui.BRAND_DARK);
        header.setForeground(Color.WHITE);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 22f));
        header.setBorder(BorderFactory.createEmptyBorder(18, 10, 18, 10));
        add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0; form.add(new JLabel("Email"), c);
        c.gridx = 1; form.add(emailField, c);
        c.gridx = 0; c.gridy = 1; form.add(new JLabel("Password"), c);
        c.gridx = 1; form.add(passwordField, c);
        c.gridx = 1; c.gridy = 2; form.add(showPassword, c);
        c.gridx = 1; c.gridy = 3; c.fill = GridBagConstraints.HORIZONTAL; form.add(loginButton, c);
        c.gridx = 0; c.gridy = 4; c.gridwidth = 2; form.add(statusLabel, c);
        statusLabel.setForeground(new Color(185, 28, 28));
        add(form, BorderLayout.CENTER);

        JLabel hint = new JLabel("Demo admin: admin@shopsphere.com / admin123", SwingConstants.CENTER);
        hint.setForeground(Color.GRAY);
        hint.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        add(hint, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(loginButton);      // Enter key logs in
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public void setPasswordVisible(boolean visible) {
        passwordField.setEchoChar(visible ? (char) 0 : defaultEcho);
    }
}
