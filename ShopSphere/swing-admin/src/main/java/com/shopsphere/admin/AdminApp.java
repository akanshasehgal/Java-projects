package com.shopsphere.admin;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.shopsphere.admin.controller.LoginController;
import com.shopsphere.admin.view.LoginFrame;

/** Entry point of the ShopSphere desktop administration application. */
public class AdminApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Pluggable Look and Feel: start with the operating system's look
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // keep the default (Metal) look and feel
            }
            LoginFrame frame = new LoginFrame();
            new LoginController(frame);
            frame.setVisible(true);
        });
    }
}
