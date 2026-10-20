package com.shopsphere.admin.controller;

import com.shopsphere.admin.view.AdminDashboard;
import com.shopsphere.admin.view.LoginFrame;
import com.shopsphere.admin.view.Ui;
import com.shopsphere.model.User;
import com.shopsphere.service.AuthService;
import com.shopsphere.service.ServiceException;

/** Controller: checks the credentials and opens the dashboard for administrators only. */
public class LoginController {

    private final LoginFrame view;
    private final AuthService authService = new AuthService();

    public LoginController(LoginFrame view) {
        this.view = view;
        view.loginButton.addActionListener(e -> login());
        view.showPassword.addActionListener(e -> view.setPasswordVisible(view.showPassword.isSelected()));
    }

    private void login() {
        String email = view.emailField.getText();
        String password = new String(view.passwordField.getPassword());
        try {
            User user = authService.login(email, password);
            if (!user.isAdmin()) throw new ServiceException("Only administrators can use this application.");
            view.dispose();
            new AdminDashboard(user).setVisible(true);
        } catch (ServiceException e) {
            view.statusLabel.setText(e.getMessage());
            view.passwordField.setText("");
        } catch (RuntimeException e) {
            Ui.error(view, e);
        }
    }
}
