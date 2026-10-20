package com.shopsphere.service;

import java.util.regex.Pattern;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;
import com.shopsphere.util.PasswordUtil;

/** Registration, login and profile rules. */
public class AuthService {

    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private static final Pattern MOBILE = Pattern.compile("^[6-9]\\d{9}$");

    private final UserDAO userDAO = new UserDAO();

    public User register(String name, String email, String password, String confirmPassword, String mobile) {
        name = trim(name);
        email = trim(email).toLowerCase();
        mobile = trim(mobile);

        if (name.length() < 2) throw new ServiceException("Please enter your full name.");
        if (!EMAIL.matcher(email).matches()) throw new ServiceException("Please enter a valid email address.");
        if (!mobile.isEmpty() && !MOBILE.matcher(mobile).matches())
            throw new ServiceException("Please enter a valid 10-digit mobile number.");
        validatePassword(password);
        if (!password.equals(confirmPassword)) throw new ServiceException("Passwords do not match.");
        if (userDAO.findByEmail(email) != null)
            throw new ServiceException("An account with this email already exists.");

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setMobile(mobile.isEmpty() ? null : mobile);
        user.setPasswordHash(PasswordUtil.hash(password));
        user.setUserId(userDAO.insert(user));
        return user;
    }

    public User login(String email, String password) {
        User user = userDAO.findByEmail(trim(email).toLowerCase());
        if (user == null || !PasswordUtil.verify(password, user.getPasswordHash()))
            throw new ServiceException("Invalid email or password.");
        if (!user.isActive())
            throw new ServiceException("Your account has been blocked. Please contact support.");
        return user;
    }

    public User updateProfile(int userId, String name, String mobile) {
        name = trim(name);
        mobile = trim(mobile);
        if (name.length() < 2) throw new ServiceException("Please enter your full name.");
        if (!mobile.isEmpty() && !MOBILE.matcher(mobile).matches())
            throw new ServiceException("Please enter a valid 10-digit mobile number.");
        userDAO.updateProfile(userId, name, mobile.isEmpty() ? null : mobile);
        return userDAO.findById(userId);
    }

    public void changePassword(int userId, String currentPassword, String newPassword, String confirmPassword) {
        User user = userDAO.findById(userId);
        if (user == null || !PasswordUtil.verify(currentPassword, user.getPasswordHash()))
            throw new ServiceException("Current password is incorrect.");
        validatePassword(newPassword);
        if (!newPassword.equals(confirmPassword)) throw new ServiceException("New passwords do not match.");
        userDAO.updatePassword(userId, PasswordUtil.hash(newPassword));
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 6)
            throw new ServiceException("Password must be at least 6 characters long.");
    }

    static String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
