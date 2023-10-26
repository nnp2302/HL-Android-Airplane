package com.example.airplane_android.utils;

import java.util.Calendar;
import java.util.regex.Pattern;

public class ValidateUtils {
    public static boolean checkEmail(String email) {
        final String regex = "^[a-zA-Z\\d._%+-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, email);
    }

    public static boolean checkPassword(String password) {
        final String regex = "^(?=.*[A-Z]).{8,}$";
        return Pattern.matches(regex, password);
    }

    public static boolean checkRegisterPasswordMatch(String password, String rePassword) {
        return password.equals(rePassword);
    }

    public static boolean checkPhoneNumber(String phone) {
        final String regex = "\\b(0\\d{9,10}|0\\d{3}-\\d{3}-\\d{4})\\b";
        return Pattern.matches(regex, phone);
    }

    public static boolean checkBirthDay(String birth) {
        final String[] birthSplit = birth.split("/");
        final int birthOfYear = Integer.parseInt(birthSplit[2]);
        final Calendar calendar = Calendar.getInstance();
        int[] currentYear = {calendar.get(Calendar.YEAR)};

        return (currentYear[0] - birthOfYear) < 16;
    }

    public static boolean checkIdCard(String id) {
        final String regex = "^(0\\d{11}|[12389]\\d{8})$\n";
        return Pattern.matches(regex, id);
    }

    public static boolean checkContainsNumeric(String text) {
        final String regex = ".*\\d+.*";
        return Pattern.matches(regex, text);
    }
}
