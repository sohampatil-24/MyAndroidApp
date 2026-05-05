package com.cleanerapp.utils;

import android.util.Patterns;

public class ValidationUtils {

    private ValidationUtils() {}

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) return false;
        return phone.trim().length() >= 10;
    }

    public static boolean isValidName(String name) {
        return name != null && name.trim().length() >= 2;
    }

    public static boolean isValidPrice(String price) {
        try {
            double p = Double.parseDouble(price);
            return p > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String getEmailError(String email) {
        if (email == null || email.isEmpty()) return "Email is required";
        if (!isValidEmail(email)) return "Enter a valid email address";
        return null;
    }

    public static String getPasswordError(String password) {
        if (password == null || password.isEmpty()) return "Password is required";
        if (!isValidPassword(password)) return "Password must be at least 6 characters";
        return null;
    }

    public static String getNameError(String name) {
        if (name == null || name.isEmpty()) return "Name is required";
        if (!isValidName(name)) return "Name must be at least 2 characters";
        return null;
    }

    public static String getPhoneError(String phone) {
        if (phone == null || phone.isEmpty()) return "Phone is required";
        if (!isValidPhone(phone)) return "Enter a valid phone number";
        return null;
    }
}
